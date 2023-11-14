package elec366.assignment3.server;

/*
 * This class applies to only server.
 * 
 * All server strings are organized here for easier access and internationalization support.
 */
public class ServerResources {

	public static class LOGIN {
		
		public static String CONSOLE_PREFIX = "&4%s lost connection: "; 
		public static String NAME_CONFLICT = "&4&lUser with name &8%s &4&l is already online!"; 
		public static String NAME_TOO_SHORT = "&4&lYour username is too short (at least %d characters)."; 
		public static String NAME_TOO_LONG = "&4&lYour username is too long (at most %d characters).";
		public static String INVALID_CHARACTER = "&4&lYour username contains invalid characters. &2Allowed: letters, numbers, underscore."; 
		
	}
	
	public static class JOIN {
		
		public static String MESSAGE = "&8%s has connected.";
		public static String WELCOME1 = "&2&lWelcome! There are %d users online."; 
		public static String WELCOME2 = "Current time: %s"; 
		public static String WELCOME3 = "&2Online: &8%s"; 
		
	}
	
	public static class QUIT {
		
		public static String MESSAGE = "&8%s has disconnected.";
		
	}
	
	public static class CHAT {
		
		public static String OVERSIZED_LOG = "&4User %s sent oversized message (length=%d)."; 
		public static String OVERSIZED = "&4Your message is too long!"; 
		
		public static String FORMAT = "&8<&3%s&7> &r%s"; 
		
	}
	
	public static class COMMAND {
		
		public static String ISSUE_LOG = "&8%s issued server command: %s"; 
		
		public static String UNKNOWN = "&8Unknown command. Try /help."; 
		public static String EXCEPTION = "&4&lAn internal error has occured. Please contact server administorators.";
		public static String MALFORMAT = "&8Unrecognized command format. Try /help %s."; 
		
	}
	
	public static class COMMAND_HELP {
		
		public static String HELP = "General help topic (todo)"; 
		
		public static String TOPIC_ON_COMMAND1 = "&8Command &3&l%s&8: &r%s"; 
		public static String TOPIC_ON_COMMAND2 = "&8 .. &r%s"; 
		public static String TOPIC_DEFAULT = "&8No help for command &3&l%s&8."; 
		
	}
	
	public static class COMMAND_STOP {
		
		public static String HELP1 = "Usage: /stop <stop-password>"; 
		public static String HELP2 = "You need to provide the password if the server administrator has configured a stop password. "; 
		
		public static String SHUTTING_DOWN = "&bStopping server..."; 
		public static String PASSWORD_REQUIRED = "&cYou need the stop password to stop the server!"; 
		
	}
	
	public static class COMMAND_LIST {
		
		public static String HELP = "Usage: /list"; 
		
		public static String MESSAGE1 = "&2There are %d users online: "; 
		public static String MESSAGE2 = "&9%s"; 
		
	}
	
	public static class COMMAND_TELL {
		
		public static String HELP = "Usage: /tell <username> <message>"; 
		
		public static String OFFLINE = "The player is not online."; 
		public static String SELF = "The player is not online."; 
		public static String MESSAGE = "&8[%s&8 -> %s&8]&r %s"; 
		public static String FORMAT_OTHERS = "&1%s"; 
		public static String FORMAT_YOU = "&4You"; 
		
	}
	
}
