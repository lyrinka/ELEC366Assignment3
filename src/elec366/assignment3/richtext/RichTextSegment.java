package elec366.assignment3.richtext;

import java.util.function.Function;

import elec366.assignment3.richtext.modifier.BuiltinColorModifier;
import elec366.assignment3.richtext.modifier.FormatModifier;
import elec366.assignment3.richtext.modifier.IBaseModifier;
import elec366.assignment3.richtext.modifier.IColorModifier;

public class RichTextSegment {
	
	private static final IColorModifier DEFAULT_COLOR_MODIFIER = BuiltinColorModifier.BLACK; 
	
	private final IColorModifier colorModifier; 
	private final FormatModifier[] formatModifier; 
	private final String text;
	
	public RichTextSegment(IColorModifier colorModifier, FormatModifier[] formatModifier, String text) {
		this.colorModifier = colorModifier;
		this.formatModifier = formatModifier != null ? formatModifier : new FormatModifier[0];
		this.text = text;
	}
	
	public RichTextSegment(String text) {
		this(null, null, text); 
	}
	
	public IColorModifier getColorIfPresent() {
		return this.colorModifier; 
	}
	
	public IColorModifier getColor() {
		return this.getColorOrDefault(DEFAULT_COLOR_MODIFIER); 
	}
	
	public IColorModifier getColorOrDefault(IColorModifier defaultModifier) {
		if(this.colorModifier != null) return this.colorModifier; 
		return defaultModifier; 
	}
	
	public FormatModifier[] getFormats() {
		return this.formatModifier; 
	}
	
	public String getText() {
		return this.text; 
	}
	
	@Override
	public String toString() {
		return this.text; 
	}
	
	public String toDetailedString() {
		return this.toString(IBaseModifier::toDetailedString); 
	}
	
	public String toConsoleString() {
		// TODO: console format reset?
		return this.toString(IBaseModifier::getEscapeCode); 
	}
	
	private String toString(Function<IBaseModifier, String> f) {
		StringBuilder sb = new StringBuilder(); 
		if(this.colorModifier != null) sb.append(f.apply(this.colorModifier)); 
		for(FormatModifier fm : this.formatModifier) sb.append(f.apply(fm)); 
		sb.append(this.text); 
		return sb.toString(); 
	}
	
}
