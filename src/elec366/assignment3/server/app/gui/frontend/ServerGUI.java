package elec366.assignment3.server.app.gui.frontend;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ServerGUI implements IServerGUI {

	private final JFrame frame; 
	private final JLabel connectionLabel; 
	
	public ServerGUI() {
		//To create the server window 
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Chatting Server");
		frame.getContentPane().setLayout(null);
		
		
		// To set the label red
		connectionLabel = new JLabel("Text Color: Red"); 
		connectionLabel.setForeground(Color.red);
		connectionLabel.setBounds(100, 50, 300, 30); 
		connectionLabel.setFont(new Font("Times", Font.BOLD, 14));
		connectionLabel.setHorizontalAlignment(SwingConstants.LEFT);
		connectionLabel.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(connectionLabel);
		
		
		this.setOnlinePlayers(new String[0]); //To set the number of players to zero
		
	}

	@Override
	public void showUI() {
		this.frame.setVisible(true);
	}

	@Override
	public void closeUI() {
		this.frame.dispose();
	}

	@Override
	public void setOnlinePlayers(String[] players) {
		if(players.length == 0) {
			// Set as red "No Clients Connected"
			this.connectionLabel.setForeground(Color.red);
			this.connectionLabel.setText("No Clients Connected");
		}
		else {
			// set as blue "N Clients Connected"
			this.connectionLabel.setForeground(Color.blue);
			this.connectionLabel.setText(players.length + " Clients Connected");
		}
	}

}
