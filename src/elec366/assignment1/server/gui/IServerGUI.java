package elec366.assignment1.server.gui;

public interface IServerGUI {

	enum State {
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
