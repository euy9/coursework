package Game;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Objects;
import javax.swing.*;

/**
 * This class sets up each of the game board box with Buttons
 */
public class Button extends JPanel
{
    static final int ONE = 1;
    static final int EIGHT = 8;
    static final int ZERO = 0;
    static final int EIGHTY = 80;
    static final int EIGHTY_FIVE = 85;
    static final int PROPERTY_NUMBER = 37;
    public static JPanel top = new JPanel(new GridLayout(ONE, EIGHT, ZERO, ZERO));
    public static JPanel bottom = new JPanel(new GridLayout(ONE, EIGHT, ZERO, ZERO));
    public static JPanel right = new JPanel(new GridLayout(EIGHT, ONE, ZERO, ZERO));
    public static JPanel left  = new JPanel(new GridLayout(EIGHT, ONE, ZERO, ZERO));
    public static JPanel center = new JPanel();
    public static JPanel topCenter = new JPanel();
    public static JPanel centerCenter = new JPanel();
    public static JPanel bottomCenter = new JPanel();
    static Property[] property = new Property[PROPERTY_NUMBER];  // all properties
    public static JButton[] btn = new JButton[37];                      // all boxes

    /**
     * This method is a default constructor
     */
    public Button() {
    }


    public static JButton getBTN(int index){
        return btn[index];
    }

