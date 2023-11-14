package elec366.assignment3.client.app.gui.frontend;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.StyledDocument;

import elec366.assignment3.richtext.RichText;
import elec366.assignment3.util.SingleLineSanitizer;

/*
 * This class applies to only client.
 * 
 * This is the concrete implementation of client-side GUI. 
 */
public class ClientGUI implements IClientGUI {

	private final JFrame frame; 
	private final JLabel labelAddressName; 
	private final JTextField textfieldAddressName;
	private final JLabel labelClientName; 
	private final JTextField textfieldClientName; 
	private final JButton buttonconnect; 
	private final JTextPane textAreaDisplay; 
	private final JScrollPane textAreaScroller;
	private final JLabel labelSend; 
	private final JComboBox<String> names; 
	private final JTextArea textAreaSend; 
	private final JButton buttonSend;
	
	private final StyleManager styleManager; 
	
	private Runnable onConnectionButtonCallback;
	private Runnable onSendButtonCallback;
	
	public ClientGUI() {
		
		//To create the window for the chatting client application
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Chatting Client");
		frame.getContentPane().setLayout(null);
		
		//To create the label for the server address
		labelAddressName = new JLabel("Server Address: ");
		labelAddressName.setBounds(10, 20, 200, 20);
		labelAddressName.setFont(new Font("Times", Font.BOLD, 12));
		labelAddressName.setHorizontalAlignment(SwingConstants.LEFT);
		labelAddressName.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(labelAddressName);
		
		//Text field to enter the server address
		textfieldAddressName = new JTextField();
		textfieldAddressName.setFont(new Font("Times", Font.BOLD, 12));
		textfieldAddressName.setBounds(115, 20, 180, 20); // x axis, y axis, width, height
		frame.getContentPane().add(textfieldAddressName);
		
		//Label for client name
		labelClientName = new JLabel("Client Name: "); 
		labelClientName.setBounds(10, 50, 100, 30);
		labelClientName.setFont(new Font("Times", Font.BOLD, 12));
		labelClientName.setHorizontalAlignment(SwingConstants.LEFT);
		labelClientName.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(labelClientName);
		
		
		//Text field for client's name 
		textfieldClientName = new JTextField();
		textfieldClientName.setFont(new Font("Times", Font.BOLD, 12));
		textfieldClientName.setBounds(90, 55, 125, 20); // x axis, y axis, width, height
		frame.getContentPane().add(textfieldClientName);
		
		
		// Button to connect client and server
		buttonconnect = new JButton("Connect");
		buttonconnect.setBounds(230, 55, 170, 20);// x axis, y axis, width, height 
		buttonconnect.addActionListener(ignored -> {
			if(this.onConnectionButtonCallback == null) return; //No callback is registered so no callback is invoked
			this.onConnectionButtonCallback.run(); //invoked callback if applicable
		});
		frame.getContentPane().add(buttonconnect);
		
		
		
		//For rich text improvement the source is: https://stackoverflow.com/questions/6068398/how-to-add-text-different-color-on-jtextpane
		textAreaDisplay = new JTextPane();
		textAreaDisplay.setEditable(false); //To ensure the display isn't editable
		
		// Used this source to add the scrolling feature: http://www.java2s.com/Code/Java/Swing-JFC/ViewingRTFformat.htm 
		textAreaScroller = new JScrollPane(textAreaDisplay); 
		textAreaScroller.setBounds(10, 90, 400, 300);
		textAreaScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // The source used for this line: https://stackoverflow.com/questions/10177183/add-scroll-into-text-area
		frame.getContentPane().add(textAreaScroller); 
		
		//Label to select who the message should be sent to
		labelSend = new JLabel("Send to: "); 
		labelSend.setBounds(10, 390, 300, 30);
		labelSend.setFont(new Font("Times", Font.BOLD, 12));
		labelSend.setHorizontalAlignment(SwingConstants.LEFT);
		labelSend.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(labelSend);
		
		
		names = new JComboBox<String>(); // Source for the combo box feature: https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html
		names.setBounds(100, 390, 300, 30);
		names.setFont(new Font("Times", Font.BOLD, 12));
		frame.getContentPane().add(names);
		
		
		
		textAreaSend = new JTextArea();
		textAreaSend.setBounds(10, 460, 300, 80);
		frame.getContentPane().add(textAreaSend);
		textAreaSend.setLineWrap(true); //Source: https://stackoverflow.com/questions/8858584/how-to-wrap-text-in-a-jtextarea
		
		//To create the keylistener so that when enter is pressed it can send a message
		textAreaSend.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (keyCode==10) { //Int 10 is the code for the enter button
					e.consume();
					if(onSendButtonCallback != null) //if enter is not pushed nothing happens
						onSendButtonCallback.run(); // if button is pressed backend is invoked
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
		}); 
		
		//Creating the send button
		buttonSend = new JButton("Send");
		buttonSend.setBounds(350, 490, 100, 20);// x axis, y axis, width, height
		buttonSend.addActionListener(ignored -> {
			if(this.onSendButtonCallback == null) return;  //if send button is not pressed nothing happens
			this.onSendButtonCallback.run(); // if button is pressed backend is invoked
		});
		frame.getContentPane().add(buttonSend);
		
		this.styleManager = new StyleManager(); //Class created to manage rich text that appears on the GUI
		
