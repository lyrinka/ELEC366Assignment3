package elec366.assignment3.server.app.gui;

import java.util.logging.Logger;

import elec366.assignment3.server.app.gui.backend.GraphicalServer;
import elec366.assignment3.server.app.gui.frontend.IServerGUI;
import elec366.assignment3.server.app.gui.frontend.ServerGUI;
import elec366.assignment3.util.LoggerUtil;
import elec366.assignment3.util.Pair;

/*
 * This class applies to only server.
 * 
 * This class launches the GUI and the server. 
 */
public class GraphicalServerLauncher implements Runnable {

	private final int port; 
	private final Pair<Logger, Logger> loggers;
	
	private final IServerGUI ui; 
	
	public GraphicalServerLauncher(int port, boolean verbose) {
		this.port = port; 
		this.loggers = new Pair<>(LoggerUtil.createLogger("Server"), verbose ? LoggerUtil.createLogger("Tracer") : null);
		this.ui = new ServerGUI(); 
	}

	@Override
	public void run() {
		(new GraphicalServer(
				this.ui, 
				this.port, 
				this.loggers
		)).start(); 
		this.ui.showUI();
	}

}
