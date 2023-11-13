package elec366.assignment3.server.gameplay;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.Logger;

import elec366.assignment3.richtext.RichText;
import elec366.assignment3.richtext.RichTextParser;
import elec366.assignment3.server.ServerResources;
import elec366.assignment3.server.ServerSettings;
import elec366.assignment3.server.gameplay.command.CommandExecutor;
import elec366.assignment3.server.gameplay.command.CommandExecutorInstantiationException;
import elec366.assignment3.server.gameplay.command.CommandType;
import elec366.assignment3.type.ChatMessageType;
import elec366.assignment3.util.SingleLineSanitizer;

public class ChatServer extends MultiplayerServer {

	public static boolean USE_CONSOLE_CODE = true; 
	
	public ChatServer(Logger serverLogger, Logger networkLogger, int port) {
		super(serverLogger, networkLogger, port);
	}

	@Override
	public void onPlayerNameConflict(Player player) {
		String message = String.format(ServerResources.LOGIN.NAME_CONFLICT, player.getName()); 
		this.logMessage(String.format(ServerResources.LOGIN.CONSOLE_PREFIX, player.getName()) + message);
		player.sendServerMessage(message); 
	}

	@Override
	public boolean onPlayerPreLogin(Player player) {
		String message = this.playerUsernameCheck(player.getName()); 
		if(message == null) return true; 
		this.logMessage(String.format(ServerResources.LOGIN.CONSOLE_PREFIX, player.getName()) + message);
		player.sendServerMessage(message); 
		return false; 
	}
	
	private String playerUsernameCheck(String name) {
		if(name.length() < ServerSettings.USERNAME_MIN_LEN) 
			return String.format(ServerResources.LOGIN.NAME_TOO_SHORT, ServerSettings.USERNAME_MIN_LEN); 
		if(name.length() > ServerSettings.USERNAME_MAX_LEN)
			return String.format(ServerResources.LOGIN.NAME_TOO_SHORT, ServerSettings.USERNAME_MAX_LEN); 
		if(!name.matches(ServerSettings.USERNAME_PATTERN)) 
			return ServerResources.LOGIN.INVALID_CHARACTER; 
		return null; 
	}

	@Override
	public void onPlayerLogin(Player player) {
		String name = player.getName(); 
		String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yy HH:mm:ss")); 
		String[] playerNames = this.getOnlinePlayerStream().map(Player::getName).toArray(String[]::new); 
		this.broadcastMessage(ChatMessageType.SYSMSG_SERVER, String.format(ServerResources.JOIN.MESSAGE, name));
		player.sendServerMessage(String.format(ServerResources.JOIN.WELCOME1, playerNames.length)); 
		player.sendServerMessage(String.format(ServerResources.JOIN.WELCOME2, time)); 
		player.sendServerMessage(String.format(ServerResources.JOIN.WELCOME3, Arrays.toString(playerNames)));
	}

	@Override
	public void onPlayerQuit(Player player) {
		String name = player.getName(); 
		this.broadcastMessage(ChatMessageType.SYSMSG_SERVER, String.format(ServerResources.QUIT.MESSAGE, name));
	}

	@Override
	public void onPlayerChat(Player player, String message) {
		if(message.length() > ServerSettings.MESSAGE_LENGTH_MAX) {
			player.sendServerMessage(ServerResources.CHAT.OVERSIZED);
			this.logMessage(String.format(ServerResources.CHAT.OVERSIZED_LOG, player.getName(), message.length())); 
			return; 
		}
		message = SingleLineSanitizer.sanitize(message); 
		if(message.isEmpty()) return; 
		if(message.startsWith("/")) {
			this.logMessage(String.format(ServerResources.COMMAND.ISSUE_LOG, player.getName(), message)); 
			String command = message.substring(1); 
			this.onPlayerCommand(player, command.trim()); 
		}
		else {
			String chatMessage = String.format(ServerResources.CHAT.FORMAT, player.getName(), message); 
			this.broadcastMessage(ChatMessageType.CHAT_GLOBAL, chatMessage);
		}
	}
	
	public void broadcastMessage(ChatMessageType type, String message) {
		this.logMessage(message);
		this.getOnlinePlayerStream().forEach(p -> p.sendMessage(type, message));
	}
	
	public void logMessage(String message) {
		RichText text = RichTextParser.parse(message); 
		String formattedMessage = USE_CONSOLE_CODE ? text.toConsoleString() : text.toString(); 
		this.getLogger().info("[PLAY] " + formattedMessage);
	}
	
	public void onPlayerCommand(Player player, String rawInput) {
		if(rawInput.isEmpty()) {
			player.sendServerMessage(ServerResources.COMMAND.UNKNOWN);
			return; 
		}
		String[] array0 = rawInput.split(" ", 2); 
		String label = array0[0]; 
		String command = array0.length >= 2 ? array0[1] : ""; 
		
		CommandType type = CommandType.get(label); 
		if(type == null) {
			player.sendServerMessage(ServerResources.COMMAND.UNKNOWN);
			return; 
		}
		CommandExecutor executor; 
		try {
			executor = type.construct(this, player, label, command, rawInput); 
		}
		catch(CommandExecutorInstantiationException ex) {
			this.getLogger().warning("Exception while parsing " + rawInput);
			ex.printStackTrace();
			player.sendServerMessage(ServerResources.COMMAND.EXCEPTION);
			return; 
		}
		boolean result = executor.execute(); 
		if(!result) {
			player.sendServerMessage(String.format(ServerResources.COMMAND.MALFORMAT, label));
			return; 
		}
	}
	
}
