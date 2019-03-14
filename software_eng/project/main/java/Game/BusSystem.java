package Game;
import static Game.OaklandOligarchy.player;
import static Game.OaklandOligarchy.playerNum;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import static Game.Button.property;

public class BusSystem {
	
	// Field
	static int playerIndex;
	static boolean endGameBus = false;
	static final int BUS_ONE = 9;
	static final int BUS_TWO = 18;
	static final int BUS_THREE = 26;
	static final int BUS_FOUR = 36;
	static final int TWO = 2;
	static final int FOUR = 4;
	static final int EIGHT = 8;
	
	// Default constructor
	public BusSystem() {	
	}
	
	/** 
	 * Given a propertyIndex, this method can check owner of the property
	 * @param _propertyIndex
	 * @return: playerIndex represents player who knows the property 
	 */
	public static int checkOnwer(int _propertyIndex) {
		playerIndex = GameListener.ownedPropertyList[_propertyIndex];
		return playerIndex;
	}
	
	/**
	 * This method will determine how to pay the property owner as they owns different 
	 * number of bus square.
	 * The "rent" for these squares will double each time a new one is owned.
	 * @param busOwnerIndex: integer represents owner of the bus square
	 * @param buyingPrice: integer represents price of the bus square
	 * @param playerIndex: integer represents index of the player who landed on bus square
	 */
	public static void busPay(int busOwnerIndex, int buyingPrice, int playerIndex) {

		// When this property owners owns more than one bus square
        if(player[busOwnerIndex].getNumBus()  > 1) {
        	int bus = player[busOwnerIndex].getNumBus();
        	if(bus == 2) {
        		player[busOwnerIndex].receiveMoney(buyingPrice * TWO);
        		player[playerIndex].payMoney(buyingPrice * TWO);
        	} else if(bus == 3) {
        		player[busOwnerIndex].receiveMoney(buyingPrice * FOUR);
        		player[playerIndex].payMoney(buyingPrice * FOUR);
        	} else if(bus == 4) {
        		player[busOwnerIndex].receiveMoney(buyingPrice * EIGHT);
        		player[playerIndex].payMoney(buyingPrice * EIGHT);
        	}
      
        } else {	
        	// When this property owners has less or equal to one bus square, property owner receive regular rent
        	player[busOwnerIndex].receiveMoney(buyingPrice);
        	player[playerIndex].payMoney(buyingPrice);
        	
        }
        
        GameLabel.updateStatus();
	}
	/**
	 * This method will check if any player already owns all 
	 * four bus squares with property index: 9, 18, 26, 36
	 * @return endGameBus: boolean variable, return true if the game should end
	 * else, return false, since no player owns all four bus squares
	 */
	public static boolean endGameBus() {
		// If all the bus square owned by one player, we got a winner
		// When equal to four, no one owns the property
		if(GameListener.ownedPropertyList[BUS_ONE] != FOUR) {
			if(GameListener.ownedPropertyList[BUS_ONE] == GameListener.ownedPropertyList[BUS_TWO] 
					&& GameListener.ownedPropertyList[BUS_TWO] == GameListener.ownedPropertyList[BUS_THREE]
							&& GameListener.ownedPropertyList[BUS_THREE] == GameListener.ownedPropertyList[BUS_FOUR]) {
				endGameBus = true;
			}
		}	
		return endGameBus;
	}
	
	/**
	 * This method displays winner information and disable all the button
	 */
	public static void displayWinner() {
		JOptionPane.showMessageDialog(null, player[GameListener.ownedPropertyList[BUS_ONE]].getName() + ", you got all the bus in Oakland, you Won!!!");	
        GameListener.rollButton.setEnabled(false);
        GameListener.tradeButton.setEnabled(false);
        GameListener.auctionButton.setEnabled(false); 
	}
}
