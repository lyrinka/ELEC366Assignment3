package elec366.assignment3.server.gameplay.command.impl;

import java.util.Optional;

import elec366.assignment3.server.ServerResources;
import elec366.assignment3.server.gameplay.ChatServer;
import elec366.assignment3.server.gameplay.Player;
import elec366.assignment3.server.gameplay.command.CommandExecutor;
import elec366.assignment3.type.ChatMessageType;

public class CommandTell extends CommandExecutor {

	public CommandTell(ChatServer server, Player player, String label, String command, String rawInput) {
		super(server, player, label, command, rawInput);
	}
	
	@Override
	public String getHelpTopic() {
		return ServerResources.COMMAND_TELL.HELP; 
	}

	@Override
	public boolean execute() {
		if(!this.hasArg(2)) return false; 
		String senderName = this.getPlayer().getName(); 
		String receiverName0 = this.getArg(1); 
		if(receiverName0.equalsIgnoreCase(senderName)) {
			this.getPlayer().sendServerMessage(ServerResources.COMMAND_TELL.SELF);
			return true; 
		}
		Optional<Player> oPlayer = this.getServer().getOnlinePlayerStream().filter(p -> p.getName().equalsIgnoreCase(receiverName0)).findAny(); 
		if(!oPlayer.isPresent()) {
			this.getPlayer().sendServerMessage(ServerResources.COMMAND_TELL.OFFLINE);
			return true; 
		}
		Player receiver = oPlayer.get(); 
		String receiverName = receiver.getName(); 
		senderName   = String.format(ServerResources.COMMAND_TELL.FORMAT_OTHERS, senderName); 
		receiverName = String.format(ServerResources.COMMAND_TELL.FORMAT_OTHERS, receiverName); 
		String yourName = ServerResources.COMMAND_TELL.FORMAT_YOU; 
		String content = this.getCommand().split(" ", 2)[1]; 
		String msg1 = String.format(ServerResources.COMMAND_TELL.MESSAGE, senderName, receiverName, content); 
		String msg2 = String.format(ServerResources.COMMAND_TELL.MESSAGE, yourName,   receiverName, content);
		String msg3 = String.format(ServerResources.COMMAND_TELL.MESSAGE, senderName, yourName,     content);
		this.getServer().logMessage(msg1);
		this.getPlayer().sendMessage(ChatMessageType.CHAT_PRIVATE, msg2);
		receiver.sendMessage(ChatMessageType.CHAT_PRIVATE, msg3); 
		return true; 
	}

}
