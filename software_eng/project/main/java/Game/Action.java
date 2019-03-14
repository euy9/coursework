package Game;

import java.util.Random;
import javax.swing.*;

import static Game.OaklandOligarchy.player;
import static Game.OaklandOligarchy.playerNum;
import static Game.Button.property;


@SuppressWarnings({ "static-access", "static-access", "static-access" })
public class Action 
{
	//static GameLabel label = new Label();
	static Player currentPlayer;
	static final int ACTION_LOCATION1 = 7;
	static final int ACTION_LOCATION2 = 14;
	static final int ACTION_LOCATION3 = 22;
	static final int ACTION_LOCATION4 = 32;	
	static final int MAX = 13;
	static final int MIN = 1;
	static final int JAIL = 10;
	static final int START = 1;
	static final int RITE_AID = 33;
	static final int FREE_PARKING = 19;
	static final int STAR_BUCKS = 6;
	static final int PITT_SHOP = 35;	
	static final int PAY_MONEY_1 =  50;
	static final int PAY_MONEY_2 =  70;
	static final int COLLECT_MONEY_1 = 300;
	static final int COLLECT_MONEY_2 = 500;
	static final int FORWARD_STEPS = 4;
	static final int BACKWARD_STEPS = 3;
	static boolean isAction;
	static int randomNum;
	static int currentPos;
	static int curPlayerIndex = 0;      //Indicate location of the current player in the player object array
	static int countRound = 0;
	static int playerIndex;
	static int YES_CARD = 1;
	static int NO_CARD = 0;
	static String noPayCard = "noPayCard";
	static String doublePayCard = "doublePayCard";
	static String playerName;
	static Property desiredProperty;
	static Property getRidOfProperty;
	static int targetSwappingPlayerIndex = 0;
	static boolean swap;
	
	
	// Constructor
	public Action() {
		currentPlayer = GameListener.getActionPlayer();     	// Get the current player in action
		playerIndex = GameListener.getActionPlayerIndex();
		currentPos = GameListener.getCurrentLocation();
	}
	
	/**
	 * Based on random number we get, decide what action to take
	 */
	public void actionSlot() {
		//randomNum = generateRandomNum();
		randomNum = 11;
		switch(randomNum) {
		case 1:		
			actionDialog("Action:\nSorry, jail time!");
			goToLocation(JAIL);
			break;
		case 2:		
			actionDialog("Action:\nPay $50 to every player!");
			payEveryone(PAY_MONEY_1);
			break;
		case 3:			
			actionDialog("Action:\nPay $70 to every player!");
			payEveryone(PAY_MONEY_2);
			break;
		case 4:
			actionDialog("Action:\nGo all the way back to the START!");
			goToLocation(START);
			break;
		case 5:
			actionDialog("Congratulation:\n\nYou earned $300!");
			earnedMoney(COLLECT_MONEY_1);
			break;
		case 6:
			actionDialog("Congratulation:\n\nYou earned $500!");
			earnedMoney(COLLECT_MONEY_2);
			break;
		case 7:
			actionDialog("Action:\nGo to Rite Aid!");
			goToLocation(RITE_AID);
			break;
		case 8:
			actionDialog("Action:\nYou got a free parking!");
			goToLocation(FREE_PARKING);
			break;
		case 9:
			actionDialog("Action:\nGet a coffee from starBucks!");
			goToLocation(STAR_BUCKS);
			break;
		case 10:
			actionDialog("Action:\nIt's time to get a pitt T-shirt from Pitt Shop!");
			goToLocation(PITT_SHOP);
			break;
		case 11:
			payDouble(); 
			break;
		case 12:
			actionDialog("Action:\nPlease move 4 steps forward!");
			moveTowardsDirection("forward", FORWARD_STEPS);
			break;
		case 13:
			actionDialog("Action:\nPlease move 3 steps backward!");
			moveTowardsDirection("backward", BACKWARD_STEPS);
			break;
		case 14:
			actionDialog("Action:\nGo back to your previous location!");
			moveTowardsDirection("backward", GameListener.getStepsMoved());
			break;
		case 15:
			actionDialog("Action:\nSay No! \nUse this card to not pay a rent to next landed property owner!");
			player[playerIndex].setNoPayCard(true);
			updateStatus(playerIndex);
			break;
		case 16:		
			if( gotProperty(playerIndex) == true) {
				actionDialog("Action:\nYou can swap one of your property with player you select!");
				swapProperty();
			} else {
				actionDialog("Sorry:\nYou cannot swap with others without property!"
						+ "\nGo to get some property.");
			}		 
			break;		
		}
	}
	/**
	 * This method will get called when the swapProperty action is on the action card
	 */
	public void swapProperty() {
            listOfPlayer();
            targetSwappingPlayerIndex = targetPlayerIndex(playerName);
            // Get a player to swap

            if( gotProperty(targetSwappingPlayerIndex) == true) {

                // Get target player's property for exchange
                desiredProperty = property[targetProperty(getPropertyArray(targetSwappingPlayerIndex), targetSwappingPlayerIndex)];
                // Get your property for exchange
                getRidOfProperty = property[targetProperty(getPropertyArray(playerIndex), playerIndex)];    

                int exchange = JOptionPane.showConfirmDialog(null, "Are you sure about exhange property!", null, JOptionPane.YES_NO_OPTION);
                if ( exchange == JOptionPane.YES_OPTION ) {  			
                    currentPlayer.addProp(desiredProperty);    							// Add the property to your property list
                    currentPlayer.removeProperty(getRidOfProperty);					// Remove the property you don't want

                    player[targetSwappingPlayerIndex].addProp(getRidOfProperty);	// Target player gets the property you don't want
                    player[targetSwappingPlayerIndex].removeProperty(desiredProperty);//Target player lose the property you desired 
                    
                    // update ownedPropertyList
                    GameLabel.ownedPropertyList[Button.getPropertyIndexByName(desiredProperty.getName())] = playerIndex;
                    GameLabel.ownedPropertyList[Button.getPropertyIndexByName(getRidOfProperty.getName())] = targetSwappingPlayerIndex;
                    
                }	
            } else {
                    actionDialog("Sorry:\nSelected player doesn't have any property!");
            }
            GameLabel.updateStatus();

	}
	/**
	 * This method will check if player him/herself to have property to swap,
	 * it also checks if the target player has property to swap with.
	 * @return swap: boolean variable to represente if you have property
	 */
	public boolean gotProperty(int _playerIndex) {
		if(GameLabel.combo[_playerIndex].getItemCount() <= 0) {
			swap = false;
		} else {
			swap = true;
		}
		return swap;
	}
	
