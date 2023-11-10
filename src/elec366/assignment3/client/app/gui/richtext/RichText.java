package elec366.assignment3.client.app.gui.richtext;

import java.util.ArrayList;
import java.util.List;

public class RichText {
	
	// Rich text object describes text with format.
	// For instance, the user enters these in chat:
	//	"Hello &aJohn, &lhow are you?"
	// This message will be formatted into three Rich text segments by application code:
	// 	Segment 1 has the text "Hello " and format <empty array>. 
	// 	Segment 2 has the text "John, " and format "GREEN". 
	// 	Segment 3 has the text "how are you?" and format "GREEN, BOLD".
	
	// The application will take care of building rich text objects. 
	// The UI simply take the object and append segments in order, with the correct format. 
	
	// The UI appends lines to the text box. Therefore, the UI should remove possible end-of-lines in the text. 
	// The following code can be applied:
	//	string = string.replaceAll("\\r?\\n", " "); 
	// This will replace all end of lines into spaces in a segment text. 
	
	// Text modifiers are split into two categories:
	// Color modifiers specify a color that can be obtained through modifier.getColor(); 
	// Format modifiers specify a font style. It can be used in a switch(modifier) statement or similar.
	// If no color modifier is provided, the UI shall use its default color. In case of a light background color, one can choose black. 
	// If no format modifier is provided, the UI shall use default font settings. 
	// For each segment, multiple modifiers may present. 
	// The UI should use the last color modifier in the array. For instance, "RED, BLUE" will result in BLUE text.  
	// The UI should apply all format modifiers, if possible. For instance, "GREEN, BOLD, UNDERLINE" should make the text rendered GREEN and both BOLD and UNDERLINE.  
	
	// During development, a plain text string of the message can be obtained via richTextObject.toString(); 
	// The string will contain exactly the same text. With the previous example, it will be "Hello John, how are you?". 
	
	public static class RichTextSegment {
		
		private final RichTextModifier[] modifiers; 
		private final String text; 
		
		public RichTextSegment(RichTextModifier[] modifiers, String text) {
			this.modifiers = modifiers; 
			this.text = text; 
		}
		
		public RichTextModifier[] getModifiers() {
			return this.modifiers; 
		}
		
		public String getText() {
			return this.text;
		}
		
		@Override
		public String toString() {
			return this.getText(); 
		}
		
	}
	
	private final List<RichTextSegment> segments; 
	
	public RichText() {
		this.segments = new ArrayList<>(); 
	}
	
	public RichText add(RichTextSegment segment) {
		this.segments.add(segment); 
		return this; 
	}
	
	public List<RichTextSegment> getSegments() {
		return this.segments; 
	}
	
	@Override
	public String toString() {
		return this.segments.stream().reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append).toString(); 
	}
	
}
