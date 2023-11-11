package elec366.assignment3.client.app.gui.frontend;

import java.awt.Font;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import elec366.assignment3.util.SingleLineSanitizer;

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
	
	private Runnable onConnectionButtonCallback;
	private Runnable onSendButtonCallback; 
	
	public ClientGUI() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Chatting Client");
		frame.getContentPane().setLayout(null);
		
		
		labelClientName = new JLabel("Client Name: "); 
		labelClientName.setBounds(10, 50, 300, 30);
		labelClientName.setFont(new Font("Times", Font.BOLD, 12));
		labelClientName.setHorizontalAlignment(SwingConstants.LEFT);
		labelClientName.setVerticalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(labelClientName);
		
		
		textfieldClientName = new JTextField();
		textfieldClientName.setFont(new Font("Times", Font.BOLD, 12));
		textfieldClientName.setBounds(100, 55, 120, 20); // x axis, y axis, width, height
		frame.getContentPane().add(textfieldClientName);
		
		
		buttonconnect = new JButton("Connect");
		buttonconnect.setBounds(230, 55, 100, 20);// x axis, y axis, width, height
		buttonconnect.addActionListener(ignored -> {
			if(this.onConnectionButtonCallback == null) return; 
			this.onConnectionButtonCallback.run(); 
		});
		frame.getContentPane().add(buttonconnect);
		
		
		textAreaDisplay = new JTextArea();
		textAreaDisplay.setBounds(10, 90, 350, 300);
		frame.getContentPane().add(textAreaDisplay);
		frame.getContentPane().repaint(); 
		textAreaDisplay.setEditable(false); 
		
		
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
		names.setBounds(80, 390, 300, 30);
		names.setFont(new Font("Times", Font.BOLD, 12));
		frame.getContentPane().add(names);
		
		
		textAreaSend = new JTextArea();
		textAreaSend.setBounds(10, 430, 300, 80);
		frame.getContentPane().add(textAreaSend);
		
		
		buttonSend = new JButton("Send");
		buttonSend.setBounds(330, 460, 100, 20);// x axis, y axis, width, height
		buttonSend.addActionListener(ignored -> {
			if(this.onSendButtonCallback == null) return; 
			this.onSendButtonCallback.run();
		});
		frame.getContentPane().add(buttonSend);
		
		
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
	public void setState(State state) {
		if(state == null) return; 
		boolean showSendUI; 
		switch(state) {
			default:
			case DISCONNECTED: {
				this.buttonconnect.setText("Connect");
				this.buttonconnect.setEnabled(true);
				this.textfieldClientName.setEnabled(true);
				showSendUI = false; 
				this.setOnlinePlayers(new String[0]);
				this.textAreaSend.setText("");
				break; 
			}
			case CONNECTING: {
				this.buttonconnect.setText("Connecting..");
				this.buttonconnect.setEnabled(false);
				this.textfieldClientName.setEnabled(false);
				showSendUI = false; 
				break; 
			}
			case CONNECTED: {
				this.buttonconnect.setText("Disconnect");
				this.buttonconnect.setEnabled(true);
				this.textfieldClientName.setEnabled(false);
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
	public void clearChat() {
		this.textAreaDisplay.setText("");
	}

	@Override
	public void appendChat(String appendedLine) {
		if(appendedLine == null) return; 
		this.textAreaDisplay.append(SingleLineSanitizer.sanitize(appendedLine) + "\n");
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
	public String getChat() {
		return SingleLineSanitizer.sanitize(this.textAreaSend.getText()); 
	}

}
