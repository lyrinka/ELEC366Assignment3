package elec366.assignment3.richtext.modifier;

public interface IBaseModifier {
	
	// All formatting codes & console control sequences from: 
	// https://minecraft.fandom.com/wiki/Formatting_codes
	
	public char getCode(); 
	
	default public String getEscapeCode() {
		return ""; 
	}

	default public String toDetailedString() {
		return this.toString(); 
	}
	
}