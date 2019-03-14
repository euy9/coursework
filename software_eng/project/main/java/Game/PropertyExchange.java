package Game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.util.Random;
import java.util.*;
import javax.swing.*;

import static Game.OaklandOligarchy.player;
import static Game.OaklandOligarchy.playerNum;
import static Game.OaklandOligarchy.currentPlayerIndex;
import static Game.Button.property;


/**
 * This class is created to keep track of all the tradings within the game played
 */


public class PropertyExchange {
    static int curLocation = 1;                 // the curren't player's position
    static int[] ownedPropertyList = GameLabel.ownedPropertyList; 
    static final int ZERO = 0;
    static final int ONE = 1;
    static final int TWO = 2;
    static final int THREE = 3;
    static final int FOUR = 4;
    static final int MAX_DICE_ROLL = 6;
    static final int THIRTY_SEVEN = 37;
    static final int DOUBLE = 2;
    static boolean hasRolled = false;  
    static int priceArray[] = new int[4];
    static Player[] tradingTarget = new Player[3];
    static String[] buyerList = new String[4];
    static String price;
    static String auctionProp;
    static int propIndex;
    
	/*
	 * This method will provide auction for player at will or without money
	 */
    static void auctionAndTrade(JFrame frame, int _auctionIndex ){
    
    	String[] auctionedProperties = player[_auctionIndex].getProp();
        if (auctionedProperties.length == 0){
            JOptionPane.showMessageDialog(frame, player[_auctionIndex].getName() + 
                    ", you cannot auction because you do not own any property!");
            return;
        }
        auctionProp = (String) JOptionPane.showInputDialog(null, player[_auctionIndex].getName() + 
                ", select a property you want for auction: ",
                null, JOptionPane.QUESTION_MESSAGE, null, auctionedProperties, 
                auctionedProperties[0]);
        
    	System.out.println("what is auctionProp" +auctionProp);
    	// When player click okay to trade
    	if (auctionProp != null ) {  
    		 propIndex = Button.getPropertyIndexByName( auctionProp );

    			for(int x = 0; x < playerNum ; x++){
    				if(x != _auctionIndex ){
    					while(true) {					
    						price = (String) JOptionPane.showInputDialog( frame,
    								player[x].getName()+", how much do you want to pay for the property:\n"
    								+auctionProp, "Auction Log", JOptionPane.PLAIN_MESSAGE, null, null, "1");

    						if((!price.matches("[0-9]*") || (Integer.parseInt(price) > player[x].getMoney()))) {
    							continue;
    						} else {
    							buyerList[x] = player[x].getName().concat("\t $$: " + price);
    							priceArray[x] = Integer.parseInt(price);
    							break;			
    						}					
    					}
    				}
    			}
    			// This adjusted list will take out null for DialogBox display
    			String[] adjustedBuyerList = new String[playerNum - 1];
    			for(int x = 0; x < playerNum; x++) {
    				
    					if(buyerList[x] != null && x != _auctionIndex) {
    	                    adjustedBuyerList[x] = buyerList[x];
    					} else {
    						while(x < playerNum - 1) {
    							adjustedBuyerList[x] = buyerList[x+1];
    							x++;
    						}
    						break;
    					}
    				
    			}
    			String buyer = (String) JOptionPane.showInputDialog(null, "Choose player you want to trade property with: ",
    		    		null, JOptionPane.QUESTION_MESSAGE, null, adjustedBuyerList, adjustedBuyerList[0]);
    			
    			if(buyer != null) {
    				for(int x = 0; x < playerNum ; x++) {		
        				if(buyer == null) {
        					x++;
        				}
        				if(buyer.equals(buyerList[x])) {
        			    	player[x].payMoney(priceArray[x]);
        			    	player[_auctionIndex].receiveMoney(priceArray[x]);	    	
        			        player[x].addProp(property[propIndex]);       
        			        player[_auctionIndex].removeProperty(property[propIndex]);
        			        GameLabel.ownedPropertyList[propIndex] = x;
        			        GameLabel.updateStatus();
        			        break;
        				}
        			}
    			}		
    	}
        GameListener.totalRand = 3;
    }  
  
	
	/**
	*	to update hasrolled by gameListener
	*
	*/
	static void updateHasRolled(){
		hasRolled = true;
	}
	
	/**
    * this method will be called when clicking the trade button, it is the trade happen between the current player and another player that the current player choose
    * the current player will buy certain property from the assigned player and this function can only be called once per roll
    * @ param: frame
    */

    static void buyAndPay( JFrame frame, int updatedIndex, boolean rolledDecider ){
        currentPlayerIndex = updatedIndex;
		rolledDecider = hasRolled;
		if(hasRolled){
            List<String> traderName = new ArrayList<String>();
            int count = ZERO;
                while(count < player.length){
                    //get the player's name
                    if( !player[ count ].getName().equals ( player[currentPlayerIndex].getName() ) ){
                        //add the rest of the player names
                        traderName.add( player[ count ].getName() );
                    }
                    count++;
                }

            String[] optionNames = new String[traderName.size()];

            //fill up the trader names
            for(int i = 0; i < traderName.size(); i++){
                    optionNames[ i ] = traderName.get( i );
            }

            int tradingPlayerIndex = JOptionPane.showOptionDialog(frame,
            player[currentPlayerIndex].getName()+", Which player would you like to trade with ",
                    "Trading Option", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, optionNames,
                    optionNames[ZERO]);
			
			System.out.println("the index is "+tradingPlayerIndex+" the name is "+optionNames[ tradingPlayerIndex ]);
            //pass the name of the player and the property index to use the tradeWithPartner method
            tradeWithPartner( tradingPlayerIndex, optionNames[ tradingPlayerIndex ]);

            //reset hasRolled, make sure this method can be called once per roll
            hasRolled = false;
        } else{
            //can't trade twice per roll
			actionDialog(" can't trade twice in a row ");
        }
    }
	
