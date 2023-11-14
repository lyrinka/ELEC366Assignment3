package elec366.assignment3.server.app.gui.frontend;

/*
 * This interface applies to only server.
 * 
 * Denotes that the implementing class holds an implementation of server-side GUI. 
 */
public interface IServerGUI {
	
	// User interface state
	void showUI(); 
	void closeUI(); 
	
	// UI writes - Set online players
	// (If the UI only displays the number of online players, use the length of this array.)
	void setOnlinePlayers(String[] players);
	
}
