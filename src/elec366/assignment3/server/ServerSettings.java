package elec366.assignment3.server;

/*
 * This class applies to only server.
 * 
 * All server settings are organized here
 */
public class ServerSettings {

	// Length: 3 to 15 characters
	public static int USERNAME_MIN_LEN = 3; 
	public static int USERNAME_MAX_LEN = 15; 
	
	// Allowed characters: numbers, letters, underscore
	public static String USERNAME_PATTERN = "[0-9a-zA-Z_]+"; 

	// By default, no stop password.
	// The launcher can change this before starting the server.
	public static String STOP_PASSWORD = ""; 
	
	// Maximum length of a chat message to avoid malicious users crashing clients.
	public static int MESSAGE_LENGTH_MAX = 1000;
	
}
