package elec366.assignment3.server.gameplay.command.impl;

import java.util.Arrays;

import elec366.assignment3.server.ServerResources;
import elec366.assignment3.server.gameplay.ChatServer;
import elec366.assignment3.server.gameplay.Player;
import elec366.assignment3.server.gameplay.command.CommandExecutor;

public class CommandList extends CommandExecutor {

	public CommandList(ChatServer server, Player player, String label, String command, String rawInput) {
		super(server, player, label, command, rawInput);
	}
	
	@Override
	public String getHelpTopic() {
		return ServerResources.COMMAND_LIST.HELP; 
	}

	@Override
	public boolean execute() {
		String[] playerNames = this.getServer().getOnlinePlayerStream().map(Player::getName).toArray(String[]::new); 
		this.getPlayer().sendServerMessage(String.format(ServerResources.COMMAND_LIST.MESSAGE1, playerNames.length));
		this.getPlayer().sendServerMessage(String.format(ServerResources.COMMAND_LIST.MESSAGE2, Arrays.toString(playerNames)));
		return true; 
	}

}
