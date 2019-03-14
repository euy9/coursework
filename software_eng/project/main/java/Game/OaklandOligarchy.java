package Game;

import java.awt.*;
import java.util.Date;
import java.util.Random;
import javax.swing.*;
import java.util.concurrent.atomic.*;
import javax.imageio.ImageIO;

public class OaklandOligarchy implements Runnable {

    public static JPanel panel;
    public static JFrame frame = new JFrame("Oakland Oligarchy");
    public static JOptionPane op;
    public static GridBagConstraints gbc = new GridBagConstraints();

    //Constants
    static final int ZERO = 0;
    static final int ONE = 1;
    static final int TWO = 2;
    static final int THREE = 3;
    static final int FOUR = 4;
    static final int TWENTY = 20;
    static final int FOURTY = 40;
    static final int EIGHTY = 80;
    static final int HUNDRED_TWENTY = 120;
    static final int SEVEN_HUNDRED = 750;
    static final int EIGHT_HUNDRED = 800;
    static final int THOUSAND_TWENTY = 1200;
    static final int THOUSDAND_FIFTY = 1150;
    static final int PROPERTY_NUMBER = 36;
    static final double ZERO_POINT_FIVE = 0.5;

    //Players set up
    public static int playerNum = ONE;
    public static Property[] property;
    public static String name[] = new String[FOUR];
    public static Player[] player;
    public static GameLabel label;

    //used for playGame
    int currPosition[] = new int[4];
    static volatile int currentPlayerIndex;    // which player is currently playing
    static boolean gameEnded = false;

    // clocking
    static AtomicInteger sec = new AtomicInteger(ZERO);
    static AtomicInteger min = new AtomicInteger(ZERO);
    static AtomicInteger hour = new AtomicInteger(ZERO);
    static ClockTimer clockTimer;
    
    // how many times the game has been saved
    static int saveNum = 1;
    
    // test
    static boolean loaded = false;

    
    /**
    * This main method will start the InputDialog to get player info
    * once the information is collected, it will start the main
    * window and display the frame and other component inside the frame.
    */
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        showInputDialog();
        SwingUtilities.invokeLater(new OaklandOligarchy());
        setUpPlayers();
        label = new GameLabel(gbc, panel);
        ChatClient chat = new ChatClient("OaklandOligarchy chat");
        // figure out who goes first randomly
        currentPlayerIndex = (new Random()).nextInt(playerNum);
        
        // clock
        clockTimer = new ClockTimer();
        
