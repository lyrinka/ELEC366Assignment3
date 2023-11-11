package elec366.assignment3.client.app.gui.frontend;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ClientGUI implements IClientGUI {

	private final JFrame frame; 
	private final JLabel labelClientName; 
	private final JTextField textfieldClientName; 
	private final JButton buttonconnect; 
	private final JTextArea textAreaDisplay; 
	private final JLabel labelSend; 
	private final JComboBox<String> names; 
	private final JTextArea textAreaSend; 
	private final JButton buttonSend; 
	
	public ClientGUI() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Chatting Client");
		frame.getContentPane().setLayout(null);
		
		labelClientName = new JLabel(""); 
		labelClientName.setBounds(10, 50, 300, 30);
		labelClientName.setFont(new Font("Times", Font.BOLD, 12));
		labelClientName.setHorizontalAlignment(SwingConstants.LEFT);
		labelClientName.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(labelClientName);
		labelClientName.setText("Client Name: ");
		
		textfieldClientName = new JTextField();
		textfieldClientName.setFont(new Font("Times", Font.BOLD, 12));
		textfieldClientName.setBounds(100, 55, 120, 20); // x axis, y axis, width, height
		frame.getContentPane().add(textfieldClientName);
		
		buttonconnect = new JButton("Connect");
		buttonconnect.setBounds(230, 55, 100, 20);// x axis, y axis, width, height
		frame.getContentPane().add(buttonconnect);
		
		textAreaDisplay = new JTextArea();
		textAreaDisplay.setBounds(10, 90, 350, 300);
		frame.getContentPane().add(textAreaDisplay);
		frame.getContentPane().repaint(); 
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
		
		labelSend = new JLabel(""); 
		labelSend.setBounds(10, 390, 300, 30);
		labelSend.setFont(new Font("Times", Font.BOLD, 12));
		labelSend.setHorizontalAlignment(SwingConstants.LEFT);
		labelSend.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(labelSend);
		labelSend.setText("Send to: ");
		
		//Create the array or arrays and convert it to strings so it can be added to the combo box
		
		//an example to see it works
		
		String[] stringNames= {"Jack", "Julie", "Stephanie"};
		
		names = new JComboBox<String>(stringNames); // Source https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html
		names.setBounds(80, 390, 300, 30);
		names.setFont(new Font("Times", Font.BOLD, 12));
		frame.getContentPane().add(names);
		
		textAreaSend = new JTextArea();
		textAreaSend.setBounds(10, 430, 300, 80);
		frame.getContentPane().add(textAreaSend);
		frame.getContentPane().repaint(); 
		textAreaDisplay.setEditable(true);
		
		
		buttonSend = new JButton("Send");
		buttonSend.setBounds(330, 460, 100, 20);// x axis, y axis, width, height
		frame.getContentPane().add(buttonSend);
		buttonSend.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent send) {
				
				// Need to add a list or something of test so it doesnt replace it and adds to the list?
				
				String entered = textAreaSend.getText();
				textAreaDisplay.setText(entered);
			}
		});
	}

	@Override
	public void onConnectionButton(Runnable callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendButton(Runnable callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showUI() {
		frame.setVisible(true);
	}

	@Override
	public void setState(State state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayDialog(String dialogTitle, String displayedMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setApplicationTitle(String applicationTitle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearChat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void appendChat(String appendedLine) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnlinePlayers(String[] onlinePlayerNames) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRecepient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChat() {
		// TODO Auto-generated method stub
		return null;
	}

}
