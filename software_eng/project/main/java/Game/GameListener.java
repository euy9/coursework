package Game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.util.Random;
import java.util.*;
import javax.swing.*;

import static Game.Button.property;
import static Game.OaklandOligarchy.player;
import static Game.OaklandOligarchy.playerNum;
import static Game.OaklandOligarchy.currentPlayerIndex;

/**
 * This class takes care of what happens after the "roll" button has been clicked
 */
public class GameListener implements ActionListener{
    //for passing the source of the button
    public static JButton rollButton;
    public static JButton tradeButton;
    public static JButton auctionButton;
    public static JButton teleportButton;
    public static JButton turnButton;

    // constants
    static final int ZERO = 0;
    static final int ONE = 1;
    static final int TWO = 2;
    static final int THREE = 3;
    static final int FOUR = 4;
    static final int MAX_DICE_ROLL = 6;
    static final int THIRTY_SEVEN = 37;
    static final int DOUBLE = 2;

    static JPanel p;
    static int stepsMoved;
    static volatile int totalRand;       // sum of the two die
    static int[] ownedPropertyList = GameLabel.ownedPropertyList;
    static int curLocation = 1;                 // the curren't player's position
    static int newLocation = 0;

    static boolean hasRolled = false;                                                                                                           //to keep track if changed cur player
    
    // get dice icon
    static ImageIcon icon1 = new ImageIcon(GameLabel.class.getResource("/1.png"));
    static ImageIcon icon2 = new ImageIcon(GameLabel.class.getResource("/2.png"));
    static ImageIcon icon3 = new ImageIcon(GameLabel.class.getResource("/3.png"));
    static ImageIcon icon4 = new ImageIcon(GameLabel.class.getResource("/4.png"));
    static ImageIcon icon5 = new ImageIcon(GameLabel.class.getResource("/5.png"));
    static ImageIcon icon6 = new ImageIcon(GameLabel.class.getResource("/6.png"));

    // for icon moving
    static int curPlayerIndex = 0;      // current player's index [0, 1, 2, 3]
    static JButton prevLocButton = null;      // current player's previous location

    // keep track of rolling doubles
    static boolean doubles = false; // check if a player rolled doubles
    static int numDoubles = 0;  // keep track of consecutively doubles

    // Variable used for Action class
    static Action action;
    static Player actionPlayer;
    static int actionPlayerIndex;

    //to keep track of the property changes
    static PropertyExchange propChange;

    // For win/lose
    static WinLose win = new WinLose();
    static int lostIndex = 0;
    static boolean currentLost = false;
    static boolean skipTurn = false;
    static boolean someoneLost = false;
    
    //to initialize the GameListener
    public GameListener( JButton rollButton, JButton tradeButton, JButton auctionButton, 
            JButton teleportButton, JButton turnButton ){ 
        this.rollButton = rollButton;
        this.tradeButton = tradeButton;
        this.auctionButton = auctionButton;
        this.teleportButton = teleportButton;
        this.turnButton = turnButton;
    }

    @Override
    public void actionPerformed( ActionEvent e ) {

        //specify cases of the clicking sauces of three, rolling, 
        //trading and auctioning, frame is used for showing the information
        JFrame frame = new JFrame( "Trading Frame" );
        
        p = GameLabel.p;
        curPlayerIndex = currentPlayerIndex;
        
        // if this player's turn has been taken away
        if (player[curPlayerIndex].noTurn){
            JOptionPane.showMessageDialog(p, player[curPlayerIndex].getName() + 
                    ", your turn has been taken away!");
            player[curPlayerIndex].noTurn = false;  // reset
            
            totalRand = 3;      // move to next player
            return;
        }

        //case for the movement calculations
        if(e.getSource() == rollButton){
            rollAneMove();
            PropertyExchange.updateHasRolled();
        
        //the trading situation
        } else if(e.getSource() == tradeButton){
            buyAndPay(frame);
            totalRand = 3;          // to move to next player

        
        // teleport
        } else if (e.getSource() == teleportButton){
            teleport();

            
        } else if (e.getSource() == turnButton){
            turnExchange();

        // the auction situation
        } else {
            auctionAndTrade(frame);
        }
        
        someoneLost = false;
        
        
        while(player[curPlayerIndex].getMoney() <= 0 && player[curPlayerIndex].getPropNum() > 0) {
            // Keep force this player to sale property until either ends with positive money or LOST
            auctionAndTrade(frame);
        } 
        
        if(win.ifLose(curPlayerIndex) == true) {
            someoneLost = true;
            player = win.removeLostPlayer(curPlayerIndex);
            setLostPlayerIndex(curPlayerIndex);      
            GameLabel.updateStatus();
            
        }        
        
        
        if(WinLose.shouldGameEnd() == true) {
            JOptionPane.showMessageDialog(null, WinLose.newPlayer[0].getName() + ", You Won!!!");
            
            rollButton.setEnabled(false);
            tradeButton.setEnabled(false);
            auctionButton.setEnabled(false);      
        }
        
    }

