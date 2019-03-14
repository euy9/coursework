package Game;

import java.io.Serializable;

/**
 * This class creates Property objects used for the game.
 */
public class Property implements Serializable{        
    String name;        // name of the property
    int price;          // price to pay when a player purchases this property
    int rent;           // price to pay when a player lands
    boolean action;     // if it is an action box
    boolean jail;       // if it is a jail box
    boolean toJail;     // if it is go to jail box
    boolean start;      // start box
    boolean park;       // FREE PARKING box
    boolean bus;		// bus square
    
    /**
     * This method is a default constructor that calls the 
     * next constructor with blank name
     */
    public Property(){
        this("");
    }
    
    /**
     * This method sets up property object by 
     * setting the name to the given name;
     * setting price, rent to 0;
     * setting action, jail, toJail, start boolean values to false
     * @param name : the name the property will have
     */
    public Property(String name){
        this.name = name;
        price = 0;
        rent = 0;
        action = false;
        jail = false;
        toJail = false;
        start = false;
        park = false;
        bus = false;
    }
    

    /**
     * This method sets the purchase price of this property to the given value
     * @param price : the purchase price of this property
     */
    public void setPrice(int price){
        this.price = price;
    }
    
    /**
     * This method sets the rent price of this property to the given value
     * @param rent : the rent price of this property
     */
    public void setRent(int rent){
        this.rent = rent;
    }
    
    /**
     * This method marks this property box as an action box.
     */
    public void setAction(){
        this.action = true;
    }
    
    /**
     * This method marks this property as Jail
     */
    public void setJail(){
        this.jail = true;
    }
    
    /**
     * This method marks this property as "go to Jail" box
     */
    public void setToJail(){
        this.toJail = true;
    }
    
    /**
     * This method marks this property as the "start" box
     */
    public void setStart(){
        this.start = true;
    }
     
    /**
     * This method marks this property as the "FREE PARKING" box
     */
    public void setPark(){
        this.park = true;
    }
    
    
    /**
     * This property returns the name of this property
     * @return the name of the property
     */
    public String getName(){
        return name;
    }
    
    /**
     * This property returns the price of this property
     * @return the purchasing price of the property
     */
    public int getPrice(){
        return price;
    }
    
    /**
     * This method returns the rental price of this property
     * @return the rent price of the property
     */
    public int getRent(){
        return rent;
    }
    
    /**
     * This method indicates whether this box is an action box
     * @return true if this is an action box, false it it is not
     */
    public boolean getAction(){
        return action;
    }
    
    /**
     * This method indicates whether this box is a Jail
     * @return true it this is Jail, false if it is not
     */
    public boolean getJail(){
        return jail;
    }
    
    /**
     * This method indicates whether this box is a "go to Jail" box
     * @return true if this is a "go to Jail" box, false if it is not
     */
    public boolean getToJail(){
        return toJail;
    }
    
    /**
     * This method indicates whether this box is a "Start" box
     * @return true if this is a "Start" box, false if it is not
     */
    public boolean getStart(){
        return start;
    }
    
    /**
     * This method indicates whether this box is a "FREE PARKING" box
     * @return true if this is a "FREE PARKING" box, false if it is not.
     */
    public boolean getPark(){
        return park;
    }
    
    /**
     * This method set the square to bus square
     */
    public void setBus() {
    	this.bus = true;
    }
    /**
	 * This method will check if player landed on the Bus square
	 */
	public boolean isBus() {
		return bus;
	}
}
