package elec366.assignment3.richtext.modifier;

/*
 * This class is common to server and client. 
 * 
 * Represents a color modifier that can change the color of the text. 
 */
public interface IColorModifier extends IBaseModifier {

	public int getColor(); 
	
}
