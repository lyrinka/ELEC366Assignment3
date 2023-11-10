package elec366.assignment2.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class Main {

	int clientCounter; //Use this counter in the connected pop up
	//Need to create a list names or the database to hold names if they exist
	
	public static void main(String[] args) {
		
		 
		//Creating the outline for number of clients connected or not
		
		// to create the outline of the application, as seen in the lab
		/*
		JFrame frame = new JFrame();
		frame.setBounds(100, 100, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Chatting Server");
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);

		//Not connected
		//I was thinking we can add an if else statment? 
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
		*/
		
		//The above should be added to the Server Gui
		
		JFrame frame2 = new JFrame();
		frame2.setBounds(100, 100, 500, 500);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.setTitle("Chatting Client");
		frame2.getContentPane().setLayout(null);
		frame2.setVisible(true);
		
		JLabel labelClientName = new JLabel(""); 
		labelClientName.setBounds(10, 50, 300, 30);
		labelClientName.setFont(new Font("Times", Font.BOLD, 12));
		labelClientName.setHorizontalAlignment(SwingConstants.LEFT);
		labelClientName.setVerticalAlignment(SwingConstants.CENTER);
		frame2.getContentPane().add(labelClientName);
		labelClientName.setText("Client Name: ");
		
		JTextField textfieldClientName = new JTextField();
		textfieldClientName.setFont(new Font("Times", Font.BOLD, 12));
		textfieldClientName.setBounds(100, 55, 120, 20); // x axis, y axis, width, height
		frame2.getContentPane().add(textfieldClientName);
		frame2.repaint(); //Can remove this if we want to set it up differently
		
		String enteredName = textfieldClientName.getText();
		
		
		JButton buttonconnect = new JButton("Connect");
		buttonconnect.setBounds(230, 55, 100, 20);// x axis, y axis, width, height
		frame2.getContentPane().add(buttonconnect);
		
		JTextArea textAreaDisplay = new JTextArea();
		textAreaDisplay.setBounds(10, 90, 350, 300);
		frame2.getContentPane().add(textAreaDisplay);
		frame2.getContentPane().repaint(); 
		textAreaDisplay.setEditable(false); //It is still staying editable....
		
		buttonconnect.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent Connect) {
				//if(connected) {
					//for if connected or not connected
				//}
				
				//Cannot get the name to work properly I need to understand the interface more behind the scenes to get how its being saved or is there a better way to display the name?
				//textAreaDisplay.setText("Connection rejected: The name " + enteredName +" is used by another client");
				
				textAreaDisplay.setText("You are Connected");
			}
		});
		
	
		
		
		
		//need to use the users entered name here
		
		//Set up an if connected function the following appears, If not connected the above appears
		
		JLabel labelSend = new JLabel(""); 
		labelSend.setBounds(10, 390, 300, 30);
		labelSend.setFont(new Font("Times", Font.BOLD, 12));
		labelSend.setHorizontalAlignment(SwingConstants.LEFT);
		labelSend.setVerticalAlignment(SwingConstants.CENTER);
		frame2.getContentPane().add(labelSend);
		labelSend.setText("Send to: ");
		
		//Create the array or arrays and convert it to strings so it can be added to the combo box
		
		//an example to see it works
		
		String[] stringNames= {"Jack", "Julie", "Stephanie"};
		
		JComboBox names = new JComboBox(stringNames); // Source https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html
		names.setBounds(80, 390, 300, 30);
		names.setFont(new Font("Times", Font.BOLD, 12));
		frame2.getContentPane().add(names);
		
	
		
		JTextArea textAreaSend = new JTextArea();
		textAreaSend.setBounds(10, 430, 300, 80);
		frame2.getContentPane().add(textAreaSend);
		frame2.getContentPane().repaint(); 
		textAreaDisplay.setEditable(true);
		
		
		JButton buttonSend = new JButton("Send");
		buttonSend.setBounds(330, 460, 100, 20);// x axis, y axis, width, height
		frame2.getContentPane().add(buttonSend);
		buttonSend.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent send) {
				
				// Need to add a list or something of test so it doesnt replace it and adds to the list?
				
				String entered = textAreaSend.getText();
				textAreaDisplay.setText(entered);
			}
		});
		
	}

}