    /**
     * create for test purpose
     * @param _index
     */
   public static void removeLostProperty(int _index) {
       /* keeps track of what properties are owned by which players
         * when equal to 4, no one owns this property
         * if ownedPropertyList[5] = 2, then player 2 owns property[5] */
       for(int x = 0; x < 37; x++) {
           if(GameLabel.ownedPropertyList[x] == _index) {
               GameLabel.ownedPropertyList[x] = 4;
           }
       }
       System.out.println("REMOVE PROPERTY ....");
   }
   
   
    /**
    * this method will be called when a user is running out of money, he would be forced to sell one of his property to other players, the other players
    * can offer a price, but the final trading decision can only be made by the bankrupting player
    * @param frame
    */
    static void auctionAndTrade(JFrame frame ){
        PropertyExchange.auctionAndTrade(frame, curPlayerIndex);
        
        // Check if any player owns all four bus square, if yes, then game should end
                if(BusSystem.endGameBus()) {
                    BusSystem.displayWinner();
                }
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
        
        // the property owner gets the money
        int propertyOnwerIndex = ownedPropertyList[propertyIndex];

        player[propertyOnwerIndex].receiveMoney(buyingPrice);
        
        
        GameLabel.updateStatus();
    }
    
    static boolean rollingDoubles(){
            return doubles;
    }

    static int countDoubles(){
            return numDoubles;
    }

    
 public static int fixCurrentIndex(int _curPlayerIndex)  {  // Turn with lost player
        
        int thisIndex = 0;
        if(lostIndex == 0) {        // 1st player in array lost
            thisIndex = 0;
        } else if(lostIndex == playerNum) { //last player in array lost
            thisIndex = 0;
        } else {
            thisIndex = _curPlayerIndex;
            System.out.println("next player index stay the same");
        }       
        currentLost = false; // Rest for next lost player
        
        return thisIndex;
    }
    
    
    /** 
     * This method records lost player's index
     * @param _index: integer represents lost player's index
     */
    public static void setLostPlayerIndex(int _index) {
        lostIndex = _index;
        currentLost = true;
        System.out.println("lost player index:" + _index);
    }
    
