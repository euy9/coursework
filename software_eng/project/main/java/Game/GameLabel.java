package Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;

import static Game.OaklandOligarchy.player;
import static Game.OaklandOligarchy.playerNum;
import static Game.OaklandOligarchy.currentPlayerIndex;

/**
 * This class is mainly used to set up the left side of the frame.
 * The labels in the status bar are also set up.
 */
public class GameLabel extends JPanel {
    static JPanel p;
    static GridBagConstraints gbc;
    static JPanel panel;

    // dice labels
    static JLabel diceImg1;
    static JLabel diceImg2;

    // for status bar
    public static Box up;
    public static JTextArea text[];     // to show each player's name, money
    public static JLabel[] playerProperty;      // "property:" label
    public static JComboBox[] combo;     // to list each player's properties

    /* keeps track of what properties are owned by which players
     * when equal to 4, no one owns this property
     * if ownedPropertyList[5] = 2, then player 2 owns property[5] */
    static int[] ownedPropertyList = new int[37];

    //Constants
    static final int ZERO = 0;
    static final int ONE = 1;
    static final int TWO = 2;
    static final int FOUR = 4;
    static final int FIVE = 5;
    static final int SIX = 6;
    static final int TEN = 10;
    static final int FIFTEEN = 15;
    static final int TWENTY_TWO = 22;
    static final int THIRTY = 30;
    static final int FOURTY = 40;
    static final int SEVENTY = 70;
    static final int THIRTY_SEVEN = 37;
    static final int ONE_HUNDRED_FIFTY = 200;
    static final int SEVEN_HUNDRED = 700;
    static final int SEVEN_HUNDRED_FIFTY = 750;
    static JPanel playerStatusPanel;
    /**
     * Constructor of the Label class
     * @param _gbc  : GridBagConstraints
     * @param _panel : panel we will be using
     */
    public GameLabel(GridBagConstraints _gbc, JPanel _panel)
    {
        gbc = _gbc;
        panel = _panel;
        text = new JTextArea[playerNum];
        playerProperty = new JLabel[playerNum];
        combo = new JComboBox[playerNum];
    }

    /**
     * This addLabel method will divide entire frame into two panels, and depends on the panel,
     * it will apply different Layout to different panel, This method also calls the addButton method
     * in order to add JButtons to the game board
     * @param i: takes an integer and depends on the integer it pass it, this method will decide what action
     * to take accordingly.
     */
    public static void addComponentForTwoMainPanel(int i, JPanel panel)
    {
        JButton rollButton = new JButton(" Roll ");
        JButton tradeButton = new JButton(" Trade ");
        JButton auctionButton = new JButton(" Auction ");
        JButton teleportButton = new JButton(" Teleport ");
        JButton turnButton = new JButton(" Turn Exchange ");
        p = new JPanel();
        switch(i) {
            case 1:
                p.setBorder(null);
                p.setPreferredSize(new Dimension(SEVEN_HUNDRED, SEVEN_HUNDRED_FIFTY));
                Button.addButton(p);  // Way to call a static method of other class
                p.setBackground(Color.WHITE);
                p.setVisible(true);
                break;

            case 2:
                p.setBackground(Color.WHITE);
                p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

                // Adding labels into the panel
                JPanel userStatus = new JPanel();
                userStatus.setBackground(Color.WHITE);
                
                JLabel state = new JLabel("Player Information: ");
                state.setFont(new Font("Serif", Font.ITALIC + Font.BOLD, THIRTY));
                state.setForeground(Color.blue);
                state.setBorder(BorderFactory.createEmptyBorder(FOURTY, TEN, FOURTY, TEN));
                
                JPanel topStatus = new JPanel();
                topStatus.setBackground(Color.WHITE);
                topStatus.add(ClockTimer.clockLabel);
                topStatus.add(Box.createHorizontalStrut(30));   
                
                JButton saveBtn = new JButton("Save");
                JButton loadBtn = new JButton("Load");
                SaveLoadListener svl = new SaveLoadListener(saveBtn, loadBtn);
                saveBtn.addActionListener(svl);
                loadBtn.addActionListener(svl);
                
                topStatus.add(saveBtn);
                topStatus.add(loadBtn);
                
                userStatus.add(topStatus);
                userStatus.add(state);
                p.add(userStatus);

                //if(GameListener.someoneLost == true) {
                    
                for(int playerCount = ONE; playerCount <= playerNum; playerCount++)
                {
                    playerStatusPanel = new JPanel();
                    addLabelForPlayerInfo(playerCount, playerStatusPanel); // Call addLabelForPlayerInfo method
                    p.add(playerStatusPanel);
                } 
                System.out.println("In Label, might reupdate??");
                //}
                
                p.setPreferredSize(new Dimension(ONE_HUNDRED_FIFTY, SEVEN_HUNDRED_FIFTY));
                p.setMaximumSize(new Dimension(ONE_HUNDRED_FIFTY, SEVEN_HUNDRED_FIFTY));
                p.setMaximumSize(new Dimension(ONE_HUNDRED_FIFTY, SEVEN_HUNDRED_FIFTY));


                // panel for "roll" button
                JPanel functionalButtonPanel = new JPanel();
                functionalButtonPanel.setBackground(Color.CYAN);
                functionalButtonPanel.add(Box.createHorizontalStrut(FIVE));
                functionalButtonPanel.add(tradeButton);
                functionalButtonPanel.add(rollButton);
                functionalButtonPanel.add(auctionButton);
                functionalButtonPanel.add(teleportButton);
                functionalButtonPanel.add(turnButton);

                for (int j = ZERO; j < 37; j++){
                    ownedPropertyList[j] = FOUR;
                }

                // actionlistener for random number 2 - 12
                GameListener actionTrigger = new GameListener(rollButton, tradeButton, 
                        auctionButton, teleportButton, turnButton);
                rollButton.addActionListener(actionTrigger);
                tradeButton.addActionListener(actionTrigger);
                auctionButton.addActionListener(actionTrigger);
                teleportButton.addActionListener(actionTrigger);
                turnButton.addActionListener(actionTrigger);
                
                // change die picture
                diceImg1 = new JLabel();
                diceImg2 = new JLabel();
                Button.centerCenter.setLayout(new BoxLayout(Button.centerCenter, BoxLayout.X_AXIS));
                Button.centerCenter.add(diceImg1);
                Button.centerCenter.add(diceImg2);

                p.add(functionalButtonPanel);
                break;
        }
       
        panel.add(p, gbc);
       
    }


//  public static void addPropertyToCombo( Property property ){ //to update the combo box in the player
//            combo[ playerIndex ].addItem( property.getName() );
//  }


