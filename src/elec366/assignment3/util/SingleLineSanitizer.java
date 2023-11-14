package elec366.assignment3.util;

/*
 * Sanitizes the input string by removing all ASCII control characters
 * and limiting the text to a single line (replacing EOLs with spaces). 
 */
public class SingleLineSanitizer {

	public static String sanitize(String message) {
		message = message.replaceAll("(?:[\r\n]+|\t)", " "); 
		message = message.replaceAll("[\\p{Cntrl}]", ""); 
		message = message.trim(); 
		return message; 
	}

}