        // while there is no winner
        while(!gameEnded){
            play();
        }
    }
    
    
    public static void updatePanel() {
        System.out.println("updating in oakland as well");
        panel = GameLabel.panel;
        SwingUtilities.updateComponentTreeUI(GameLabel.playerStatusPanel);
        frame.revalidate();
        frame.repaint();
    }
    /**
    *   This run method will create frame and set up its appropriate size then
    *   add two panels into the frame, one is status panel for display player status,
    *   and the other panel contains the entire game board. This method will call
    *   the void add() method from below and set up each panel's grid layout.
    */
    /**
     * This method set up frame size and call the addGridPrameter method to apply appropriate size for panels
     * and add panels into the frame
     */
    @Override
    public void run() {
        frame.setSize(THOUSAND_TWENTY, EIGHT_HUNDRED);
        panel = new JPanel(new GridBagLayout());

        setUpPlayers();     // call player set up

        //game board
        addGridPrameter(ONE, FOUR, ZERO, HUNDRED_TWENTY, EIGHTY);     //Call method: addGridPrameter()

        //status bar
        addGridPrameter(TWO, ZERO, ZERO, THREE, FOURTY);     //Call method: addGridPrameter()

        // add components to the frame
        frame.add(panel);
        frame.setPreferredSize(new Dimension(THOUSDAND_FIFTY, SEVEN_HUNDRED));
        frame.setMinimumSize(new Dimension(THOUSDAND_FIFTY, SEVEN_HUNDRED));
        frame.setMaximumSize(new Dimension(THOUSDAND_FIFTY, SEVEN_HUNDRED));
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        
        
    }

     /**
     * Getting user's name with Input Dialog, accepted input: A-Za-z0-9
     */
    private static void showInputDialog() {
        // ask for player number
        String[] choices = {"2", "3", "4"};
        String numb = (String)JOptionPane.showInputDialog(null, "How many players?", "Number of Players",
                  JOptionPane.QUESTION_MESSAGE, null, choices, choices[ZERO]);
        playerNum = Integer.parseInt(numb);

        // ask for player name
        op = new JOptionPane();
        for (int i = ZERO; i < playerNum; i++) {
            boolean named = false;
            while (named == false) {
                int update = i + 1;
                name[i] = (String)op.showInputDialog("Player " + update + ", please enter your name:",
                                                "Player".concat(Integer.toString(update)));
                // closing the window exists the game
                if (name[i] == null) {
                    System.exit(1);

                // when the name is left blank
                } else if (name[i].isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Player name cannot be empty! Please enter your name:");
                    named = false;

                // when the name does not consist of numbers & letters or when the name length exceeds 20 characters.
                } else if (!name[i].matches("[A-Za-z0-9]*") || name[i].length() > TWENTY) {
                    JOptionPane.showMessageDialog(frame, "Player name must consist of no more than 20 letters and numbers");
                    named = false;

                // the name sounds good
                } else {
                    named = true;
                }
            }
        }
    }

    /**
     * This method set up player information for the game and store
     * it in the Player class
     */
    public static void setUpPlayers(){
        player = new Player[playerNum];
        for(int i = ZERO; i < playerNum; i++){
            player[i] = new Player(name[i]);
        }
        
        // set up icon
        for(int i = ZERO; i < playerNum; i++) {
            try{
                player[i].setPlayerIcon(ImageIO.read(Label.class.getResource("/bird" + (i+1) + ".png")));
            } catch (Exception e){}
        }
    }

    /**
     * This method will apply appropriate parameter for Grid layout for the two major
     * panel: Game board panel and user status panel
     * @param i: panel number
     * @param x: Integer number of horizontal cells the panel will take
     * @param y: Integer number of vertical cells the panel will take
     * @param w: Integer number of width the panel will take
     * @param h: Integer number of height the panel will take
     */
    public static void addGridPrameter(int i, int x, int y, int w, int h) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        gbc.weightx = ZERO_POINT_FIVE;
        gbc.weighty = ZERO_POINT_FIVE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(ZERO, ZERO, ZERO, ZERO);
        label.addComponentForTwoMainPanel(i, panel);
    }

    /**
     * This method plays the game & take turns among players
     */
    static void play(){
        eachTurn();
       
        nextTurn();
    }

    /**
     * This method makes sure each player's take turn
     */
    static void eachTurn(){
        // wait for player to roll the die
        while(GameListener.getTotalRand() < 2){
            try {
                Thread.sleep(1000);
            }
            catch(InterruptedException e) {
            }
        }
        
        // reset totalRand
        GameListener.totalRand = 0;
    }
    
  
    /**
     * This method determines which player plays next
     */
    static void nextTurn(){
        
        System.out.println("\n\noakland in turn player index: " + currentPlayerIndex +
                "\nplayerNum: " + playerNum);
        if (currentPlayerIndex == (playerNum - 1)) {
            currentPlayerIndex = 0;
        // next player plays
        } else {                
            currentPlayerIndex++;
            
        }
        
        System.out.println("next person to play from oakland: " + currentPlayerIndex );
    }
    

    /**
     * This method returns the current player's index
     * @return the current player's index
     */
    static int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Get all the player who entered the game
     * @return player: Player array
     */
    public static Player[] getAllPlayer() {
        return player;
    }

    /**
     * Get number of player
     * @return playerNum: integer number of player
     */
    public static int getPlayerNum() {
        return playerNum;
    }
    
    /**
     * Set the new player array with only inGame player
     * @param _player: Player array that represent still inGame player
     */
    public static void setNewPlayer(Player[] _player, int _playerNum) {
        player = _player;
        playerNum = _playerNum;
    }


    /**
     * This method set up correct current player Index as player array is adjusted as some
     * player is losing and removed from the player array
     * @param curPlayerIndex: integer represent current player index
     */
    public static void setCurrentPlayerIndex(int curPlayerIndex) {
        if(GameListener.lostIndex == 0) {
            currentPlayerIndex  = 0;
            System.out.println("GO BACK TO THE BEIGING OF ARRAY ");
        } else {
            currentPlayerIndex = curPlayerIndex;
            System.out.println("stay the same!!!!");
        }
    }
    

    /**
     * This method sets new player array 
     * @param _playerNum: integer represent number of player still in game
     */
    public static void setPlayerNum(int _playerNum) {
        playerNum = _playerNum;
    }
}
