package elec366.assignment3.client.app.gui;

import java.util.logging.Logger;

import elec366.assignment3.client.app.gui.backend.GraphicalClient;
import elec366.assignment3.client.app.gui.frontend.ClientGUI;
import elec366.assignment3.client.app.gui.frontend.IClientGUI;
import elec366.assignment3.client.app.type.ConnectionInformation;
import elec366.assignment3.util.LoggerUtil;
import elec366.assignment3.util.Pair;

public class GraphicalClientLauncher implements Runnable {

	private ConnectionInformation conn; 
	private final Pair<Logger, Logger> loggers;
	
	private final IClientGUI ui;
	
	public GraphicalClientLauncher(ConnectionInformation conn, boolean verbose) {
		this.conn = conn; 
		this.loggers = new Pair<>(LoggerUtil.createLogger("Client"), verbose ? LoggerUtil.createLogger("Tracer") : null); 
		this.ui = new ClientGUI(); 
	}
	
	@Override
	public void run() {
		this.ui.onConnectionButton(this::onConnectButton);
		this.ui.setServerAddress(conn.getHost() + ":" + conn.getPort());
		this.ui.setState(IClientGUI.State.DISCONNECTED);
		this.ui.showUI();
	}

	protected void onConnectButton() {
		String addrRaw = this.ui.getServerAddress(); 
		String[] addrSplit = addrRaw.split(":", 2); 
		try {
			this.conn = new ConnectionInformation(addrSplit[0], Integer.parseInt(addrSplit[1])); 
			if(this.conn.getHost().isEmpty()) throw new IllegalArgumentException(); 
		}
		catch(ArrayIndexOutOfBoundsException | IllegalArgumentException ignored) {
			this.ui.displayDialog(null, "The server address is invalid.");
			return; 
		}
		
		String username = this.ui.getUsername().trim(); 
		if(username.isEmpty()) {
			this.ui.displayDialog(null, "Please enter a username.");
			return; 
		}
		
		this.ui.clearChat();
		this.ui.setState(IClientGUI.State.CONNECTING);
		new Thread(() -> {
			(new GraphicalClient(
					this.ui, 
					username, 
					this.conn, 
					this.loggers, 
					this::run
			)).start(); 
		}).start();
	}
	
}
