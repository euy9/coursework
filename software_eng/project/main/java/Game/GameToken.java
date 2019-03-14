package Game;

import java.util.List;
import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import static Game.OaklandOligarchy.player;
import static Game.OaklandOligarchy.playerNum;
import static Game.Button.btn;


public class GameToken extends JPanel {
    
    // constants
    static final int ZERO = 0;
    static final int ONE = 1;
    static final int FOUR = 4;
    static final int THIRTY = 30;
    
    // buffered image
    static BufferedImage[] curOverlappingImage;
    static BufferedImage[] prevOverlappingImage;		//not super sure why we need this
    static int count;       // how many overlapping images
    
    static int[] allPos;            // to keep track of each player's position
    static boolean checkOverlap;    // if overlap exists
    
    public static void movement(int loc, int playerIndex) {
        
        Player curPlayer = player[playerIndex];     // the current player
        curPlayer.setPrevPos(curPlayer.getPos());   // set prev position
        curPlayer.setPos(loc);                      // set current position

        curOverlappingImage = new BufferedImage[playerNum];
        
        checkOverLap(curPlayer.getPrevPos());
        overlap(curPlayer.getPrevPos());
        checkOverlap = false;
        
        checkOverLap(curPlayer.getPos());
        overlap(curPlayer.getPos());
        checkOverlap = false;
        
    }
    
    
    void setUpBufferedImage() {								//just curious, why do we have this method here since we never used it
        curOverlappingImage = new BufferedImage[playerNum];
    }
    
    static void checkOverLap(int loc ) {
        // figure out where each player's position is
        // & which player overlaps with the given location
        count = 0;
        for(int i = 0; i < playerNum; i++ ){
            if (player[i].getPos() == loc){
                curOverlappingImage[count] = player[i].getPlayerIcon();
                count++;
            }
        }
        if (count > 1)
            checkOverlap = true;
    }
    
	// to prevent deleting any none loser players when removing the losing player icon
	static void removeLostPlayerOverlap(int _playerIndex){
		Icon curIcon;
		int loc = player[_playerIndex].getPos();	//to get the assigned player current location
		if (checkOverlap){
            BufferedImage img = player[0].getPlayerIcon();  // to get sample size
            int width = img.getWidth();
            BufferedImage combinedImage = new BufferedImage(width*count-1		//don't need the lost player's image
                    , img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = combinedImage.createGraphics();   //adding the images together
			
			//curOverlappingImage = new BufferedImage[playerNum];
			List<BufferedImage> fileteredOverlappingImeage = new ArrayList<BufferedImage>();
			for(int i = 0; i < count; i++){
				if(curOverlappingImage[i] != player[_playerIndex].getPlayerIcon()){
					fileteredOverlappingImeage.add(curOverlappingImage[i]);
				}
			}
			if(count-1 >= 1){
				g.drawImage(fileteredOverlappingImeage.get(0), ZERO, ZERO, null);
				if(count-1 >= 2){       // more than 2 player overlap
					g.drawImage(fileteredOverlappingImeage.get(1), width, ZERO, null);
					if (count-1 >= 3){
						g.drawImage(fileteredOverlappingImeage.get(2), 2*width, ZERO, null);
					}
				}
			}	
           
            
            g.dispose();
            curIcon = new ImageIcon((Image) combinedImage);
            btn[loc].setIcon(curIcon);            
        }		
	}
	
	
    // reset Icon on btn[loc]
    static void overlap(int loc) {
        Icon curIcon;
        
        // if icons overlap
        if (checkOverlap){
            BufferedImage img = player[0].getPlayerIcon();  // to get sample size
            int width = img.getWidth();
            BufferedImage combinedImage = new BufferedImage(width*count
                    , img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = combinedImage.createGraphics();   //adding the images together

            if(count >= 2){       // more than 2 player overlap
                g.drawImage(curOverlappingImage[0], ZERO, ZERO, null);
                g.drawImage(curOverlappingImage[1], width, ZERO, null);
                
                if (count >= 3){
                    g.drawImage(curOverlappingImage[2], 2*width, ZERO, null);
                    
                    if (count == 4){
                        g.drawImage(curOverlappingImage[3], 3*width, ZERO, null);
                    }
                }
            }
            
            g.dispose();
            curIcon = new ImageIcon((Image) combinedImage);
            btn[loc].setIcon(curIcon);
            
        // if nothing overlaps
        } else {
            if (count == 0){        // there is no icon here
                btn[loc].setIcon(null);
            } else {                // only 1 icon
                curIcon = new ImageIcon((Image)curOverlappingImage[0]);
                btn[loc].setIcon(curIcon);
            }
        }
    }
    
    static void resetIcon() {
        for(int i = 1; i < 37; i++) {
            btn[i].setIcon(null);
        }
        
        for(int i = 0; i < playerNum; i++ ){
            movement(player[i].getPos(), i);
        }
    }
  
}