    /**
    * this method is called when the roll button is clicked, it controlled the movement of the icons
    *
    */
    static void rollAneMove(){
        p = GameLabel.p;
        // get who is playing right now
        if(currentLost == true) {
            System.out.println("This lost player index: " + curPlayerIndex);
            curPlayerIndex = fixCurrentIndex(curPlayerIndex);   
            System.out.println("AFTER UPDATING, INDEX : " + curPlayerIndex);
            skipTurn = true;
            OaklandOligarchy.setPlayerNum(playerNum);
            OaklandOligarchy.setCurrentPlayerIndex(curPlayerIndex);         
            OaklandOligarchy.setNewPlayer(player, playerNum);
            System.out.println("next person to play: " + player[curPlayerIndex].getName());
        }
//      } 
//      else {
//          curPlayerIndex = OaklandOligarchy.getCurrentPlayerIndex();
//          System.out.println("Nomral: " + curPlayerIndex);
//      }
        
        
        // roll die & get calculation
        int random1 = (new Random()).nextInt(MAX_DICE_ROLL) + ONE;
        int random2 = (new Random()).nextInt(MAX_DICE_ROLL) + ONE;

        if (random1 == random2){
            doubles = true;
            numDoubles++;
        } else {
            doubles = false;
            numDoubles = 0;
        }

        switch(random1){
            case 1:     GameLabel.diceImg1.setIcon(icon1);     break;
            case 2:     GameLabel.diceImg1.setIcon(icon2);     break;
            case 3:     GameLabel.diceImg1.setIcon(icon3);     break;
            case 4:     GameLabel.diceImg1.setIcon(icon4);     break;
            case 5:     GameLabel.diceImg1.setIcon(icon5);     break;
            case 6:     GameLabel.diceImg1.setIcon(icon6);     break;
        }
        switch(random2){
            case 1:     GameLabel.diceImg2.setIcon(icon1);     break;
            case 2:     GameLabel.diceImg2.setIcon(icon2);     break;
            case 3:     GameLabel.diceImg2.setIcon(icon3);     break;
            case 4:     GameLabel.diceImg2.setIcon(icon4);     break;
            case 5:     GameLabel.diceImg2.setIcon(icon5);     break;
            case 6:     GameLabel.diceImg2.setIcon(icon6);     break;
        }

        totalRand = random1 + random2;
        stepsMoved = totalRand;
        
        // get new location
        newLocation = player[curPlayerIndex].getPos() + totalRand;
        int curLocation = extremeCaseLoc(newLocation);

        // move the tokens
        GameToken.movement(curLocation, curPlayerIndex);

        /*** when a player lands on a square ***/
        // currentPlayer owns this property
        if (ownedPropertyList[curLocation] == curPlayerIndex){
            JOptionPane.showMessageDialog(p, player[curPlayerIndex].getName() + ", You already own this property!");

        // player lands on an ACTION
        } else if (property[curLocation].getAction()) {
            //GameToken.movement(curLocation, curPlayerIndex); // Move to action square first
            actionPlayer = player[ curPlayerIndex ];
            actionPlayerIndex = curPlayerIndex;
            action = new Action();
            action.actionSlot();    // Decide what condition to apply based on slot
          
        // player lands on GO TO JAIL
        } else if (property[curLocation].getToJail()){

        // player lands on JAIL
        } else if (property[curLocation].getJail()){
            
        // player lands on START
        } else if (property[curLocation].getStart()){

        // player lands on FREE PARKING
        } else if (property[curLocation].getPark()){

        // no one owns this property
        } else if (ownedPropertyList[curLocation] == FOUR){
            
            // Only give buy option when player have enough money
            if(player[curPlayerIndex].getMoney() > property[curLocation].getPrice()) 
            {
                int result = JOptionPane.showConfirmDialog(p, player[curPlayerIndex].getName() +
                                ", Would you like to purchase this property?",
                                null, JOptionPane.YES_NO_OPTION);
                // the player is buying this property
                if(result == JOptionPane.YES_OPTION){
                    buyThisProperty(curPlayerIndex, curLocation);

                // the player is not buying this property
                } else{
                    JOptionPane.showMessageDialog(p, "You decided not to buy this property." );
                }
            } else {
                JOptionPane.showConfirmDialog(p, player[curPlayerIndex].getName() + 
                        ", You do not have enough money to purchase this property!", null, JOptionPane.YES_OPTION);
            }
            
        // some other player owns this property -> pay rent
        } else {
            if(action.getCardInfo(curPlayerIndex,"noPayCard").equals("Yes"))
            {
                action.sayNo();  // If player has noPayCard, no need to pay rent
            } else {
                
                if(action.getCardInfo(curPlayerIndex, action.doublePayCard).equals("Yes")) {// Pay twice
                    // If player has doublePayCard,need to pay double of the rent
                    JOptionPane.showMessageDialog(p, "Sorry:\n\n" + player[curPlayerIndex].getName() + ", Its time to pay the double rent!");
                            
                    payDouble(curPlayerIndex, curLocation);
                    
                    
                } else {
                    // If player doesn't have doublePayCard nor noPayCard, pay regular rent
                    JOptionPane.showMessageDialog(p, player[ownedPropertyList[curLocation]].getName()+ " already owns this property");

                    int result2 = JOptionPane.showConfirmDialog(p, player[curPlayerIndex].getName() + "\nYou must pay rent!", null, JOptionPane.YES_NO_OPTION);
                    if ( result2 == JOptionPane.YES_OPTION ){      
                            int buyingPrice = property[curLocation].getPrice();
                
                            
                            // Player landed on other player owned bus square
                            if(property[curLocation].isBus()) {
                                System.out.println(" BUS BUS BUS");
                                int busOwnerIndex = BusSystem.checkOnwer(curLocation);
               
                                BusSystem.busPay(busOwnerIndex, buyingPrice, curPlayerIndex);       
                                
                            } else {  
                                
                                // regular square no bus
                                payForThisProperty(curPlayerIndex, curLocation, buyingPrice);
                            }
                            

                    } else {            // the player doesn't want to pay -> trade
                            JOptionPane.showMessageDialog(p, "You don't really have a choice... YOU MUST PAY");      // for now! no trading
                    }
                }
            }
        }
        
        //trade only be able to happen once per roll
        /*
        hasRolled = true;
        propChange.updateHasRolled();
        if (GameListener.rollingDoubles()){
            JOptionPane.showMessageDialog(p, "You've rolled doubles Player "+ player[currentPlayerIndex].getName() + "\nRoll Again!");
            if (GameListener.countDoubles() < 3){
                    System.out.println(player[currentPlayerIndex].getName() + " Rolled a double!");
            } else{
                    // go to JAIL
            }
        }
        else{}
        */
        
        // Check if any player owns all four bus square, if yes, then game should end
        if(BusSystem.endGameBus()) {
            BusSystem.displayWinner();
        }
    }

