package Game;
import java.awt.TextArea;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import static Game.OaklandOligarchy.player;
import static Game.OaklandOligarchy.playerNum;

public class ChatClient 
	implements ActionListener, KeyListener {
	
	
	// Field
	private TextArea message_area 	= null;
	private TextField send_area 	= null;
	private String user_name 		= null;
	private JList list				= null;
	private String[] name			= null;
	JFrame j = new JFrame("OaklandOligarchy Chat");
	JButton send;
	JButton clear;

	
	
	// Constructor
	public ChatClient(String s) {
		
		name = new String[4];
		for(int x = 0; x < playerNum; x++) {
			name[x] = player[x].getName();
		}
		
		//j.addWindowListener(this);
		j.setSize(500, 700);
		j.setResizable(true);
		j.setLayout(new BorderLayout());
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		message_area = new TextArea();
		message_area.setEditable(false);
		j.add(message_area, "Center");
		message_area.setFont(new Font("Arial", Font.PLAIN, 16));
		
		Panel p = new Panel();
		p.setLayout(new FlowLayout());
		
		send_area = new TextField(30);
		send_area.addKeyListener(this);
		send_area.setFont(new Font("Arial", Font.PLAIN, 16));
		
		list = new JList(name);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		
		p.add(list);
		p.add(send_area);
		p.setBackground(new Color(221, 221, 221));
		send = new JButton("Send");
		send.addActionListener(this);
		p.add(send);
		clear = new JButton("Clear");
		clear.addActionListener(this);
		p.add(clear);
				
		j.add(p, "South");
		j.setVisible(true);
		send_area.requestFocus();
		j.pack();	
	}

	/** 
	 * This action listener will takes in action event e and distinguish if 
	 * user clicked "Clear" or "Send" button.
	 * If "Clear" is clicked, erase all the text in the text field
	 * If "Send" is clicked, append the text to the chat separate by new line.
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == send) {
			if(send_area != null) {
				String selectName = list.getSelectedValue().toString();
				message_area.append(selectName  + " : " + send_area.getText() + "\n");
			} 
		} if(e.getSource() == clear) {
			send_area.setText(" ");
		}
		
	}

	public void keyTyped(KeyEvent e) {		
	}
	public void keyReleased(KeyEvent e) {	
	}
	public void keyPressed(KeyEvent e) {
	}
}
