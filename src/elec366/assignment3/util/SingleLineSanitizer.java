package elec366.assignment3.util;

public class SingleLineSanitizer {

	public static String sanitize(String message) {
		message = message.replaceAll("(?:[\r\n]+|\t)", " "); 
		message = message.replaceAll("[\\p{Cntrl}]", ""); 
		message = message.trim(); 
		return message; 
	}

}
