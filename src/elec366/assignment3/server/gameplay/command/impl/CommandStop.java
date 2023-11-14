package elec366.assignment3.server.gameplay.command.impl;

import elec366.assignment3.server.ServerResources;
import elec366.assignment3.server.ServerSettings;
import elec366.assignment3.server.gameplay.ChatServer;
import elec366.assignment3.server.gameplay.Player;
import elec366.assignment3.server.gameplay.command.CommandExecutor;

/*
 * This class applies to only server.
 * 
 * Stop command:
 * - /stop				Stops the server, if the server has no stop password.
 * - /stop <password>	Stops the server when password matches, if the server has a stop password.
 */
public class CommandStop extends CommandExecutor {

	public CommandStop(ChatServer server, Player player, String label, String command, String rawInput) {
		super(server, player, label, command, rawInput);
	}

	@Override
	public String getHelpTopic() {
		return ServerResources.COMMAND_STOP.HELP1 + "\n"
			 + ServerResources.COMMAND_STOP.HELP2; 
	}
	
	@Override
	public boolean execute() {
		if(ServerSettings.STOP_PASSWORD.isEmpty() || (this.hasArg(1) && ServerSettings.STOP_PASSWORD.equals(this.getArg(1)))) {
			this.getServer().logMessage(ServerResources.COMMAND_STOP.SHUTTING_DOWN);
			this.getPlayer().sendServerMessage(ServerResources.COMMAND_STOP.SHUTTING_DOWN);
			this.getServer().shutdown();
		}
		else {
			this.getPlayer().sendServerMessage(ServerResources.COMMAND_STOP.PASSWORD_REQUIRED);
		}
		return true; 
	}
	
}
