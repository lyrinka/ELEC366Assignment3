package elec366.assignment3.server.gameplay.command.impl;

import java.util.Objects;

import elec366.assignment3.server.ServerResources;
import elec366.assignment3.server.gameplay.ChatServer;
import elec366.assignment3.server.gameplay.Player;
import elec366.assignment3.server.gameplay.command.CommandExecutor;
import elec366.assignment3.server.gameplay.command.CommandExecutorInstantiationException;
import elec366.assignment3.server.gameplay.command.CommandType;

public class CommandHelp extends CommandExecutor {

	public CommandHelp(ChatServer server, Player player, String label, String command, String rawInput) {
		super(server, player, label, command, rawInput);
	}

	@Override
	public String getHelpTopic() {
		return ServerResources.COMMAND_HELP.HELP; 
	}
	
	@Override
	public boolean execute() {
		this.executeCore(); 
		return true; 
	}
	
	public void executeCore() {
		if(!this.hasArg(1)) {
			this.getPlayer().sendServerMessage(this.getHelpTopic());
			return; 
		}
		String targetLabel = this.getArg(1); 
		CommandType target = CommandType.get(targetLabel); 
		try {
			String targetHelp = Objects.requireNonNull(target).construct(this.getServer(), this.getPlayer(), targetLabel, "", "").getHelpTopic();
			if(Objects.requireNonNull(targetHelp).isEmpty()) throw new NullPointerException(); 
			String[] targetHelpLines = targetHelp.split("\r?\n"); 
			boolean isFirst = true; 
			for(String targetHelpLine : targetHelpLines) {
				String message; 
				if(isFirst) 
					message = String.format(ServerResources.COMMAND_HELP.TOPIC_ON_COMMAND1, targetLabel, targetHelpLine); 
				else
					message = String.format(ServerResources.COMMAND_HELP.TOPIC_ON_COMMAND2, targetHelpLine); 
				this.getPlayer().sendServerMessage(message); 
				isFirst = false; 
			}
		} catch (NullPointerException | CommandExecutorInstantiationException ignored) {
			this.getPlayer().sendServerMessage(String.format(ServerResources.COMMAND_HELP.TOPIC_DEFAULT, targetLabel));
			return; 
		}
	}

}
