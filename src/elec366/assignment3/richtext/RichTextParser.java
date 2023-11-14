package elec366.assignment3.richtext;

/*
 * This class is common to server and client. 
 * 
 * A stateful parser that converts a string with embedded &-code modifiers
 * into rich text format. 
 * The parser treats any malformat or ambiguity as plain text 
 * and does not throw exceptions nor fail. 
 * 
 * The rich text parser is implemented as a static method. 
 * The rich text parser object is used internally, with a private constructor. 
 */
import java.util.ArrayList;

import elec366.assignment3.richtext.modifier.CustomColorModifier;
import elec366.assignment3.richtext.modifier.FormatModifier;
import elec366.assignment3.richtext.modifier.IBaseModifier;
import elec366.assignment3.richtext.modifier.IColorModifier;
import elec366.assignment3.richtext.modifier.ModifierRegistry;
import elec366.assignment3.util.Either;
import elec366.assignment3.util.StringCharIterator;

public class RichTextParser {
	
	private final StringCharIterator iterator; 
	private boolean onHold; 
	private Either<String, IBaseModifier> holdObject; 
	
	private RichTextParser(String string) {
		this.iterator = new StringCharIterator(string); 
		this.onHold = false; 
		this.holdObject = null; 
	}
	
	private Either<String, IBaseModifier> getCore() {
		if(!this.iterator.hasNext()) return null; 
		char ch = this.iterator.next(); 
		if(ch != ModifierRegistry.prefixChar || !this.iterator.hasNext()) return Either.ofFirst(String.valueOf(ch)); 
		char ch1 = this.iterator.next(); 
		if(ch1 == ModifierRegistry.prefixChar) return Either.ofFirst(String.valueOf(ch1)); 
		IBaseModifier modifier = ModifierRegistry.get(ch1); 
		if(modifier == null) return Either.ofFirst(new String(new char[] {ModifierRegistry.prefixChar, ch1})); 
		if(!(modifier instanceof CustomColorModifier)) return Either.ofSecond(modifier); 
		CustomColorModifier ccm = customColorModifierParser((CustomColorModifier)modifier); 
		if(ccm != null) return Either.ofSecond(ccm);
		return Either.ofFirst(new String(new char[] {ModifierRegistry.prefixChar, ch1})); 
	}
	
	private CustomColorModifier customColorModifierParser(CustomColorModifier ccm) {
		if(!this.iterator.hasNext(6)) return null; 
		String hexCode = this.iterator.next(6); 
		if(!hexCode.matches("[0-9a-fA-F]{6}")) return null; 
		ccm.setColor(Integer.parseInt(hexCode, 16));
		return ccm; 
	}
	
	private Either<String, IBaseModifier> get() {
		if(this.onHold) {
			Either<String, IBaseModifier> object = this.holdObject; 
			this.holdObject = null; 
			this.onHold = false; 
			return object; 
		}
		this.holdObject = this.getCore(); 
		return this.holdObject; 
	}
	
	private void hold() {
		this.onHold = true; 
	}
	
	private static RichTextSegment parseSegment(RichTextParser parser, RichTextSegment oldRts) {
		boolean stickyReset = false; 
		IColorModifier cm = null; 
		ArrayList<FormatModifier> fm = new ArrayList<>(); 
		while(true) {
			Either<String, IBaseModifier> object = parser.get(); 
			if(object == null) return null; 
			if(object.hasFirst()) {
				parser.hold(); 
				break; 
			}
			IBaseModifier bm = object.getSecond(); 
			if(bm instanceof IColorModifier) {
				cm = (IColorModifier)bm; 
				stickyReset = true; 
			}
			if(bm instanceof FormatModifier) {
				FormatModifier fm0 = (FormatModifier)bm; 
				fm.add(fm0); 
				if(fm0 == FormatModifier.RESET) stickyReset = false; 
			}
		}
		if(!stickyReset && oldRts != null) {
			if(cm == null) cm = oldRts.getColorIfPresent(); 
			for(FormatModifier fm0 : oldRts.getFormats()) {
				if(fm.contains(fm0)) continue; 
				fm.add(fm0); 
			}
		}
		StringBuilder sb = new StringBuilder(); 
		while(true) {
			Either<String, IBaseModifier> object = parser.get(); 
			if(object == null) break; 
			if(object.hasSecond()) {
				parser.hold(); 
				break; 
			}
			sb.append(object.getFirst()); 
		}
		return new RichTextSegment(cm, fm.toArray(new FormatModifier[0]), sb.toString()); 
	}
	
	public static RichText parse(String string) {
		RichTextParser parser = new RichTextParser(string); 
		RichText text = new RichText(); 
		RichTextSegment oldRts = null; 
		while(true) {
			RichTextSegment rts = parseSegment(parser, oldRts); 
			if(rts == null) break;
			oldRts = rts; 
			text.add(rts); 
		}
		return text; 
	}

}
