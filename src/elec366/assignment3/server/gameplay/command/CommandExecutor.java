package elec366.assignment3.server.gameplay.command;

import elec366.assignment3.server.gameplay.ChatServer;
import elec366.assignment3.server.gameplay.Player;

public abstract class CommandExecutor {

	private final ChatServer server; 
	private final Player player; 	// NonNull
	private final String label; 	// Nullable
	private final String command; 	// NonNull
	private final String rawInput; 	// Nullable
	private String[] args; 			// Lazy computed
	
	public CommandExecutor(ChatServer server, Player player, String label, String command, String rawInput) {
		this.server = server; 
		this.player = player; 
		this.label = label; 
		this.command = command; 
		this.rawInput = rawInput; 
		this.args = null; 
	}
	
	public ChatServer getServer() {
		return this.server; 
	}
	
	public Player getPlayer() {
		return this.player;
	}

	public String getLabel() {
		return this.label;
	}

	public String getCommand() {
		return this.command;
	}

	public String getRawInput() {
		return this.rawInput; 
	}
	
	public String[] getArgs() {
		if(this.args == null) 
			this.args = this.command.split(" "); 
		return this.args;
	}
	
	public boolean hasArg(int position) {
		if(position > 0) position--; 
		return position < this.getArgs().length && !(position == 0 && this.getArg(1).isEmpty()); 
	}
	
	public String getArg(int position) {
		if(position > 0) position--; 
		try {
			return this.getArgs()[position]; 
		}
		catch(ArrayIndexOutOfBoundsException ignored) {
			return null; 
		}
	}
	
	public abstract boolean execute(); 
	
	public String getHelpTopic() {
		return null; 
	}

}
