package elec366.assignment3.server.app.gui.backend;

import java.util.logging.Logger;

import elec366.assignment3.server.app.gui.frontend.IServerGUI;
import elec366.assignment3.server.gameplay.ChatServer;
import elec366.assignment3.server.gameplay.Player;
import elec366.assignment3.util.Pair;

public class GraphicalServer extends ChatServer {

	private final IServerGUI ui; 
	
	public GraphicalServer(IServerGUI ui, int port, Pair<Logger, Logger> loggers) {
		super(loggers.getFirst(), loggers.getSecond(), port); 
		this.ui = ui; 
		this.ui.setOnlinePlayers(new String[0]);
	}
	
	@Override
	public void onPlayerLogin(Player player) {
		super.onPlayerLogin(player);
		this.updateOnlinePlayers(); 
	}
	
	@Override
	public void onPlayerQuit(Player player) {
		super.onPlayerQuit(player);
		this.updateOnlinePlayers();
	}
	
	
	public void updateOnlinePlayers() {
		this.ui.setOnlinePlayers(this.getOnlinePlayerStream().toArray(String[]::new));
	}
	
}
