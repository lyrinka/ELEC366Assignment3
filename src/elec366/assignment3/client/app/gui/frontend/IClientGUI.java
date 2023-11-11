package elec366.assignment3.client.app.gui.frontend;

import elec366.assignment3.richtext.RichText;

public interface IClientGUI {
	
	enum State {
		DISCONNECTED, 
		CONNECTING, 
		CONNECTED, 
	}
	
	public static State DEFAULT_STATE = State.DISCONNECTED; 
	
	// UI activity - Callbacks
	void onConnectionButton(Runnable callback); 
	void onSendButton(Runnable callback); 
	void removeCallbacks(); 
	
	// User interface state
	void showUI(); 
	
	// User interface state
	void setState(State state); 
	
	// Generic - Display dialog
	void displayDialog(String dialogTitle, String displayedMessage); 
	
	// Generic - Change application title
	void setApplicationTitle(String applicationTitle); 
	
	// UI writes - Append text to chat box
	void clearChat(); 
	void appendChat(String appendedLine); 
	default void appendChat(RichText appendedLine) {
		this.appendChat(appendedLine.toString());
	}
	
	// UI writes - Change combo box online player list
	void setOnlinePlayers(String[] onlinePlayerNames); 
	
	// UI reads - Get player provided user name
	String getUsername(); 
	
	// UI reads - Get combo box destination
	String getRecepient(); 
	
	// UI reads - Get chat box content
	String getChat(); 
	
}