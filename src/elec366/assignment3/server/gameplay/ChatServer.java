package elec366.assignment3.server.gameplay;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

import elec366.assignment3.richtext.RichText;
import elec366.assignment3.richtext.RichTextParser;
import elec366.assignment3.server.ServerResources;
import elec366.assignment3.server.ServerSettings;
import elec366.assignment3.type.ChatMessageType;
import elec366.assignment3.util.SingleLineSanitizer;

public class ChatServer extends MultiplayerServer {

	public static boolean USE_CONSOLE_CODE = true; 
	
	public ChatServer(Logger serverLogger, Logger networkLogger, int port) {
		super(serverLogger, networkLogger, port);
	}

	@Override
	public void onPlayerNameConflict(Player player) {
		String message = String.format(ServerResources.LOGIN_NAME_CONFLICT, player.getName()); 
		this.logMessage(String.format(ServerResources.LOGIN_KICK_PREFIX, player.getName()) + message);
		player.sendServerMessage(message); 
	}

	@Override
	public boolean onPlayerPreLogin(Player player) {
		String message = this.playerUsernameCheck(player.getName()); 
		if(message == null) return true; 
		this.logMessage(String.format(ServerResources.LOGIN_KICK_PREFIX, player.getName()) + message);
		player.sendServerMessage(message); 
		return false; 
	}
	
	private String playerUsernameCheck(String name) {
		if(name.length() < ServerSettings.USERNAME_MIN_LEN || name.length() > ServerSettings.USERNAME_MAX_LEN) 
			return ServerResources.LOGIN_NAME_LENGTH_VIOLATION; 
		if(!name.matches(ServerSettings.USERNAME_PATTERN)) 
			return ServerResources.LOGIN_NAME_INVALID_CHARACTER; 
		return null; 
	}

	@Override
	public void onPlayerLogin(Player player) {
		String name = player.getName(); 
		String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yy HH:mm:ss")); 
		String[] playerNames = this.getOnlinePlayerStream().map(Player::getName).toArray(String[]::new); 
		this.broadcastMessage(ChatMessageType.SYSMSG_SERVER, String.format(ServerResources.JOIN_MESSAGE, name));
		player.sendServerMessage(String.format(ServerResources.JOIN_WELCOME1, playerNames.length, time)); 
		player.sendServerMessage(ServerResources.JOIN_WELCOME2 + Arrays.toString(playerNames));
	}

	@Override
	public void onPlayerQuit(Player player) {
		String name = player.getName(); 
		this.broadcastMessage(ChatMessageType.SYSMSG_SERVER, String.format(ServerResources.QUIT_MESSAGE, name));
	}

	@Override
	public void onPlayerChat(Player player, String message) {
		if(message.length() > ServerSettings.MESSAGE_LENGTH_MAX) {
			player.sendServerMessage(ServerResources.OVERSIZE_MESSAGE);
			this.logMessage(String.format(ServerResources.OVERSIZE_MESSAGE_LOG, player.getName(), message.length())); 
			return; 
		}
		message = SingleLineSanitizer.sanitize(message); 
		if(message.isEmpty()) return; 
		if(message.startsWith("/")) {
			this.logMessage(String.format(ServerResources.COMMAND_ISSUE, player.getName(), message)); 
			String command = message.substring(1); 
			this.onPlayerCommand(player, command.trim()); 
		}
		else {
			String chatMessage = String.format(ServerResources.CHAT_FORMAT, player.getName(), message); 
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
	
	public void onPlayerCommand(Player player, String message) {
		if(message.isEmpty()) {
			player.sendServerMessage(ServerResources.COMMAND_UNKNOWN);
			return; 
		}
		String[] array0 = message.split(" ", 2); 
		String command = array0[0]; 
		String args = array0.length >= 2 ? array0[1] : ""; 
		switch(command.toLowerCase()) {
			default: {
				player.sendServerMessage(ServerResources.COMMAND_UNKNOWN);
				return; 
			}
			case "help": {
				player.sendServerMessage(ServerResources.COMMAND_HELP);
				return; 
			}
			case "stop": {
				if(ServerSettings.STOP_PASSWORD.isEmpty() || ServerSettings.STOP_PASSWORD.equals(args)) {
					this.logMessage(ServerResources.COMMAND_STOP_SHUTDOWN);
					player.sendServerMessage(ServerResources.COMMAND_STOP_SHUTDOWN);
					this.shutdown();
				}
				else {
					player.sendServerMessage(ServerResources.COMMAND_STOP_PASSWORD);
				}
				return; 
			}
			case "list": {
				String[] playerNames = this.getOnlinePlayerStream().map(Player::getName).toArray(String[]::new); 
				player.sendServerMessage(String.format(ServerResources.COMMAND_LIST_REPLY1, playerNames.length));
				player.sendServerMessage(Arrays.toString(playerNames));
				return; 
			}
			case "tell": {
				String[] array1 = args.split(" ", 2); 
				if(array1.length < 2) {
					player.sendServerMessage(ServerResources.COMMAND_TELL_HELP);
					return; 
				}
				String receiverName = array1[0]; 
				if(receiverName.equalsIgnoreCase(player.getName())) {
					player.sendServerMessage(ServerResources.COMMAND_TELL_SELF);
					return; 
				}
				Optional<Player> oPlayer = this.getOnlinePlayerStream().filter(p -> p.getName().equalsIgnoreCase(receiverName)).findAny(); 
				if(!oPlayer.isPresent()) {
					player.sendServerMessage(ServerResources.COMMAND_TELL_OFFLINE);
					return; 
				}
				Player receiver = oPlayer.get(); 
				String concent = array1[1].trim(); 
				String msg1 = String.format(ServerResources.COMMAND_TELL_MSG, player.getName(), receiver.getName(), concent); 
				String msg2 = String.format(ServerResources.COMMAND_TELL_MSG, ServerResources.COMMAND_TELL_MSG_YOU, receiver.getName(), concent);
				String msg3 = String.format(ServerResources.COMMAND_TELL_MSG, player.getName(), ServerResources.COMMAND_TELL_MSG_YOU, concent);
				this.logMessage(msg1);
				player.sendMessage(ChatMessageType.CHAT_PRIVATE, msg2);
				receiver.sendMessage(ChatMessageType.CHAT_PRIVATE, msg3); 
				return; 
			}
		}
	}
	
}