    /**
     * This addButton method add 36 JButtons to the game board (btn[1] to btn[36])
     * Buttons are placed on top, right, bottom left panels.
     * @param p: this method takes in JPanel object and add JButtons to the
     * panel it passed in.
     */
    public static void addButton(JPanel p)
    {
        //Set up game board image
//        ImageIcon jail = new ImageIcon(Button.class.getResource("/jail.png"));
//        ImageIcon start = new ImageIcon(Button.class.getResource("/start.png"));
//        ImageIcon action = new ImageIcon(Button.class.getResource("/action.png"));
//        ImageIcon inJail = new ImageIcon(Button.class.getResource("/InJail.png"));
        ImageIcon monopoly = new ImageIcon(Button.class.getResource("/Monopoly.png"));
        ImageIcon welcome = new ImageIcon(Button.class.getResource("/Welcome.png"));

        //change the colors on the gameboard
        top.setBackground(Color.RED);
        right.setBackground(Color.BLUE);
        left.setBackground(Color.GREEN);
        bottom.setBackground(Color.YELLOW);

        top.setBorder(null);
        right.setBorder(null);
        left.setBorder(null);
        center.setBorder(null);

        p.setLayout(new BorderLayout());
        p.add(top, BorderLayout.NORTH);
        p.add(right, BorderLayout.EAST);
        p.add(left, BorderLayout.WEST);
        p.add(bottom, BorderLayout.SOUTH);
        p.add(center, BorderLayout.CENTER);
//        p.add(new Token());

        setUpProperty();        // set up each property

        // Top & Right
        for(int i=1 ; i<19 ; i++){          // count up
            btn[i] = new JButton();

            // if non-property box, just put name
            if(property[i].getAction() || property[i].getJail() || property[i].getToJail() || property[i].getStart()){
                btn[i].setText(property[i].name);

            // if property box, put name & price
            } else {
                btn[i].setText("<html>" + property[i].name + "<br /> $" + property[i].getPrice() + "</html>");
            }

            // buttons change setting
            btn[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            btn[i].setPreferredSize(new Dimension(EIGHTY, EIGHTY));

            btn[i].setMargin(new Insets(ZERO, ZERO, ZERO, ZERO));
            btn[i].setHorizontalAlignment(SwingConstants.CENTER);
            btn[i].setOpaque(false);
            btn[i].setContentAreaFilled(false);


            if (1 <= i && i < 11){  // for top panel
                top.add(btn[i]);
            } else {             // for right panel
                right.add(btn[i]);
            }
        }

        // Bottom & Left
        for(int i=36 ; i>18 ; i--){         // count down
            btn[i] = new JButton();

            // if non-property box, just put name
            if(property[i].getAction() || property[i].getJail() || property[i].getToJail() || property[i].getStart()){
                btn[i].setText(property[i].name);

            // if property box, put name & price
            } else {
                btn[i].setText("<html>" + property[i].name + "<br /> $" + property[i].getPrice() + "</html>");
            }

            // change settings for buttons
            btn[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            btn[i].setPreferredSize(new Dimension(EIGHTY, EIGHTY));
            btn[i].setMargin(new Insets(ZERO, ZERO, ZERO, ZERO));
            btn[i].setOpaque(false);
            btn[i].setContentAreaFilled(false);

            if (19 <= i && i < 29) {        // for bottom panel
                bottom.add(btn[i]);
            } else {                        // for right panel
                left.add(btn[i]);
            }
        }

        // set all panels visible
        top.setVisible(true);
        right.setVisible(true);
        bottom.setVisible(true);
        left.setVisible(true);


        //CENTER
        center.setBackground(Color.WHITE);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        topCenter.setBackground(Color.WHITE);
        centerCenter.setBackground(Color.WHITE);
        bottomCenter.setBackground(Color.WHITE);
        JLabel first = new JLabel();
        first.setIcon(monopoly);

        JLabel third = new JLabel();
        third.setIcon(welcome);

        topCenter.add(first);
        bottomCenter.add(third);

        center.add(topCenter);
        center.add(centerCenter);
        center.add(bottomCenter);
        p.add(center);
        p.setVisible(true);
    }
    
    /**
    * this method will iterate through all the properties and find the rigtht property by its name
    */
    public static int getPropertyIndexByName( String propertyN ){
        int index = 1;                                                                                  //default   
        for(int i = 1; i < property.length; i ++){
            if(property[i].getName().equals( propertyN )){
                index = i;
            }
        }
        return index;
    }
    
    
    /**
     *  This method initializes the main board boxes.
     *  There are 36 boxes: property[1] to property[36]
     */
    public static void setUpProperty(){
        // Top panel
        property[1] = new Property("-> START");
        property[1].setStart();
        property[2] = new Property("Five Guys");
        property[2].setPrice(100);
        property[2].setRent(4);
        property[3] = new Property("Oakland Bakery & Market");
        property[3].setPrice(100);
        property[3].setRent(4);
        property[4] = new Property("Thackeray Hall");
        property[4].setPrice(110);
        property[4].setRent(6);
        property[5] = new Property("Fuel and Fuddle");
        property[5].setPrice(110);
        property[5].setRent(6);
        property[6] = new Property("Starbucks");
        property[6].setPrice(120);
        property[6].setRent(8);
        property[7] = new Property("ACTION");
        property[7].setAction();
        property[8] = new Property("Dunkin Donuts");
        property[8].setPrice(140);
        property[8].setRent(10);
        property[9] = new Property("61A");
        property[9].setPrice(100);
        property[9].setBus();
        property[9].setRent(100);
        //property[9] = new Property("Crazy Mocha");
        //property[9].setPrice(140);
        //property[9].setRent(10);
        property[10] = new Property("Hillman Library");
        property[10].setJail();

        // Right Panel
        property[11] = new Property("Cathedral of Learning");
        property[11].setPrice(160);
        property[11].setRent(12);
        property[12] = new Property("Petersen Events Center");
        property[12].setPrice(180);
        property[12].setRent(14);
        property[13] = new Property("Soldiers and Sailors");
        property[13].setPrice(180);
        property[13].setRent(14);
        property[14] = new Property("ACTION");
        property[14].setAction();
        property[15] = new Property("Benedum Hall");
        property[15].setPrice(200);
        property[15].setRent(16);
        property[16] = new Property("Sennott Square");
        property[16].setPrice(220);
        property[16].setRent(18);
        property[17] = new Property("ACTION");
        property[17].setAction();
        property[18] = new Property("61B");
        property[18].setPrice(100);
        property[18].setBus();
        property[18].setRent(100);
        
        /*
        property[18] = new Property("David Lawrence Hall");
        property[18].setPrice(220);
        property[18].setRent(18);
		*/
        // Bottom Panel
        property[19] = new Property("FREE PARKING");
        property[19].setPark();
        property[20] = new Property("Litchfield Towers");
        property[20].setPrice(240);
        property[20].setRent(20);
        property[21] = new Property("Nordenberg Hall");
        property[21].setPrice(260);
        property[21].setRent(22);
        property[22] = new Property("ACTION");
        property[22].setAction();
        property[23] = new Property("Sutherland Hall");
        property[23].setPrice(260);
        property[23].setRent(24);
        property[24] = new Property("Forbes Ave.");
        property[24].setPrice(300);
        property[24].setRent(26);
        property[25] = new Property("Fifth Ave.");
        property[25].setPrice(300);
        property[25].setRent(26);
        property[26] = new Property("61C");
        property[26].setPrice(100);
        property[26].setBus();
        property[26].setRent(100);
        
        /*
        property[26] = new Property("Bigelow Blvd.");
        property[26].setPrice(320);
        property[26].setRent(28);
        */
        property[27] = new Property("Heinz Chapel");
        property[27].setPrice(350);
        property[27].setRent(35);
        property[28] = new Property("<html> --> <br /> HILLMAN LIBRARY </html>");
        property[28].setToJail();

        // Left Panel
        property[29] = new Property("UPMC Presbyterian");
        property[29].setPrice(350);
        property[29].setRent(36);
        property[30] = new Property("Magee Women's Hospital");
        property[30].setPrice(380);
        property[30].setRent(38);
        property[31] = new Property("Pittsburgh VA Hospital");
        property[31].setPrice(400);
        property[31].setRent(40);
        property[32] = new Property("ACTION");
        property[32].setAction();
        property[33] = new Property("Rite Aid");
        property[33].setPrice(400);
        property[33].setRent(42);
        property[34] = new Property("The University Store on Fifth");
        property[34].setPrice(440);
        property[34].setRent(44);
        property[35] = new Property("The Pitt Shop");
        property[35].setPrice(440);
        property[35].setRent(48);
        property[36] = new Property("61D");
        property[36].setPrice(100);
        property[36].setBus();
        property[36].setRent(100);
        /*
        property[36] = new Property("Barco Law Building");
        property[36].setPrice(500);
        property[36].setRent(50);
        */

    }

}
