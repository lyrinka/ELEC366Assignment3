package elec366.assignment3.server.gameplay;

import java.util.Arrays;
import java.util.Optional;

import elec366.assignment3.protocol.packet.impl.PacketOutChat;
import elec366.assignment3.server.R;

public class ChatServer extends PacketServer {
	
	public ChatServer() {
		this(R.DEFAULT_PORT); 
	}
	
	public ChatServer(int port) {
		super(port); 
	}
	
	@Override
	protected void onPlayerUsernameConflict(Player player) {
		this.getLogger().info("Client " + player.getConnectionID() + " lost connection: " + player.getName() + " is already online!"); 
		player.sendMessage(R.USERNAME_CONFLICT);
	}

	@Override
	protected boolean onPlayerPreLogin(Player player) {
		String name = player.getName(); 
		if(name.length() < R.USERNAME_LEN_MIN || name.length() > R.USERNAME_LEN_MAX) {
			player.sendMessage(R.USERNAME_TOO_SHORT);
			return false; 
		}
		return true; 
	}
	
	@Override
	protected void onPlayerLogin(Player player) {
		this.broadcastPlayerChange(); 
		this.broadcast(String.format(R.PLAYER_CONNECT, player.getName()));
	}

	@Override
	protected void onPlayerQuit(Player player) {
		this.broadcastPlayerChange(); 
		this.broadcast(String.format(R.PLAYER_DISCONNECT, player.getName()));
		
	}
	
	@Override
	protected void onPlayerChat(Player player, String message) {
		if(message.startsWith("/")) {
			this.handleCommand(player, message.substring(1));
			return; 
		}
		if(message.startsWith("./")) {
			message = message.substring(1); 
		}
		message = message.trim(); 
		if(message.isEmpty()) return; 
		this.handleChat(player, message); 
	}
	
	private void handleChat(Player player, String message) {
		this.broadcast(PacketOutChat.Type.CHAT_GLOBAL, String.format(R.GLOBAL_CHAT, player.getName(), message));
	}
	
	private void handleCommand(Player player, String command) {
		String[] array0 = command.split(" ", 2); 
		if(array0.length < 1 || array0[0].isEmpty()) {
			player.sendMessage(R.UNKNOWN_COMMAND);
			return; 
		}
		command = array0[0]; 
		String args = ""; 
		if(array0.length >= 2) args = array0[1]; 
		
		switch(command.toLowerCase()) {
			default: {
				player.sendMessage(R.UNKNOWN_COMMAND);
				break; 
			}
			case "tell": {
				this.handleCommandTell(player, args);
				break; 
			}
			case "list": {
				this.handleCommandList(player);
				break; 
			}
		}
	}
	
	private void handleCommandTell(Player sender, String args) {
		String[] array0 = args.split(" ", 2); 
		if(array0.length < 2) {
			sender.sendMessage(R.PRIVATE_CHAT_HELP);
			return; 
		}
		String specifiedName = array0[0]; 
		String message = array0[1]; 
		Optional<Player> optionalRecepient = this.getOnlinePlayers().stream().filter(p -> p.getName().equalsIgnoreCase(specifiedName)).findAny(); 
		if(!optionalRecepient.isPresent()) {
			sender.sendMessage(R.PRIVATE_CHAT_NOT_ONLINE);
			return; 
		}
		Player recepient = optionalRecepient.get(); 
		String msg1 = String.format(R.PRIVATE_CHAT1, sender.getName(), message); 
		String msg2 = String.format(R.PRIVATE_CHAT2, recepient.getName(), message); 
		String msg3 = String.format(R.PRIVATE_CHAT2, sender.getName(), recepient.getName(), message); 
		recepient.sendMessage(msg1); 
		sender.sendMessage(msg2);
		this.getLogger().info(msg3);
	}
	
	private void handleCommandList(Player sender) {
		String[] players = this.getOnlinePlayers().stream().map(p -> p.getName()).toArray(String[]::new); 
		sender.sendMessage(String.format(R.LIST_COMMAND, players.length));
		sender.sendMessage(Arrays.toString(players));
	}
	
	private void broadcast(String message) {
		this.getLogger().info(message);
		this.getOnlinePlayers().forEach(p -> p.sendMessage(message));
	}
	
	private void broadcast(PacketOutChat.Type type, String message) {
		this.getLogger().info(message);
		this.getOnlinePlayers().forEach(p -> p.sendMessage(type, message));
	}
	
	private void broadcastPlayerChange() {
		this.getOnlinePlayers().forEach(p -> this.updateClientPlayerlist(p)); 
	}

}