	 /**
    * this method is called when the trading partner has been selected
    * @param: int tradingPlayerIndex               indicator of whether the trade is successfully or not
    * @param: String traderName                    the name of the trading partner, used to get the player index as key
    */
    public static void tradeWithPartner(int tradingPlayerIndex, String traderName){//when player buy from player 3,   payer index is 0 buyer index is 1
    JFrame frame = new JFrame("Trading Frame");
	//System.out.println("in the beginning the curplayer index is "+currentPlayerIndex);
        //making  sure the trade is not canceled
        if(tradingPlayerIndex != -1 ){
            int count = ZERO;

            //default setting
            int playerIndex = ZERO;
            while(count < player.length){
                //get the player's name
                if( player[count].getName().equals ( traderName ) ){
                        playerIndex = count;
                        break;
                }
                count++;
            }
   
            Player tradingPartner = player[ playerIndex ];  //to find a trading partnet
            int ans = JOptionPane.showConfirmDialog(frame, tradingPartner.getName() + " "
                    + "Would you like to trade with " + player[currentPlayerIndex].getName(),
                    "A Trading decision", JOptionPane.YES_NO_OPTION);
                    //agree to trade
            if(ans == ZERO){
   
                String[] partnerProperty = Action.getPropertyArrayStatic( playerIndex );

                //this part need to be fixed, right now, the logic sounds like rubbing
                if(partnerProperty.length == ZERO){
					actionDialog(" can't trade because your trading partner has no properties ");
                } else if(player[currentPlayerIndex].getMoney() == ZERO){
					actionDialog(" can't trade because you don't have enough money ");
                } else{
                    for( int i = 0;i < partnerProperty.length-1; i ++){
                        System.out.println("property is "+partnerProperty[i]);
                    }
                    String s = ( String )JOptionPane.showInputDialog(frame,
                            "Complete the trading:\n" + "\"you want to buy...\"",
                            "THIS IS A GOOD DEAL", JOptionPane.PLAIN_MESSAGE,
                            //this is the place for putting an icon in the original code
                            null, partnerProperty,"trade" );
							System.out.println("hola");
                    //successfully trade
                    if (( s != null ) && ( s.length() > ZERO )) 
                    {
                    //you need to find its price based on its name
                        int propertyIndex = Button.getPropertyIndexByName(s);
                        if(property[ propertyIndex ].getPrice() > ( player[currentPlayerIndex].getMoney() ) ){
							actionDialog(  " can't trade because your don't have enough money ");
                        }
                        else{
                            ////////////////////////////////////////////////////////////////////    auction only need this part
                            //curplayer pays for the property
                        	int buyingPrice = property[propertyIndex].getPrice();
							System.out.println("currentplayer is "+currentPlayerIndex);
                            completeTrading(frame, currentPlayerIndex , propertyIndex, playerIndex, buyingPrice );			//player1 try to buy player2's property
                            GameLabel.updateStatus();
                            ///////////////////////////////////////////////////////////
                        }
                    }
                }

            //  player doesn't agree to pay
            } else{
				actionDialog(" Trading unsuccessful, trading partner doesn't agree to trade ");
            }
        }

        //the trade is canceled
        else{
			actionDialog(" Trading unsuccessful, trading partner doesn't agree to trade ");
        }
    }
	
	
    /**
     * this method will complete the money and property transaction between two players, can be used for both the auction part and the trading part
     * @param JFrame frame: the place to put the text
     * @param payerIndex, the index of the payer
     * @param propertyIndex, the index of the property
     * @param sellerIndex, the index of the seller
    */
    static void completeTrading(JFrame frame, int payerIndex, int propertyIndex, int sellerIndex, int buyingPrice){
    	actionDialog("money trading with: " + buyingPrice);
    	player[payerIndex].payMoney(buyingPrice);
    	player[sellerIndex].receiveMoney(buyingPrice);
    	
    	actionDialog("buyer : " + player[payerIndex].getName());
    	actionDialog("seller: " + player[sellerIndex].getName());
    	
        player[payerIndex].addProp(property[propertyIndex]);      
        player[sellerIndex].removeProperty(property[propertyIndex]);
        GameLabel.ownedPropertyList[propertyIndex] = payerIndex;
        
        actionDialog(" trade complete ");
        GameLabel.updateStatus();
    }
	
 
    /**
     * This method facilitates paying rent for an owned property
     * @param playerIndex : the player to pay the rent
     * @param propertyIndex : the property the player has to pay the rent for
     * @param buyingPrice: the price of the property
     */
    static void payForThisProperty(int playerIndex, int propertyIndex, int buyingPrice){
        // player pays
        player[playerIndex].payMoney(buyingPrice);
        actionDialog("player who buys property is: " + player[playerIndex].getName());
        actionDialog(" MONEY IS: " + buyingPrice);
        // the property owner gets the money
        int propertyOnwerIndex = ownedPropertyList[propertyIndex];
        
        actionDialog("player who recieve money is: " + player[propertyOnwerIndex].getName());
        player[propertyOnwerIndex].receiveMoney(buyingPrice);
        GameLabel.updateStatus();
    }
	
	/**
	 * This method display the message via JOptionPane
	 * @param actionDescription: String representation 
	 * of the message going to get displayed a direct copy from the action.java
	 */
	public static void actionDialog(String actionDescription) {
		JOptionPane.showMessageDialog(null, actionDescription); 
	}

}