	/**
	 * This method will provide player with player list to swap property with
	 * @return input: String representation of user input from list of player
	 */
	public String listOfPlayer() {
		String[] swapChoices = new String[playerNum];	
		for(int counter = 0; counter < playerNum; counter++) {
			if(!player[counter].getName().equals(currentPlayer.getName())) {    //Exclude yourself from swapping player list
				swapChoices[counter] = player[counter].getName();
			}
		} 	
	    playerName = (String) JOptionPane.showInputDialog(null, "Choose player you want to swap property with:",
	    		null, JOptionPane.QUESTION_MESSAGE, null, swapChoices, swapChoices[1]);
	    return playerName;
	}
	/**
	 * This method use "input" -- selected to swap player name,
	 * to figure out the target player's index
	 * @return targetSwappingPlayerIndex: integer represent target player's index
	 */
	public int targetPlayerIndex(String playerName) {
		while(targetSwappingPlayerIndex < playerNum) {
			if(playerName.equals(player[targetSwappingPlayerIndex].getName())) {
				break;
			}
			targetSwappingPlayerIndex++;
		}
		return targetSwappingPlayerIndex;	
	}
	
	/**
	 * This method gets target player's property
	 * @param _propertyList: String array represent list of property for the player
	 * @return desiredProperty:
	 */
	public int targetProperty(String[] _propertyList, int _playerIndex) {
		
            String propertyName = null;
            if(_playerIndex == playerIndex) {
                    propertyName =  (String) JOptionPane.showInputDialog(null, "Choose property you want to get:",
                               null, JOptionPane.QUESTION_MESSAGE, null, _propertyList, _propertyList[0]);	
            } else if ( _playerIndex == targetSwappingPlayerIndex ) {
                    propertyName =  (String) JOptionPane.showInputDialog(null, "Choose property you want to get rid of:",
                            null, JOptionPane.QUESTION_MESSAGE, null, _propertyList, _propertyList[0]);
            }
            return Button.getPropertyIndexByName(propertyName);
	}
	
	/**
	 * select from your list of own property to exchange with target player's
	 * selected property 
	 * @return getRidOfProperty: String representation of property you want to get rid of
	 */
	/*
	public String yourProperty() {		
		String[] _propertyList = getPropertyArray( playerIndex );
		getRidOfProperty = (String) JOptionPane.showInputDialog(null, "Choose property you want to get rid of:",
    			null, JOptionPane.QUESTION_MESSAGE, null, _propertyList, _propertyList[0]);
		return getRidOfProperty;
	}
	*/
	/*
	* to get the property array with the assigned parameters
	*
	*/
	public String[] getPropertyArray(int index){
		
            String[] propertyList = new String[GameLabel.combo[index].getItemCount()];
            for(int x = 0; x < GameLabel.combo[index].getItemCount(); x++) {
                propertyList[x] = (String) GameLabel.combo[index].getItemAt(x);
            }	
            return propertyList;
	}
	/*
	*exactly the same method as above, except it is in static, just for the gamelistener part
	*
	*/
	public static String[] getPropertyArrayStatic(int index){
		
            String[] propertyList = new String[GameLabel.combo[index].getItemCount()];
                for(int x = 0; x < GameLabel.combo[index].getItemCount(); x++) {
                    propertyList[x] = (String) GameLabel.combo[index].getItemAt(x);
                }	
            return propertyList;
	}
	
