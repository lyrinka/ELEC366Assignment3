package elec366.assignment1.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import elec366.assignment1.client.richtext.RichText;

// It is recommended to have IClientGUI interface showing side by side when viewing this document.
public class ExampleClient implements IClientGUI {

	private final JFrame frame; 
	
	// The application will first call this constructor. 
	public ExampleClient() {
		
		// User interface initialized here
		
		// Creation of all UI objects
		this.frame = new JFrame(); 
		// ... 

		// Initialize UI state to default (i.e. disconnected)
		this.setState(IClientGUI.DEFAULT_STATE);
		
		// Do not show frame yet! 
		// The application will register for all callbacks, and then invoke the showUI() method. 
		
	}
	
	// The application will then register for all callbacks. 
	@Override
	public void onConnectionButton(Runnable callback) {
		
		// Example:
		JButton button = new JButton(); // This should be done in the constructor
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent eventIgnored) {
				callback.run();
			}
			
		});
		
		// It is also possible to write it like this (equivalent but looks fancy):
		button.addActionListener(event -> callback.run());
		
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendButton(Runnable callback) {
		// TODO Auto-generated method stub
		
	}
	
	// Finally, the application shows the window to the user. 
	@Override
	public void showUI() {
		this.frame.setVisible(true);
	}

	// During normal operations: the application can configure UI between different display states
	// Note: when an UI element is set invisible, ideally we should clear its content as well, for instance, by object.setText(""); 
	// The input enum will never be null. 
	@Override
	public void setState(State state) {
		switch(state) {
			default:
			case DISCONNECTED: {
				// Disconnected: sending-related UI hidden, button showing "Connect"
				
				break; 
			}
			case CONNECTING: {
				// Connecting: same as disconnected, but the button shows "Connecting..." and grays out
				
				break; 
			}
			case CONNECTED: {
				// Connected: username textfield grays out, sending-related UI shown. Button showing "Disconnect"
				
				break; 
			}
		}
	}
	
	// During normal operations: the application can show a dialog to the user. 
	// The input strings will never be null. 
	@Override
	public void displayDialog(String dialogTitle, String displayedMessage) {
		JOptionPane.showMessageDialog(null, displayedMessage, dialogTitle, JOptionPane.INFORMATION_MESSAGE);
	}

	// During normal operations: the application can set the title of the application. 
	// The input string will never be null. 
	@Override
	public void setApplicationTitle(String applicationTitle) {
		this.frame.setTitle(applicationTitle);
	}

	// During normal operations: the application can clear everything displayed in the chat box
	@Override
	public void clearChat() {
		// TODO Auto-generated method stub
		
	}

	// During normal operations: the application can append a new line to the chat box
	// The input rich text object will never be null. 
	@Override
	public void appendChat(RichText appendedLine) {
		// Please see the RichText class for more details of rich text objects. 
		// For initial testing, simply use appendedLine.toString() to obtain a plain text message. 
		String stringWithNoFormat = appendedLine.toString(); 
		// TODO Auto-generated method stub
		
	}

	// During normal operations: the application can set the combo box to reflect the list of players online
	// The input array will never be null. It may be empty (length = 0). 
	@Override
	public void setOnlinePlayers(String[] onlinePlayerNames) {
		// TODO Auto-generated method stub
		
	}

	// When the connect button is pressed: the application will use this method to obtain the user name configured by the user. 
	// It is safe to return a null here. 
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	// When the send button is pressed: the application will use this method to obtain the indicated recipient. 
	// It is safe to return a null here. If this method returns an empty string or null, the application will issue a global chat. 
	// If this method returns a name and the player with that name is not online, the application will tell the user that the player is offline. 
	@Override
	public String getRecepient() {
		// TODO Auto-generated method stub
		return null;
	}

	// When the send button is pressed: the application will use this method to obtain the content, either a chat or a command. 
	// It is safe to return a null here. 
	@Override
	public String getChat() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// IMPORTANT! When testing your application, instantiate your client object in the main function, 
	// and you can test the client by invoking various methods. 
	// Your main function can be anywhere
	public static void main(String[] args) {
		System.out.println("Test");
		IClientGUI client = new ExampleClient(); // Replace this 
//		client.onConnectionButton(() -> System.out.println("Connect button pressed! Username: " + client.getUsername()));
//		client.onSendButton(() -> System.out.println("Send button pressed! Recepient: " + client.getRecepient() + ", message: " + client.getChat()));
		client.showUI();
		// testing code etc
	}

}
