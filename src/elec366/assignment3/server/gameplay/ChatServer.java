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
		String message = String.format(ServerResources.SERVER_USERNAME_CONFLICT, player.getName()); 
		this.getLogger().info(String.format(ServerResources.SERVER_USER_KICK_PREFIX, player.getName()) + message);
		player.sendServerMessage(message); 
	}

	@Override
	public boolean onPlayerPreLogin(Player player) {
		String message = this.playerUsernameCheck(player.getName()); 
		if(message == null) return true; 
		this.getLogger().info(String.format(ServerResources.SERVER_USER_KICK_PREFIX, player.getName()) + message);
		player.sendServerMessage(message); 
		return false; 
	}
	
	private String playerUsernameCheck(String name) {
		if(name.length() < ServerSettings.USERNAME_MIN_LEN || name.length() > ServerSettings.USERNAME_MAX_LEN) {
			return ServerResources.SERVER_USERNAME_LENGTH_VIOLATION; 
		}
		// TODO: username pattern check
		return null; 
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
		if(message.equals("/stop")) {
			this.stop(); 
			return; 
		}
		String chatMessage = String.format(ServerResources.SERVER_CHAT_FORMAT, player.getName(), message); 
		this.broadcastMessage(ChatMessageType.CHAT_GLOBAL, chatMessage);
		
	}

	public void broadcastMessage(ChatMessageType type, String message) {
		this.getLogger().info(message);
		this.getOnlinePlayerStream().forEach(p -> p.sendMessage(type, message));
	}
	
}
