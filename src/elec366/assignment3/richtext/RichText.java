package elec366.assignment3.richtext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import elec366.assignment3.richtext.modifier.FormatModifier;

/*
 * This class is common to server and client. 
 * 
 * A rich text object represents a paragraph of text with format (colors, style, etc.). 
 */
public class RichText {
	
	private final List<RichTextSegment> segments; 
	
	public RichText() {
		this.segments = new ArrayList<>(); 
	}
	
	public RichText add(RichTextSegment segment) {
		this.segments.add(segment); 
		return this; 
	}
	
	// Obtain raw segments for custom rendering
	public List<RichTextSegment> getSegments() {
		return this.segments; 
	}
	
	// Obtain plain string without any formatting
	@Override
	public String toString() {
		return this.toString(RichTextSegment::toString); 
	}
	
	// Obtain readable string with embedded readable format modifiers
	public String toDetailedString() {
		return this.toString(RichTextSegment::toDetailedString); 
	}
	
	// Obtain string with Linux terminal escape sequences to display colors and format in a Linux console. 
	public String toConsoleString() {
		return this.toString(RichTextSegment::toConsoleString) + FormatModifier.RESET.getEscapeCode();  
	}
	
	// Helper method that obtains a string by concatenating the strings obtained
	// by applying the given function to every rich text segment. 
	public String toString(Function<RichTextSegment, String> f) {
		return this.segments.stream()
			.map(f)
			.reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append)
			.toString(); 
	}
	
}