		this.setState(IClientGUI.DEFAULT_STATE); //To enable the client name and server address fields while hiding the other buttons/actions
		
	}

	@Override
	public void onConnectionButton(Runnable callback) {
		this.onConnectionButtonCallback = callback; 
	}

	@Override
	public void onSendButton(Runnable callback) {
		this.onSendButtonCallback = callback; 
	}
	
	@Override
	public void removeCallbacks() {
		this.onConnectionButtonCallback = null; 
		this.onSendButtonCallback = null; 
	}

	@Override
	public void showUI() {
		frame.setVisible(true);
	}
	
	@Override
	public void closeUI() {
		frame.dispose();
	}
//To alternate the user states
	@Override
	public void setState(State state) {
		if(state == null) return; 
		boolean showSendUI; 
		switch(state) {
			default:
				//To enable the connect button, client name and server address text fields while disabling the other labels,buttons and text fields
			case DISCONNECTED: {
				this.buttonconnect.setText("Connect");
				this.buttonconnect.setEnabled(true);
				this.textfieldClientName.setEnabled(true);
				this.textfieldAddressName.setEnabled(true);
				showSendUI = false; 
				this.setOnlinePlayers(new String[0]);
				this.textAreaSend.setText("");
				break; 
			}
			//While the client and server is connecting this state appears the only enabled button states connecting while the rest isn't enabled
			case CONNECTING: {
				this.buttonconnect.setText("Connecting..");
				this.buttonconnect.setEnabled(false);
				this.textfieldClientName.setEnabled(false);
				this.textfieldAddressName.setEnabled(false);
				showSendUI = false; 
				break; 
			}
			//The connect button now says disconnect and it is enabled, the client name and server address text fields are disabled since they have already been populated.
			//The send button, send label, the text area to send messages and the names of the players are visible
			case CONNECTED: {
				this.buttonconnect.setText("Disconnect");
				this.buttonconnect.setEnabled(true);
				this.textfieldClientName.setEnabled(false);
				this.textfieldAddressName.setEnabled(false);
				showSendUI = true; 
				break;
			}
		}
		this.labelSend.setVisible(showSendUI);
		this.buttonSend.setVisible(showSendUI);
		this.textAreaSend.setVisible(showSendUI);
		this.names.setVisible(showSendUI);
	}

	//Displays a dialog, if username or server address doesn't match desired format a dialog is produced
	@Override
	public void displayDialog(String dialogTitle, String displayedMessage) {
		if(displayedMessage == null || displayedMessage.isEmpty()) return; 
		if(dialogTitle == null || dialogTitle.isEmpty()) dialogTitle = "Message"; 
		JOptionPane.showMessageDialog(null, displayedMessage, dialogTitle, JOptionPane.INFORMATION_MESSAGE);	
	}

	//To set the title of the application
	@Override
	public void setApplicationTitle(String applicationTitle) {
		if(applicationTitle == null || applicationTitle.isEmpty()) return; 
		this.frame.setTitle(applicationTitle);
	}

	//To set the server address
	@Override
	public void setServerAddress(String address) {
		if(address == null) return; 
		this.textfieldAddressName.setText(address);
	}
	
	//Cleared each time the button is connected in a new instance 
	@Override
	public void clearChat() {
		this.textAreaDisplay.setText("");
	}

	//Adds the chat message that was sent to a new line in the text area
	@Override
	public void appendChat(String appendedLine) {
		if(appendedLine == null) return; 
		StyledDocument doc = this.textAreaDisplay.getStyledDocument(); 
		this.styleManager.addText(doc, appendedLine);
		this.styleManager.addNewLine(doc);
	}
	
	//Adds the rich text in the text area in a new line
	public void appendChat(RichText appendedLine) {
		StyledDocument doc = this.textAreaDisplay.getStyledDocument(); 
		this.styleManager.addRichText(doc, appendedLine);
		this.styleManager.addNewLine(doc);
	}

	//Array of player names
	@Override
	public void setOnlinePlayers(String[] onlinePlayerNames) {
		// Consulted source:
		// https://stackoverflow.com/questions/4747020/how-to-update-jcombobox-content-from-arraylist
		if(onlinePlayerNames == null) return; 
		if(onlinePlayerNames.length == 0) {
			this.names.removeAllItems(); 
			return; 
		}
		String prev = (String)this.names.getSelectedItem(); 
		this.names.setModel(new DefaultComboBoxModel<String>(onlinePlayerNames));
		int index = 0; 
		if(prev != null) {
			for(int i = 0; i < onlinePlayerNames.length; i++) {
				if(onlinePlayerNames[i].equalsIgnoreCase(prev)) {
					index = i; 
					break; 
				}
			}
		}
		this.names.setSelectedIndex(index);
	}
	
	@Override
	public void setMessageFocus() {
		this.textAreaSend.setText("");
		this.textAreaSend.requestFocus();
	}

	@Override
	public String getServerAddress() {
		return this.textfieldAddressName.getText().trim(); 
	}
	
	@Override
	public String getUsername() {
		return this.textfieldClientName.getText().trim(); 
	}

	@Override
	public String getRecepient() {
		String recepient = (String)this.names.getSelectedItem(); 
		if(recepient == null) return ""; 
		return recepient; 
	}

	@Override
	public String getMessage() {
		return SingleLineSanitizer.sanitize(this.textAreaSend.getText()); 
	}

}
