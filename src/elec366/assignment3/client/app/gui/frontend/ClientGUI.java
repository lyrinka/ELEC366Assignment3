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
		
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Chatting Client");
		frame.getContentPane().setLayout(null);
		
		//For some reason it made me add the type here.. did you create the labels somewhere else?
		//need to see if this will fit properly
		labelAddressName = new JLabel("Server Address: ");
		labelAddressName.setBounds(10, 20, 200, 20);
		labelAddressName.setFont(new Font("Times", Font.BOLD, 12));
		labelAddressName.setHorizontalAlignment(SwingConstants.LEFT);
		labelAddressName.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(labelAddressName);
		
		textfieldAddressName = new JTextField();
		textfieldAddressName.setFont(new Font("Times", Font.BOLD, 12));
		textfieldAddressName.setBounds(115, 20, 180, 20); // x axis, y axis, width, height
		frame.getContentPane().add(textfieldAddressName);
		
		labelClientName = new JLabel("Client Name: "); 
		labelClientName.setBounds(10, 50, 100, 30);
		labelClientName.setFont(new Font("Times", Font.BOLD, 12));
		labelClientName.setHorizontalAlignment(SwingConstants.LEFT);
		labelClientName.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(labelClientName);
		
		
		// TODO: this text is a bit clipped
		//Should no longer be clipped but it needs to be tested 
		textfieldClientName = new JTextField();
		textfieldClientName.setFont(new Font("Times", Font.BOLD, 12));
		textfieldClientName.setBounds(90, 55, 125, 20); // x axis, y axis, width, height
		frame.getContentPane().add(textfieldClientName);
		
		
		// TODO: The button is not wide enough to display "Connecting.."
		//It should be wide enough now
		buttonconnect = new JButton("Connect");
		buttonconnect.setBounds(230, 55, 170, 20);// x axis, y axis, width, height 
		buttonconnect.addActionListener(ignored -> {
			if(this.onConnectionButtonCallback == null) return; 
			this.onConnectionButtonCallback.run(); 
		});
		frame.getContentPane().add(buttonconnect);
		
		
		// TODO: wrapping and scrolling ->wrapping and scrolling should be okay now
		// TODO: right click menu -> what I found to support right clicks https://stackoverflow.com/questions/35513767/right-click-focus-in-swing
		// TODO: rich text improvement -> I think we need to add a text pane for this to work -> example from online https://stackoverflow.com/questions/9650992/how-to-change-text-color-in-the-jtextarea
		// Source: https://stackoverflow.com/questions/6068398/how-to-add-text-different-color-on-jtextpane
		textAreaDisplay = new JTextPane();
//		textAreaDisplay.setLineWrap(true); //Source: https://stackoverflow.com/questions/8858584/how-to-wrap-text-in-a-jtextarea
		textAreaDisplay.setEditable(false);
		
		textAreaScroller = new JScrollPane(textAreaDisplay); // Source: http://www.java2s.com/Code/Java/Swing-JFC/ViewingRTFformat.htm this is where I found the below lines as well4
		textAreaScroller.setBounds(10, 90, 400, 300);
		textAreaScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(textAreaScroller); 
		
		//need to use the users entered name here
		//Set up an if connected function the following appears, If not connected the above appears
		labelSend = new JLabel("Send to: "); 
		labelSend.setBounds(10, 390, 300, 30);
		labelSend.setFont(new Font("Times", Font.BOLD, 12));
		labelSend.setHorizontalAlignment(SwingConstants.LEFT);
		labelSend.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(labelSend);
		
		
		//Create the array or arrays and convert it to strings so it can be added to the combo box
		//an example to see it works
		names = new JComboBox<String>(); // Source https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html
		names.setBounds(100, 390, 300, 30);
		names.setFont(new Font("Times", Font.BOLD, 12));
		frame.getContentPane().add(names);
		
		
		// TODO: limit text to single line
		// TODO: press enter to send (same effect as pressing the send button)
		textAreaSend = new JTextArea();
		textAreaSend.setBounds(10, 460, 300, 80);
		frame.getContentPane().add(textAreaSend);
		textAreaSend.setLineWrap(true); //Source: https://stackoverflow.com/questions/8858584/how-to-wrap-text-in-a-jtextarea
		
		
		textAreaSend.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				//e.getKeyCode();
				int keyCode = e.getKeyCode();
				if (keyCode==10) {
					if(onSendButtonCallback != null)
						onSendButtonCallback.run();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
		}); 
		
		
		buttonSend = new JButton("Send");
		buttonSend.setBounds(350, 490, 100, 20);// x axis, y axis, width, height
		buttonSend.addActionListener(ignored -> {
			if(this.onSendButtonCallback == null) return; 
			this.onSendButtonCallback.run();
		});
		frame.getContentPane().add(buttonSend);
		
		this.styleManager = new StyleManager(); 
		
		this.setState(IClientGUI.DEFAULT_STATE);
		
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

	@Override
	public void setState(State state) {
		if(state == null) return; 
		boolean showSendUI; 
		switch(state) {
			default:
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
			case CONNECTING: {
				this.buttonconnect.setText("Connecting..");
				this.buttonconnect.setEnabled(false);
				this.textfieldClientName.setEnabled(false);
				this.textfieldAddressName.setEnabled(false);
				showSendUI = false; 
				break; 
			}
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

	@Override
	public void displayDialog(String dialogTitle, String displayedMessage) {
		if(displayedMessage == null || displayedMessage.isEmpty()) return; 
		if(dialogTitle == null || dialogTitle.isEmpty()) dialogTitle = "Message"; 
		JOptionPane.showMessageDialog(null, displayedMessage, dialogTitle, JOptionPane.INFORMATION_MESSAGE);	
	}

	@Override
	public void setApplicationTitle(String applicationTitle) {
		if(applicationTitle == null || applicationTitle.isEmpty()) return; 
		this.frame.setTitle(applicationTitle);
	}

	@Override
	public void setServerAddress(String address) {
		if(address == null) return; 
		this.textfieldAddressName.setText(address);
	}
	
	@Override
	public void clearChat() {
		this.textAreaDisplay.setText("");
	}

	@Override
	public void appendChat(String appendedLine) {
		if(appendedLine == null) return; 
		StyledDocument doc = this.textAreaDisplay.getStyledDocument(); 
		this.styleManager.addText(doc, appendedLine);
		this.styleManager.addNewLine(doc);
	}
	
	public void appendChat(RichText appendedLine) {
		StyledDocument doc = this.textAreaDisplay.getStyledDocument(); 
		this.styleManager.addRichText(doc, appendedLine);
		this.styleManager.addNewLine(doc);
	}

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
