package elec366.assignment3.server.app.gui.frontend;

public interface IServerGUI {

	public static enum State {
		STARTING, 
		STARTED, 
	}
	
	// User interface state
	void showUI(); 
	
	// User interface state
	void setState(State state); 
	
	// UI writes - Set online players
	// (If the UI only displays the number of online players, use the length of this array.)
	void setOnlinePlayers(String[] players); 
	
	// UI writes - Append text to log
	void appendLog(String appendedLine); 
	
}
