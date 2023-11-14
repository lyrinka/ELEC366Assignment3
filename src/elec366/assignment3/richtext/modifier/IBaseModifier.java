package elec366.assignment3.richtext.modifier;

/*
 * This class is common to server and client. 
 * 
 * Represents a modifier that can change the rendering of the text following it. 
 * For instance, "&aHELLO" will display a green HELLO. 
 * 
 * Format modifiers can be used in chat, private messages, and server resources (built-in strings). 
 * 
 * Inspirations taken from Minecraft. 
 * All formatting codes & console control sequences from: 
 * https://minecraft.fandom.com/wiki/Formatting_codes
 */
public interface IBaseModifier {
	
	public static char ESC = '\033'; 
	
	public char getCode(); 
	
	default public String getEscapeCode() {
		return ""; 
	}

	default public String toDetailedString() {
		return this.toString(); 
	}
	
}
