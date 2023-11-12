package elec366.assignment3.client.app.gui.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

import elec366.assignment3.client.app.gui.frontend.IClientGUI;
import elec366.assignment3.client.app.type.ConnectionInformation;
import elec366.assignment3.client.gameplay.ChatClient;
import elec366.assignment3.richtext.RichTextParser;
import elec366.assignment3.type.ChatMessageType;
import elec366.assignment3.util.Pair;

public class GraphicalClient extends ChatClient {

	private final String username; 
	private final IClientGUI ui; 
	private final Runnable disconnectionCallback; 
	
	public GraphicalClient(IClientGUI ui, String username, ConnectionInformation conn, Pair<Logger, Logger> loggers, Runnable disconnectionCallback) {
		super(loggers.getFirst(), loggers.getSecond(), conn.getHost(), conn.getPort(), username);
		this.disconnectionCallback = disconnectionCallback; 
		this.username = username; 
		this.ui = ui; 
		this.ui.onSendButton(this::onSendButton);
		this.ui.onConnectionButton(this::onDisconnectionButton);
	}
	
	@Override
	public void onChatServerConnection() {
		this.ui.setState(IClientGUI.State.CONNECTED);
	}

	@Override
	public void onInboundChatMessage(ChatMessageType type, String message) {
		this.ui.appendChat(RichTextParser.parse(message));
	}

	@Override
	public void onPlayerListUpdate(String[] players) {
		ArrayList<String> playerList = new ArrayList<>(players.length); 
		for(String player : players) playerList.add(player); 
		playerList.removeIf(s -> s.equalsIgnoreCase(this.username)); 
		Collections.sort(playerList); 
		playerList.add(0, "<everyone>");
		this.ui.setOnlinePlayers(playerList.toArray(new String[0]));
	}

	@Override
	public void onChatServerDisconnection() {
		this.ui.removeCallbacks();
		this.ui.appendChat("Disconnected.");
		this.ui.appendChat("");
		this.ui.setState(IClientGUI.State.DISCONNECTED);
		this.disconnectionCallback.run();
	}
	
	@Override
	public void onChatServerAbnormalDisconnection(String reason, Throwable cause) {
		this.ui.appendChat("A network has occured. " + (reason == null ? "" : "Internal information: " + reason));
		if(cause != null) this.ui.appendChat(cause.toString());
		this.onChatServerDisconnection();
	}
	
	protected void onSendButton() {
		String chat = this.ui.getMessage(); 
		String recepient = this.ui.getRecepient(); 
		if(recepient.isEmpty() || recepient.equals("<everyone>")) {
			this.sendChatMessage(chat);
		}
		else {
			String command = String.format("/tell %s %s", recepient, chat); 
			this.sendChatMessage(command);
		}
	}
	
	protected void onDisconnectionButton() {
		this.disconnect();
	}

}
