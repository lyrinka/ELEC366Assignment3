package elec366.assignment3.richtext.modifier;

public enum BuiltinColorModifier implements IColorModifier {
	
	BLACK		('0', 0x000000, "[30m"), 
	DARK_BLUE	('1', 0x0000AA, "[34m"), 
	DARK_GREEN	('2', 0x00AA00, "[32m"), 
	DARK_AQUA	('3', 0x00AAAA, "[36m"), 
	DARK_RED	('4', 0xAA0000, "[31m"), 
	DARK_PURPLE	('5', 0xAA00AA, "[35m"), 
	GOLD		('6', 0xFFAA00, "[33m"), 
	GRAY		('7', 0xAAAAAA, "[37m"), 
	DARK_GRAY	('8', 0x555555, "[90m"), 
	BLUE		('9', 0x5555FF, "[94m"), 
	GREEN		('a', 0x55FF55, "[92m"), 
	AQUA		('b', 0x55FFFF, "[96m"), 
	RED			('c', 0xFF5555, "[91m"), 
	LIGHT_PURPLE('d', 0xFF55FF, "[95m"), 	
	YELLOW		('e', 0xFFFF55, "[93m"), 
	WHITE		('f', 0xFFFFFF, "[97m"), 
	; 
	
	private final char code; 
	private final int color; 
	private final String escapeSeq; 
	
	BuiltinColorModifier(char code, int color, String escape) {
		this.code = code; 
		this.color = color; 
		this.escapeSeq = escape; 
	}
	
	@Override
	public char getCode() {
		return this.code; 
	}
	
	@Override
	public int getColor() {
		return this.color; 
	}
	
	@Override
	public String getEscapeCode() {
		return IBaseModifier.ESC + this.escapeSeq; 
	}
	
	@Override
	public String toDetailedString() {
		return "<color:" + this.toString().toLowerCase() + ">"; 
	}
	
}
