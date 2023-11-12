package elec366.assignment3.client.app.gui;

import java.util.logging.Logger;

import elec366.assignment3.client.app.gui.backend.GraphicalClient;
import elec366.assignment3.client.app.gui.frontend.ClientGUI;
import elec366.assignment3.client.app.gui.frontend.IClientGUI;
import elec366.assignment3.client.app.type.ConnectionInformation;
import elec366.assignment3.util.LoggerUtil;
import elec366.assignment3.util.Pair;

public class GraphicalClientLauncher implements Runnable {

	private final ConnectionInformation conn; 
	private final Pair<Logger, Logger> loggers;
	
	private final IClientGUI ui;
	
	public GraphicalClientLauncher(ConnectionInformation conn) {
		this.conn = conn; 
		this.loggers = new Pair<>(LoggerUtil.createLogger("Client"), LoggerUtil.createLogger("Tracer")); 
		this.ui = new ClientGUI(); 
	}
	
	@Override
	public void run() {
		this.ui.onConnectionButton(this::onConnectButton);
		this.ui.setState(IClientGUI.State.DISCONNECTED);
		this.ui.showUI();
	}

	protected void onConnectButton() {
		String username = this.ui.getUsername().trim(); 
		if(username.isEmpty()) return; 
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
