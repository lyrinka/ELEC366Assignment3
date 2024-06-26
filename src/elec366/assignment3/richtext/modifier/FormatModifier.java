package elec366.assignment3.richtext.modifier;

/*
 * This class is common to server and client. 
 * 
 * Represents a format modifier that can change the style of the text. 
 */
public enum FormatModifier implements IBaseModifier {
	
	BOLD			('l', "[1m"), 
	STRIKETHROUGH	('m', "[9m"), 
	UNDERLINE		('n', "[4m"), 
	ITALIC			('o', "[3m"), 
	RESET			('r', "[0m"), 
	;
	
	private final char code; 
	private final String escapeSeq; 
	
	FormatModifier(char code, String escape) {
		this.code = code; 
		this.escapeSeq = escape; 
	}
	
	@Override
	public char getCode() {
		return this.code; 
	}
	
	@Override
	public String getEscapeCode() {
		return IBaseModifier.ESC + this.escapeSeq; 
	}
	
	@Override
	public String toDetailedString() {
		return "<" + super.toString().toLowerCase() + ">"; 
	}
	
}
