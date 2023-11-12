package elec366.assignment3.richtext.modifier;

public class CustomColorModifier implements IColorModifier {

	private static final char CODE = 'x';

	public static CustomColorModifier[] values() {
		CustomColorModifier[] array = new CustomColorModifier[1]; 
		array[0] = new CustomColorModifier(); 
		return array; 
	}
	
	private int color; 
	
	public void setColor(int color) {
		this.color = color; 
	}
	
	@Override
	public char getCode() {
		return CODE;
	}

	@Override
	public int getColor() {
		return this.color; 
	}
	
	@Override
	public String getEscapeCode() {
		int r = 0xFF & (this.color >> 24); 
		int g = 0xFF & (this.color >> 16); 
		int b = 0xFF &  this.color; 
		return String.format(IBaseModifier.ESC + "[38;2;%d;%d;%dm", r, g, b);
	}
	
	@Override
	public String toDetailedString() {
		return "<color:" + Integer.toString(this.color, 16)  + ">"; 
	}
	
}
