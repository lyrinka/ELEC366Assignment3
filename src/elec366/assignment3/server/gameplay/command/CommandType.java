package elec366.assignment3.server.gameplay.command;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import elec366.assignment3.server.gameplay.ChatServer;
import elec366.assignment3.server.gameplay.Player;
import elec366.assignment3.server.gameplay.command.impl.CommandHelp;
import elec366.assignment3.server.gameplay.command.impl.CommandList;
import elec366.assignment3.server.gameplay.command.impl.CommandStop;
import elec366.assignment3.server.gameplay.command.impl.CommandTell;

public enum CommandType {

	HELP(CommandHelp.class, "help", "?"), 
	STOP(CommandStop.class, "stop"), 
	LIST(CommandList.class, "list"), 
	TELL(CommandTell.class, "tell", "msg", "whisper", "m", "w"), 
	; 
	
	private static final Map<String, CommandType> registry = new HashMap<>(); 
	
	static {
		for(CommandType commandType : CommandType.values()) {
			String[] labels = commandType.labels; 
			for(String label : labels)
				CommandType.registry.put(label.toLowerCase(), commandType);			
		}
	}
	
	public static CommandType get(String label) {
		return CommandType.registry.get(label.toLowerCase()); 
	}
	
	private final String[] labels; 
	private final Class<? extends CommandExecutor> executorClass; 
	
	private CommandType(Class<? extends CommandExecutor> executorClass, String... label) {
		this.executorClass = executorClass; 
		this.labels = label; 
	}

	public String[] getLabels() {
		return this.labels;
	}

	public Class<? extends CommandExecutor> getExecutorClass() {
		return this.executorClass;
	}
	
	@SuppressWarnings("unchecked")
	public CommandExecutor construct(ChatServer server, Player player, String label, String command, String rawInput) throws CommandExecutorInstantiationException {
		try {
			return ((Constructor<CommandExecutor>)this.executorClass.getConstructor(ChatServer.class, Player.class, String.class, String.class, String.class)).newInstance(server, player, label, command, rawInput); 
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NullPointerException e) {
			throw new CommandExecutorInstantiationException(e); 
		} 
	}

}
