package elec366.assignment3.server.app.gui.frontend;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ServerGUI implements IServerGUI {

	public ServerGUI() {
		JFrame frame = new JFrame();
		frame.setBounds(100, 100, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Chatting Server");
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		//Not connected
		//I was thinking we can add an if else statement? 
		//I just wasn't super clear on how to set it up...
		JLabel labelNotConnected = new JLabel("Text Color: Red"); 
		labelNotConnected.setForeground(Color.red);
		labelNotConnected.setBounds(100, 50, 300, 30); 
		labelNotConnected.setFont(new Font("Times", Font.BOLD, 14));
		labelNotConnected.setHorizontalAlignment(SwingConstants.LEFT);
		labelNotConnected.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(labelNotConnected);
		labelNotConnected.setText("Connection Status: Not Connected"); 
		
		//Need to set up a counter of clients to work with the connection box
		
		JLabel labelConnected = new JLabel("Text Color: Blue"); 
		labelConnected.setForeground(Color.blue);
		labelConnected.setBounds(150, 50, 300, 30); 
		labelConnected.setFont(new Font("Times", Font.BOLD, 14));
		labelConnected.setHorizontalAlignment(SwingConstants.LEFT);
		labelConnected.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(labelConnected);
		labelConnected.setText("Number Clients Connected");
	}

	@Override
	public void showUI() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setState(State state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnlinePlayers(String[] players) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void appendLog(String appendedLine) {
		// TODO Auto-generated method stub
		
	}

}
