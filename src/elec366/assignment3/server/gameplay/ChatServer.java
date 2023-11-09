package elec366.assignment3.server.gameplay;

import java.util.Arrays;
import java.util.logging.Logger;

import elec366.assignment3.server.ServerResources;
import elec366.assignment3.server.ServerSettings;
import elec366.assignment3.type.ChatMessageType;

public class ChatServer extends MultiplayerServer {

	public ChatServer(Logger serverLogger, Logger networkLogger, int port) {
		super(serverLogger, networkLogger, port);
	}

	@Override
	public void onPlayerNameConflict(Player player) {
		this.sendToPlayerAndConsole(player, ChatMessageType.SYSMSG_SERVER, String.format(ServerResources.SERVER_USERNAME_CONFLICT, player.getName()));
	}

	@Override
	public boolean onPlayerPreLogin(Player player) {
		String name = player.getName(); 
		if(name.length() < ServerSettings.USERNAME_MIN_LEN || name.length() > ServerSettings.USERNAME_MAX_LEN) {
			this.sendToPlayerAndConsole(player, ChatMessageType.SYSMSG_SERVER, ServerResources.SERVER_USERNAME_LENGTH_VIOLATION);
			return false; 
		}
		// TODO: username pattern check
		return true; 
	}

	@Override
	public void onPlayerLogin(Player player) {
		String name = player.getName(); 
		String time = "<time>"; // TODO: get time
		String[] playerNames = this.getOnlinePlayerStream().map(Player::getName).toArray(String[]::new); 
		
		this.broadcastMessage(ChatMessageType.SYSMSG_SERVER, String.format(ServerResources.SERVER_JOIN_MESSAGE, name));
		player.sendServerMessage(String.format(ServerResources.SERVER_WELCOME1, playerNames.length, time)); 
		player.sendServerMessage(ServerResources.SERVER_WELCOME2 + Arrays.toString(playerNames));
	}

	@Override
	public void onPlayerQuit(Player player) {
		String name = player.getName(); 
		this.broadcastMessage(ChatMessageType.SYSMSG_SERVER, String.format(ServerResources.SERVER_QUIT_MESSAGE, name));
	}

	@Override
	public void onPlayerChat(Player player, String message) {
		String chatMessage = String.format(message, player.getName(), message); 
		this.broadcastMessage(ChatMessageType.CHAT_GLOBAL, chatMessage);
		
	}
	
	public void sendToPlayerAndConsole(Player player, ChatMessageType type, String message) {
		this.getLogger().info(message);
		player.sendMessage(type, message);
	}
	
	public void broadcastMessage(ChatMessageType type, String message) {
		this.getLogger().info(message);
		this.getOnlinePlayerStream().forEach(p -> p.sendMessage(type, message));
	}
	
}
