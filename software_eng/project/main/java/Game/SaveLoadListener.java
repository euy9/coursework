package Game;

import static Game.GameListener.p;
import static Game.OaklandOligarchy.player;
import static Game.OaklandOligarchy.playerNum;
import static Game.OaklandOligarchy.currentPlayerIndex;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.util.*;
import java.io.*;

public class SaveLoadListener implements ActionListener {
    
    JButton saveBtn;
    JButton loadBtn;
	EncryptDecryptData cipher;
   
    /**
     * This method is a constructor for this class
     * @param saveBtn : this is the "Save" Button
     * @param loadBtn : this is the "Load" Button
     */
    public SaveLoadListener(JButton saveBtn, JButton loadBtn) {
        this.saveBtn = saveBtn;
        this.loadBtn = loadBtn;
        this.cipher = new EncryptDecryptData();
    }

	
    @Override
    public void actionPerformed(ActionEvent e)/*throws IOException,
                                                      ClassNotFoundException*/ {
        // set up for save button
		byte[] encodedData = null;
        if (e.getSource() == saveBtn) {
			//need to initialize a encryptdecrypt class, use it to get the encoded data and the decoded data, then do the save and load files5
			
            SaveGame data = new SaveGame(OaklandOligarchy.hour.get(), 
                    OaklandOligarchy.min.get(), OaklandOligarchy.sec.get(),
                    GameLabel.ownedPropertyList, player, playerNum, currentPlayerIndex);
					
			
			
            try{		//save the encoded byte[]
				encodedData = cipher.returnEncoded(data); 
                ResourceManager.save(encodedData, "save(" + OaklandOligarchy.saveNum + ").sav");
                JOptionPane.showMessageDialog(p, "Game was saved: save(" + OaklandOligarchy.saveNum + ").sav");
                OaklandOligarchy.saveNum++;
            } catch (Exception ex){
                JOptionPane.showMessageDialog(p, "Game could not be saved: " + ex.getMessage());
            }
            
        // set up for load button
        } else {
            // get name of the file to load 
            File file = null;
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int returnVal = jfc.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION){
                file = jfc.getSelectedFile();
            } else {
                JOptionPane.showMessageDialog(p, "File access cancelled by user");
            }

            try{
				byte[] decodedData = (byte[])ResourceManager.load(file.getName());
				//String theData = cipher.decode(decodedData);
				
				
                SaveGame data = (SaveGame) cipher.returnDecoded(decodedData); // convert back to saveGame from byte[]
				
               
                OaklandOligarchy.hour.set(data.hour);
                OaklandOligarchy.min.set(data.min);
                OaklandOligarchy.sec.set(data.sec);
                OaklandOligarchy.player = data.players.clone();
                GameLabel.ownedPropertyList = data.propList.clone();
                OaklandOligarchy.playerNum = data.playerNum;
                OaklandOligarchy.currentPlayerIndex = data.currentPlayerIndex;
                
                // update status for every player
                OaklandOligarchy.nextTurn();
                GameLabel.updateAllStatus();
                GameToken.resetIcon();
                
                JOptionPane.showMessageDialog(p, "Selected file is loaded");
                
            } catch (Exception ex){
                JOptionPane.showMessageDialog(p, "Selected file could not be loaded: " + ex.getMessage());
            }
            
            // re-set icon
//            GameToken.resetIcon();
        }
    }
}