    /**
    * this method will be called when clicking the trade button, it is the trade happen between the current player and another player that the current player choose
    * the current player will buy certain property from the assigned player and this function can only be called once per roll
    * @ param: frame
    */

    static void buyAndPay( JFrame frame ){
        PropertyExchange.buyAndPay(frame, curPlayerIndex, hasRolled);
        
        // Check if any player owns all four bus square, if yes, then game should end
        if(BusSystem.endGameBus()) {
            BusSystem.displayWinner();
        }
    }

    /**
     * This method gets rid of the extreme location cases
     * @return appropriate current location
     */
    static int extremeCaseLoc(int loc){
        if(loc >= THIRTY_SEVEN){
            loc -= THIRTY_SEVEN;
            if(loc == ZERO)
                loc = ONE;
        }
        return loc;
    }

    /**
     * This method facilitates purchasing properties
     * @param playerIndex : the player who wants to purchase
     * @param propertyIndex : the property the player would like to buy
     */
    @SuppressWarnings("unchecked")
    static void buyThisProperty(int playerIndex, int propertyIndex){
        if (player[playerIndex].getMoney() >= property[propertyIndex].getPrice()){
            // add property to the player's owned property list
            player[playerIndex].addProp( property[propertyIndex]);  

            // update the ownedPropertyList
            ownedPropertyList[propertyIndex] = playerIndex;
            
            // player pays
            player[playerIndex].payMoney(property[propertyIndex].getPrice());
            GameLabel.updateStatus();
            player[playerIndex].addProp(property[propertyIndex]);
            JOptionPane.showMessageDialog(p, "You purchased this property!");
            
            // increase numBus if player landed on bus square and decide to buy it
            if(property[propertyIndex].isBus()) {
                System.out.println("I GOT ONE BUS????????????????");
                player[playerIndex].setNumBus();
                System.out.println("$$$$$$$$$$$$$$$ NOW I HAVE: " + player[playerIndex].getNumBus());
            }
            
        } else {
            JOptionPane.showMessageDialog(p, "You do not have enough money to purchase this property!");
        }
    }

