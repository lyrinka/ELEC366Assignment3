package elec366.assignment3.server;

public class ServerResources {

	public static String LOGIN_KICK_PREFIX = "&e%s lost connection: "; 
	public static String LOGIN_NAME_CONFLICT = "&c&lUser with name %s is already online!"; 
	public static String LOGIN_NAME_LENGTH_VIOLATION = "&c&lYour username is too short or too long!"; 
	public static String LOGIN_NAME_INVALID_CHARACTER = "&c&lYour username contains invalid characters. &aAllowed: letters, numbers, underscore."; 
	
	public static String JOIN_MESSAGE = "&e%s has connected."; 
	public static String JOIN_WELCOME1 = "&a&lWelcome! There are %d users online. Current time: %s"; 
	public static String JOIN_WELCOME2 = "&aOnline: "; 

	public static String QUIT_MESSAGE = "&e%s has disconnected.";
	
	public static String OVERSIZE_MESSAGE = "&cYour message is too long!"; 
	public static String OVERSIZE_MESSAGE_LOG = "&cUser %s sent oversized message (length=%d)."; 
	
	public static String CHAT_FORMAT = "&7<&b%s&7> &r%s"; 
	
	public static String COMMAND_ISSUE = "&7%s issued server command: %s"; 
	
	public static String COMMAND_UNKNOWN = "Unknown command. Try /help."; 
	
	public static String COMMAND_HELP = "Help topic (todo)"; // TODO: write help; 
	
	public static String COMMAND_STOP_PASSWORD = "&cYou need the stop password to stop the server!"; 
	public static String COMMAND_STOP_SHUTDOWN = "&bStopping server..."; 
	
	public static String COMMAND_LIST_REPLY1 = "&aThere are %d users online: "; 
	
	public static String COMMAND_TELL_HELP = "Usage: /tell username message"; 
	public static String COMMAND_TELL_OFFLINE = "The player is not online."; 
	public static String COMMAND_TELL_SELF = "You cannot send messages to yourself!"; 
	public static String COMMAND_TELL_MSG = "&b[%s &b-> %s&b]&r %s"; 
	public static String COMMAND_TELL_MSG_YOU = "&cYou&r"; 
	
}