	/**
	 * Player can use the noPay card to not pay rent one time to next landed property owner
	 */
	public void sayNo() {
		actionDialog("Luck You!\n\nYour NoPay card is applied to this rent!\nNo need to pay anything!");
		player[playerIndex].setNoPayCard(false); 
		updateStatus(playerIndex);
	}
	
	
	/**
	 * This method will decide what card player has and if the card is 1--return Yes, else return 0
	 */
	public void payDouble() {
		
		if(getCardInfo(playerIndex, noPayCard).equals("Yes")) {
			actionDialog("Luck You:\n\nSince you have NoPay card!\n You don't need to pay double!");
			player[playerIndex].setNoPayCard(false);
			updateStatus(playerIndex);
		} 
		if(getCardInfo(playerIndex, noPayCard).equals("No")) {
			actionDialog("ACTION:\n\nYou going to pay double in the next landing!");
                        player[playerIndex].setDoublePayCard(true);
			updateStatus(playerIndex);
		}
	}
	
	/**
	 * Check if see if this player owns noPay card and doublePay card
	 * @param _playerIndex: integer represent player index in the player array
	 * @param typeOfCard: string represent either noPay card or double pay card
	 * @return string representation of the special action card
	 */
	 public static String getCardInfo(int _playerIndex, String typeOfCard) {
    	 String card = null;
    	 if(typeOfCard.equals(noPayCard)) {
    		 if(player[_playerIndex].getNoPayCard()) {
    	         	card = "Yes";
    	         } else {
    	         	card = "No";
    	         }
    	 }
         if(typeOfCard.equals(doublePayCard)) {
        	 if(player[_playerIndex].getDoublePayCard()) {
 	         	card = "Yes";
 	         } else {
 	         	card = "No";
 	         }
         }
         return card;
    }
	 /**
		 * This method update money on the Status bar 
		 * by refreshing the JextArea with updated money amount
		 */
		public void updateStatus(int _playerIndex) {
			GameLabel.updateStatus();
		}
	
	/**
	 * This method use player's current position alone with the 
	 * steps and direction it passed in to decide a new destination 
	 * @param headingDirection: string representation of where the player is heading to
	 * @param steps: integer representation of how many steps player going to move
	 */
	public void moveTowardsDirection(String headingDirection, int steps) {
		if(headingDirection.equalsIgnoreCase("forward")) {
			int forwardDestination = GameListener.getCurrentLocation() + steps;
			GameToken.movement(forwardDestination, playerIndex);
		} else {
			int backwardDestination = GameListener.getCurrentLocation() - steps;
			GameToken.movement(backwardDestination, playerIndex);
		}
	}
	
	/**
	 * This method takes in location parameter and 
	 * move the current player's icon to designated location
	 * @param location: integer represent location 
	 */
	public void goToLocation(int location) {
		switch(location) {
		case 1:
			GameToken.movement(START, playerIndex);
			break;
		case 6:
			GameToken.movement(STAR_BUCKS, playerIndex);
			break;
		case 10:
			GameToken.movement(JAIL, playerIndex);
			break;
		case 19:
			GameToken.movement(FREE_PARKING, playerIndex);
			break;
		case 33:
			GameToken.movement(RITE_AID, playerIndex);
			break;
		case 35:
			GameToken.movement(PITT_SHOP, playerIndex);
			break;
		}
	}
	
	
	/**
	 * This method display the message via JOptionPane
	 * @param actionDescription: String representation 
	 * of the message going to get displayed 
	 */
	public static void actionDialog(String actionDescription) {
		JOptionPane.showMessageDialog(null, actionDescription); 
	}
	
	
	/**
	 * It deducts money from current player and pay same 
	 * amount of money to other player
	 * @param fine: integer represent money need to 
	 * get deducted from current player's wallet
	 */
	public void payEveryone(int fine) {
		player[playerIndex].payMoney(fine * ( playerNum - 1));  
		updateStatus(playerIndex);							// Call payMoney method from player class	
		for(int x = 0; x < playerNum; x++) {
			if( !(player[x].equals(currentPlayer))) {			// Pay everyone but not to yourself
				player[x].receiveMoney(fine);
				updateStatus(x);
			}		
		}
	}
	
	/**
	 * It adds money to the current player's total assets
	 * with the amount it passed in
	 * @param lotteryAmount: integer represent earned money
	 */
	public void earnedMoney(int lotteryAmount) {
		player[playerIndex].receiveMoney(lotteryAmount);
		updateStatus(playerIndex);
	}
	
	/**
	 * Generate random number for action slot
	 * @return randomNum: integer (lower bound: 1 - upper bound: 13)
	 */
	public int generateRandomNum() {
		Random rand = new Random();
		randomNum = rand.nextInt((MAX - MIN) + MIN) + MIN;
		return randomNum;
	}
}
