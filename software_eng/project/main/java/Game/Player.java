package Game;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * This class creates Player objects used for the game.
 */
public class Player implements Serializable{
    // constants
//    static final int MAX_PROP = 36;
    private static final long serialVersionUID = 1L;

    String name;                                            // name of the player
    Property[] property = new Property[36];          // owned property
    int numProp;                                     // number of properties
    int money;                                      // money they have
    int position;                                   // where to place the token
    int prevPos;                                // its previous position
    //whether the player is still playing: true if still playing, false if lost
    boolean inGame;
    
    boolean doublePayCard;          // true if with card; false if without card
    boolean noPayCard;              // true if with card; false if without card
    boolean noTurn;              // true if this player cannot player in the next turn

    
    transient BufferedImage playerIcon;                // this player's icon
    
    int numBus = 0;

    /**
     * This method is a constructor that accepts player name;
     * sets up number of property to 0;
     * initial money to 2000;
     * and its position to 1.
     * @param name : name of the player
     */
    public Player(String name){
        this.name = name;
        numProp = 0;
        money = 2000;           // 20 times the cheapest property
        position = 1;           // start position is 1
        prevPos = 0;            //prev position is defaultly null
        inGame = true;
        doublePayCard = false;
        noPayCard = false;
    }

    /*** methods ***/
    /**
     * This method sets the name of the player to the given String
     * @param name : the name the player will have
     */
    public String setName(String name){
        return this.name = name;
    }

    /**
     * This method returns the name of the player
     * @return name of the player
     */
    public String getName() {
        return this.name;
    }

    /**
     * This method returns the amount of money the player has
     * @return money the player has
     */
    public int getMoney(){
        return this.money;
    }

    /**
     * This method subtracts the given amount from the player's money
     * money = money - amt
     * @param amt : the money to subtract
     */
     public int payMoney(int amt){
        return this.money -= amt;
     }

    /**
     * This method adds the given amount to player's money
     * money = money + amt
     * @param amt : the money to add
     */
    public int receiveMoney(int amt){
        return this.money += amt;
    }

    /**
     * This method returns the player's property list as a string array
     * @return the properties the player owns
     */
    public String[] getProp(){
        String[] ret = new String[numProp];
        for (int i = 0; i < numProp; i++){
            ret[i] = property[i].getName();
        }
        return ret;
    }

    /**
     * This method returns the number of properties the player has
     * @return the number of the player's owned properties
     */
    public int getPropNum() {
        int propNum = 0;
        while (property[propNum] != null)
        {
            propNum++;
        }
        return propNum;
    }

    /**
     * This method adds a property to this player's owned property list
     * @param prop : property to add
     * @return return 1 if successful, 0 if the property already exists
     */
    public int addProp(Property prop){
        if (propExist(prop))
            return 0;           // property already exists!
        property[numProp] = prop;
        numProp++;
        return 1;
    }

    /**
     * This method removes a property from the player's owned property list
     * @param prop : property to remove
     * @return return 1 if successfully removed, 0 if the property does not already exist on the list.
     */
    public int removeProperty(Property prop){
        int index = findProp(prop);
        if (index == 40 ){
            return 0;           // the property is not owned by this player
        }
        property[index] = null;
        numProp--;
        
        if (numProp == 1 && property[0] == null){
            property[0] = property[1];
            property[1]= null;
        } else if (numProp > 1){
            if (property[numProp] != null){
                property[index] = property[numProp];
                property[numProp] = null;
            }
        }
        return 1;
    }

    /**
     * This method gets the current(but to move from) button the player is in
     * @return the current button the player is in
     */
    public int getPrevPos(){
        return prevPos;
    }

    /**
     * This method sets the current(but to move from) button the player is in
     * @param loc : the current button we want to set
     */
    public void setPrevPos(int loc){  //setter for the current location
            prevPos = loc;
    }

    /**
     * This method gets the position of the player
     * @return the location of the player
     */
    public int getPos(){
        return position;
    }

    /**
     * This method sets the position of the player
     * @param loc : the location we want to set
     */
    public void setPos(int loc){        //to set the surrent location
        position = loc;
    }
    
    /**
     * This method tells whether or not the player is still playing the game
     * @return true if the player is playing still, false if lost
     */
    public boolean getInGame(){
        return inGame;
    }
    
    /**
     * This method sets whether the player lost the game or not
     * @param winOrLose : true if still playing, false if lost
     */
    public void setInGame(boolean winOrLose){
        this.inGame = winOrLose;
    }
    
    public boolean getDoublePayCard() {
        return this.doublePayCard;
    }
    
    public void setDoublePayCard(boolean hasDoublePayCard) {
        this.doublePayCard = hasDoublePayCard;
    }
    
    public boolean getNoPayCard() {
        return this.noPayCard;
    }
    
    public void setNoPayCard( boolean hasNoPayCard) {
        this.noPayCard = hasNoPayCard;
    }
    
    public BufferedImage getPlayerIcon() {
        return this.playerIcon;
    }
    
    public void setPlayerIcon(BufferedImage icon) {
        this.playerIcon = icon;
    }

    
    
    // helper methods
    /**
     * This method checks whether the player owns this property
     * @param checkThisProp : the property to check
     * @return return true if it exists; return false otherwise.
     */
    public boolean propExist (Property checkThisProp){
        if (numProp == 0){      // the list is empty
            return false;
        }
        for (int i = 0; i < numProp; i++){
            if (property[i].equals(checkThisProp))
                return true;                // return true if equal
        }
        return false;
    }

    /**
     * This method helps to find at which index on the list the property is.
     * Check with propExist method before using this method.
     * @param propToFind : the property to find on the list
     * @return the index of the property on the list if successful, 40 if not.
     */
    public int findProp(Property propToFind){
        for (int i = 0; i < numProp; i ++){
            if (property[i].equals(propToFind))
                return i;
        }
        return 40;
    }

    /**
     * This method takes string name of the property and find its index
     * @param propToFind: String name of property need to find
     * @return i: index of property
     */
    public int findProp(String propToFind)
    {
        int i;
        for (i = 0; i < numProp; i ++)
        {
            if (property[i].equals(propToFind))
                break;
        }
        return i;
    }
    
    
    // for save & load
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ImageIO.write(playerIcon, "png", out); // png is lossless
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        playerIcon = ImageIO.read(in);
    }
    
   // for bus system
    public void setNumBus() {
    	numBus++;
    }
    
    public int getNumBus() {
    	return numBus;
    }
}
