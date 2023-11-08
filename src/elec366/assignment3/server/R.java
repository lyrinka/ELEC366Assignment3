package elec366.assignment3.server;

public class R {
	
	public static final int DEFAULT_PORT = 14569; 
	
	public static final int USERNAME_LEN_MIN = 3; 
	public static final int USERNAME_LEN_MAX = 15; 

	public static final String USERNAME_CONFLICT = "A user with the same username is logged in!"; 
	
	public static final String USERNAME_TOO_SHORT = "Your username is too short or too long!"; 
	
	public static final String PLAYER_CONNECT = "%s has connected.";
	
	public static final String PLAYER_DISCONNECT = "%s has disconnected.";
	
	public static final String GLOBAL_CHAT = "<%s> %s"; 
	
	public static final String PRIVATE_CHAT_HELP = "Example: /tell username content"; 
	public static final String PRIVATE_CHAT_NOT_ONLINE = "The user is not online."; 
	public static final String PRIVATE_CHAT1 = "%s whispers to you: %s";
	public static final String PRIVATE_CHAT2 = "You whisper to %s: %s"; 
	public static final String PRIVATE_CHAT3 = "%s whispered to %s: %s"; 
	
	public static final String UNKNOWN_COMMAND = "Unknown command."; 
	
	public static final String LIST_COMMAND = "There are %d users online:"; 

}
