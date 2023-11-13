package elec366.assignment3.server;

public class ServerResources {

	public static class LOGIN {
		
		public static String CONSOLE_PREFIX = "&e%s lost connection: "; 
		public static String NAME_CONFLICT = "&c&lUser with name &e%s &c&l is already online!"; 
		public static String NAME_TOO_SHORT = "&c&lYour username is too short (at least %d characters)."; 
		public static String NAME_TOO_LONG = "&c&lYour username is too long (at most %d characters).";
		public static String INVALID_CHARACTER = "&c&lYour username contains invalid characters. &aAllowed: letters, numbers, underscore."; 
		
	}
	
	public static class JOIN {
		
		public static String MESSAGE = "&e%s has connected.";
		public static String WELCOME1 = "&a&lWelcome! There are %d users online."; 
		public static String WELCOME2 = "Current time: %s"; 
		public static String WELCOME3 = "&aOnline: &e%s"; 
		
	}
	
	public static class QUIT {
		
		public static String MESSAGE = "&e%s has disconnected.";
		
	}
	
	public static class CHAT {
		
		public static String OVERSIZED_LOG = "&cUser %s sent oversized message (length=%d)."; 
		public static String OVERSIZED = "&cYour message is too long!"; 
		
		public static String FORMAT = "&7<&b%s&7> &r%s"; 
		
	}
	
	public static class COMMAND {
		
		public static String ISSUE_LOG = "&7%s issued server command: %s"; 
		
		public static String UNKNOWN = "&7Unknown command. Try /help."; 
		public static String EXCEPTION = "&c&lAn internal error has occured. Please contact server administorators.";
		public static String MALFORMAT = "&7Unrecognized command format. Try /help %s."; 
		
	}
	
	public static class COMMAND_HELP {
		
		public static String HELP = "General help topic (todo)"; 
		
		public static String TOPIC_ON_COMMAND1 = "&fCommand &b&l%s&f: &r%s"; 
		public static String TOPIC_ON_COMMAND2 = "&f .. &r%s"; 
		public static String TOPIC_DEFAULT = "&fNo help for command &b&l%s&f."; 
		
	}
	
	public static class COMMAND_STOP {
		
		public static String HELP1 = "Usage: /stop <stop-password>"; 
		public static String HELP2 = "You need to provide the password if the server administrator has configured a stop password. "; 
		
		public static String SHUTTING_DOWN = "&bStopping server..."; 
		public static String PASSWORD_REQUIRED = "&cYou need the stop password to stop the server!"; 
		
	}
	
	public static class COMMAND_LIST {
		
		public static String HELP = "Usage: /list"; 
		
		public static String MESSAGE1 = "&aThere are %d users online: "; 
		public static String MESSAGE2 = "&e%s"; 
		
	}
	
	public static class COMMAND_TELL {
		
		public static String HELP = "Usage: /tell <username> <message>"; 
		
		public static String OFFLINE = "The player is not online."; 
		public static String SELF = "The player is not online."; 
		public static String MESSAGE = "&f[%s&f -> %s&f]&r %s"; 
		public static String FORMAT_OTHERS = "&b%s"; 
		public static String FORMAT_YOU = "&cYou"; 
		
	}
	
}