    /**
     * This method facilitates paying double rent for an owned property
     * @param playerIndex : the player to pay the rent
     * @param propertyIndex : the property the player has to pay the rent for
     */
    static void payDouble(int playerIndex, int propertyIndex){
        
        // Player landed on other player owned bus square with doublePay card
        if(property[propertyIndex].isBus()) {
            System.out.println(" BUS BUS BUS");
            int busOwnerIndex = BusSystem.checkOnwer(propertyIndex);
            BusSystem.busPay(busOwnerIndex, property[propertyIndex].getPrice() * DOUBLE, playerIndex);          
            
        } else {            
            // regular square no bus, player pays
            
            player[playerIndex].payMoney(property[propertyIndex].getPrice() * DOUBLE);
            // the property owner gets the money
            int propertyOnwerIndex = ownedPropertyList[propertyIndex];
            player[propertyOnwerIndex].receiveMoney(property[propertyIndex].getPrice() * DOUBLE);
            
            player[playerIndex].setDoublePayCard(false);
            GameLabel.updateStatus();
        }       
    }
    
    /**
     * This method returns the totalRand
     * @return sum of the two die
     */
    static int getTotalRand() {
        return totalRand;
    }

    /**
     * Get loaction that after player moved
     * @return newLocation: integer represent current location player moved to
     */
    public static int getCurrentLocation(){
        return newLocation;
    }
    
    /**
     * Get player entered the action
     * @return actionPlayer: player that entered action phase
     */
    public static Player getActionPlayer() {
        return actionPlayer;
    }

    /**
     * Get action player index
     * @return actionPlayerIndex: integer represent index of player in action
     */
    public static int getActionPlayerIndex() {
        return actionPlayerIndex;
    }
    
    /**
     * Get steps moved forward
     * @return stepsMoved: integer represent how much square current
     * player moved to get to next location
     */
    public static int getStepsMoved() {
        return stepsMoved;
    }
    
    /**
     * This method lets the current player to teleport to a different square
     */
    public static void teleport() {
        
        String stringArray[] = { "Pay $100 and move to a random square", "No Way" };
        int n= JOptionPane.showConfirmDialog(p, player[curPlayerIndex].getName() + 
                ", Would you like to pay $100 and teleportto a random square?", 
                "Teleport", JOptionPane.YES_NO_OPTION);
        
        // cancelled by user
        if (n == JOptionPane.NO_OPTION)
            return;
        
        // choose to teleport
        if (n == 0) {
            // pay $100
            player[curPlayerIndex].payMoney(100);
            GameLabel.updateStatus();
            
            // move to a random square
            int r = (new Random()).nextInt(36) + 1;
            GameToken.movement(r, curPlayerIndex);
        }
               
        totalRand = 3;      // move to nex player;
    }
    
    /**
     * This method lets the current player to take away another player's turn
     * in exchange for losing his/her turn
     */
    public static void turnExchange() {
        // array of players, not including the current player
        Object[] selection = new Object[playerNum - 1];
        int j = 0;
        for (int i = 0; i < playerNum; i++){
            if (i != curPlayerIndex){
                selection[j] = player[i].getName();
                j++;
            }
        }
        
        // aks for which player's turn should be taken away
        String s = (String) JOptionPane.showInputDialog(p, player[curPlayerIndex].getName() +
                ", Whose turn would you like to take away in exchange for losing your turn?", 
                "Turn Exchange", JOptionPane.QUESTION_MESSAGE, null, 
                selection, selection[0]);
        
        // cancelled by user
        if (s.equals(null))
            return;
        
        int noTurnPlayer = 0;
        for (int i = 0; i < playerNum; i ++){
            if (s.equals(player[i].getName())){
                noTurnPlayer = i;
                break;
            }
        }
        player[noTurnPlayer].noTurn = true;
        
        totalRand = 3;      // move to nex player;
    }
}
