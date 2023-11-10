package elec366.assignment3.server;

public class ServerResources {

	public static String LOGIN_KICK_PREFIX = "%s lost connection: "; 
	public static String LOGIN_NAME_CONFLICT = "User with name %s is already online!"; 
	public static String LOGIN_NAME_LENGTH_VIOLATION = "Your username is too short or too long!"; 
	public static String LOGIN_NAME_INVALID_CHARACTER = "Your username contains invalid characters. Allowed: letters, numbers, underscore."; 
	
	public static String JOIN_MESSAGE = "%s has connected."; 
	public static String JOIN_WELCOME1 = "Welcome! There are %d users online. Current time: %s"; 
	public static String JOIN_WELCOME2 = "Online: "; 

	public static String QUIT_MESSAGE = "%s has disconnected.";
	
	public static String CHAT_FORMAT = "<%s> %s"; 
	
	public static String COMMAND_ISSUE = "%s issued server command: %s"; 
	
	public static String COMMAND_UNKNOWN = "Unknown command. Try /help."; 
	
	public static String COMMAND_HELP = "Help topic (todo)"; // TODO: write help; 
	
	public static String COMMAND_STOP_PASSWORD = "You need the stop password to stop the server!"; 
	public static String COMMAND_STOP_SHUTDOWN = "Stopping server..."; 
	
	public static String COMMAND_LIST_REPLY1 = "There are %d users online: "; 
	
	public static String COMMAND_TELL_HELP = "Usage: /tell username message"; 
	public static String COMMAND_TELL_OFFLINE = "The player is not online."; 
	public static String COMMAND_TELL_SELF = "You cannot send messages to yourself!"; 
	public static String COMMAND_TELL_MSG = "[%s -> %s] %s"; 
	public static String COMMAND_TELL_MSG_YOU = "You"; 
	
}
