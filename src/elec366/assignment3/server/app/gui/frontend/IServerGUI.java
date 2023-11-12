package elec366.assignment3.server.app.gui.frontend;

public interface IServerGUI {
	
	// User interface state
	void showUI(); 
	
	// UI writes - Set online players
	// (If the UI only displays the number of online players, use the length of this array.)
	void setOnlinePlayers(String[] players); 
	
}
