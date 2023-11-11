package elec366.assignment3.richtext.modifier;

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
		return (char)0x18 + this.escapeSeq; 
	}
	
	@Override
	public String toDetailedString() {
		return "<" + super.toString().toLowerCase() + ">"; 
	}
	
}