    /**
     * This method divide entire status panel into couple different frame with box layout,
     * then add welcome message JLabel and player information JLabel and an JTextArea accordingly
     * @param i: Integer i used to represent player
     * @param jj: JPanel component used to add JLabel and JText accordingly
     */
     public static void addLabelForPlayerInfo(int i, JPanel jj) {


        jj.setLayout(new BoxLayout(jj, BoxLayout.Y_AXIS));
        jj.setBackground(Color.WHITE);


        up = Box.createVerticalBox();     // This box contains JTextArea for player info
        up.setBorder(BorderFactory.createEmptyBorder(ZERO, FIFTEEN, FIFTEEN, FIFTEEN));
        playerProperty[i-1] = new JLabel("Property : ");
        playerProperty[i-1].setBorder(BorderFactory.createEmptyBorder(ZERO, ZERO, ZERO, ZERO));
        playerProperty[i-1].setBackground(Color.WHITE);
        playerProperty[i-1].setFont(new Font("Serif", Font.BOLD, FIFTEEN));
        playerProperty[i-1].setAlignmentX(ZERO);
        ///////////////////////// or to change
        ////////////////////// Add additional text to indicate special action card

        text[i-1] = new JTextArea(player[i-1].getName() + "\n"
                    + "Money: $" + player[i-1].getMoney()
                    + "\n" + "NoPay Card: " + Action.getCardInfo(i-1, "noPayCard")
                    + "\n" + "DoublePay Card: " + Action.getCardInfo(i-1, "doublePayCard"));

        text[i-1].setLineWrap(false);
        text[i-1].setFont(new Font("Serif", Font.BOLD, FIFTEEN));

        combo[i-1] = new JComboBox();
        combo[i-1].setPreferredSize(new Dimension(30, 30));
        combo[i-1].setAlignmentY(TEN);

        up.add(text[i-1]);
        up.add(playerProperty[i-1]);
        up.add(combo[i-1]);
        jj.add(up);
        
    }
     

    /** 
     * This method updates player status panel as player bought, lose, or sell property 
     * and it also updates when someone lost, it will remove the lost player from the panel entirely 
     */
    public static void updateStatus() {

            int x = 0;
            for(x = 0; x < playerNum; x++) {
                text[x].setText("\n" + player[x].getName() +
                        "\n" + "Money: $" + player[x].getMoney()
                        + "\n" + "NoPay Card: " + Action.getCardInfo(x, Action.noPayCard)
                        + "\n" + "DoublePay Card: " + Action.getCardInfo(x, Action.doublePayCard));
                combo[x].setModel(new DefaultComboBoxModel( player[x].getProp() ));
            }
    }
    
    
    public static void updateAllStatus() {
        for (int i = 0; i < text.length; i++){
            text[i].setVisible(false);
            playerProperty[i].setVisible(false);
            combo[i].setVisible(false);
            up.remove(GameLabel.text[i]);
            up.remove(GameLabel.playerProperty[i]);
            up.remove(GameLabel.combo[i]);
            up.revalidate();
            up.repaint();
        }
        up.removeAll();
        
        text = new JTextArea[playerNum];
        playerProperty = new JLabel[playerNum];
        combo = new JComboBox[playerNum];
        
        for(int i = 1; i <= playerNum; i++) {
            text[i-1] = new JTextArea(player[i-1].getName() + "\n"
                        + "Money: $" + player[i-1].getMoney()
                        + "\n" + "NoPay Card: " + Action.getCardInfo(i-1, "noPayCard")
                        + "\n" + "DoublePay Card: " + Action.getCardInfo(i-1, "doublePayCard"));

            text[i-1].setLineWrap(false);
            text[i-1].setFont(new Font("Serif", Font.BOLD, FIFTEEN));

            playerProperty[i-1] = new JLabel("Property : ");
            playerProperty[i-1].setBorder(BorderFactory.createEmptyBorder(ZERO, ZERO, ZERO, ZERO));
            playerProperty[i-1].setBackground(Color.WHITE);
            playerProperty[i-1].setFont(new Font("Serif", Font.BOLD, FIFTEEN));
            playerProperty[i-1].setAlignmentX(ZERO);
            
            combo[i-1] = new JComboBox();
            combo[i-1].setPreferredSize(new Dimension(30, 30));
            combo[i-1].setAlignmentY(TEN);
            combo[i-1].setModel(new DefaultComboBoxModel( player[i-1].getProp() ));

            up.add(text[i-1]);
            up.add(playerProperty[i-1]);
            up.add(combo[i-1]);
        }
    }

}
