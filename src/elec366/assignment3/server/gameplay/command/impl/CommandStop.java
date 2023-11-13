package elec366.assignment3.server.gameplay.command.impl;

import elec366.assignment3.server.ServerResources;
import elec366.assignment3.server.ServerSettings;
import elec366.assignment3.server.gameplay.ChatServer;
import elec366.assignment3.server.gameplay.Player;
import elec366.assignment3.server.gameplay.command.CommandExecutor;

public class CommandStop extends CommandExecutor {

	public CommandStop(ChatServer server, Player player, String label, String command, String rawInput) {
		super(server, player, label, command, rawInput);
	}

	@Override
	public String getHelpTopic() {
		return super.getHelpTopic(); 
	}
	
	@Override
	public boolean execute() {
		if(ServerSettings.STOP_PASSWORD.isEmpty() || (this.hasArg(1) && ServerSettings.STOP_PASSWORD.equals(this.getArg(1)))) {
			this.getServer().logMessage(ServerResources.COMMAND_STOP_SHUTDOWN);
			this.getPlayer().sendServerMessage(ServerResources.COMMAND_STOP_SHUTDOWN);
			this.getServer().shutdown();
		}
		else {
			this.getPlayer().sendServerMessage(ServerResources.COMMAND_STOP_PASSWORD);
		}
		return true; 
	}
	
}
