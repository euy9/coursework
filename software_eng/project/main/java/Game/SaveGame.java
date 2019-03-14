package Game;

import java.io.Serializable;

public class SaveGame implements Serializable{
    private static final long serialVersionUID = 1L;
    
    int hour;
    int min;
    int sec;
    int[] propList = new int[37];
    Player[] players;
    int playerNum;
    int currentPlayerIndex;
    
    public SaveGame(int hour, int min, int sec, int[] propList, Player[] players, 
            int playerNum, int currentPlayerIndex) {
        this.hour = hour;
        this.min = min;
        this.sec = sec;
        this.propList = propList.clone();
        this.players = players.clone();
        this.playerNum = playerNum;
        this.currentPlayerIndex = currentPlayerIndex;
    }
}
