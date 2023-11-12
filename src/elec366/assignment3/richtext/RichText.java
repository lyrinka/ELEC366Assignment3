package elec366.assignment3.richtext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import elec366.assignment3.richtext.modifier.FormatModifier;

public class RichText {
	
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
		return this.toString(RichTextSegment::toString); 
	}
	
	public String toDetailedString() {
		return this.toString(RichTextSegment::toDetailedString); 
	}
	
	public String toConsoleString() {
		return this.toString(RichTextSegment::toConsoleString) + FormatModifier.RESET.getEscapeCode();  
	}
	
	public String toString(Function<RichTextSegment, String> f) {
		return this.segments.stream()
			.map(f)
			.reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append)
			.toString(); 
	}
	
}
