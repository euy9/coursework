package Game;

import javax.swing.JOptionPane;

import static Game.OaklandOligarchy.player;
import static Game.OaklandOligarchy.playerNum;
import static Game.GameLabel.ownedPropertyList;


public class WinLose {
	static Player[] newPlayer;
	static boolean lost;
	static int[] loserCollection = new int[4];
	static int winnerIndex;


	// Default constructor
	public WinLose() {
            newPlayer = new Player[playerNum];
	}


	/**
	 * This method will check if the give player is lost without money without any property
	 * @param _playerIndex: integer represent player we want to check win, lose status
	 * @return lost: boolean variable, return true if the given player is lost, else return false
	 */
	public boolean ifLose(int _playerIndex) {
            if(OaklandOligarchy.player[_playerIndex].getMoney() <= 0 && OaklandOligarchy.player[_playerIndex].getPropNum() <= 0) {
                lost = true;
                player[_playerIndex].setInGame(false);
                JOptionPane.showMessageDialog(null, OaklandOligarchy.player[_playerIndex].getName() + ", Sorry, You Lost");
            }	else {
                lost = false;
                player[_playerIndex].setInGame(true);
            }
            return lost;
	}
        
    /**
     * This method remove lost player from the player array and re-adjusting player index
     * and ownedProperty list as well.    
     * @param _playerIndex: index of player who is lost
     * @return: new player array without the lost player
     */
    public Player[] removeLostPlayer(int _playerIndex) {
//        GameToken.iconArray = GameToken.fixIconIndex(_playerIndex);
        int loc = player[_playerIndex].getPos();
		GameToken.checkOverLap(loc);
		if(!GameToken.checkOverlap){
			Button.btn[loc].setIcon(null);// this part need to be fixed
			System.out.println("remove without overlapping");
		}
		else{
			GameToken.removeLostPlayerOverlap(_playerIndex);
			GameToken.checkOverlap = false;//reset the check overlap back to false	
			System.out.println("remove with overlapping");
		}
		
		
        GameLabel.p.revalidate();
        GameLabel.p.repaint();
        
        newPlayer = new Player[playerNum-1];

        for(int x = 0; x < playerNum; x++) { 
            if(x != _playerIndex) {
                newPlayer[x] = player[x];
            } else {
                while(x < playerNum -1) {
                    newPlayer[x] = player[x+1];
                    // move ownedPropertyList
                    for(int i = 1; i < 37; i++) {
                        if (ownedPropertyList[i] == x+1)
                            ownedPropertyList[i] = x;
                    }
                    x++;
                }
                break;
            }
        }							
        playerNum--;
        OaklandOligarchy.setNewPlayer(newPlayer, playerNum);


        // reset the status bar
        GameLabel.text[playerNum].setVisible(false);
        GameLabel.playerProperty[playerNum].setVisible(false);
        GameLabel.combo[playerNum].setVisible(false);
        GameLabel.up.remove(GameLabel.text[playerNum]);
        GameLabel.up.remove(GameLabel.playerProperty[playerNum]);
        GameLabel.up.remove(GameLabel.combo[playerNum]);
        GameLabel.up.revalidate();
        GameLabel.up.repaint();

        for(int x = 0; x < playerNum; x++) {
            System.out.println("player in game" + x + " : " + newPlayer[x].getName());    
        }
        return newPlayer;	
    }
	
    /**
     * This method sets new player
     * @return: newPlayer player with only inGame player
     */
    public static Player[] setNewPlayer() {
        return newPlayer;
    }


    /**
     * Check if the game should end or not
     * @return end: true if game should end, false if the game should keep going on 
     */
    public static boolean shouldGameEnd () {
        boolean end = false;
        if(playerNum == 1) {
                end = true;
        }
        return end;
    }

    /**
     * This method give winner's index
     * @return winnerIndex: interger represent winner
     */
    public static int winner() {
        if(playerNum == 1) {
            for(int x = 0; x < 4; x++) {
                if(loserCollection[x] == 0) {
                    winnerIndex = x;
                    break;
                }			
            }
        }
        return winnerIndex;
    }
}