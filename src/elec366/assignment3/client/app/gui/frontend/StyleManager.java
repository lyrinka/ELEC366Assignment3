package elec366.assignment3.client.app.gui.frontend;

import java.awt.Color;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import elec366.assignment3.richtext.RichText;
import elec366.assignment3.richtext.RichTextSegment;
import elec366.assignment3.richtext.modifier.BuiltinColorModifier;
import elec366.assignment3.richtext.modifier.FormatModifier;
import elec366.assignment3.richtext.modifier.IBaseModifier;
import elec366.assignment3.richtext.modifier.IColorModifier;
import elec366.assignment3.util.SingleLineSanitizer;

/*
 * This class applies to only client.
 * 
 * The style manager provides utility methods for rendering pre-compiled rich text in a Swing JTextPane. 
 */
public class StyleManager {
	
	public void addNewLine(StyledDocument doc) {
		try {
			doc.insertString(doc.getLength(), "\n", null);
		} catch (BadLocationException ignored) {
			
		}
	}
	
	public void addText(StyledDocument doc, String text) {
		try {
			doc.insertString(doc.getLength(), SingleLineSanitizer.sanitize(text), null);
		} catch (BadLocationException ignored) {
			
		}
	}
	
	public void addRichText(StyledDocument doc, RichText text) {
		for(RichTextSegment seg : text.getSegments()) 
			this.addRichTextSegment(doc, seg);
	}
	
	public void addRichTextSegment(StyledDocument doc, RichTextSegment seg) {
		if(seg.getColorIfPresent() == null && seg.getFormats().length == 0) {
			this.addText(doc, seg.getText());
			return; 
		}
		Style style = doc.addStyle(null, null); 
		IColorModifier m1 = seg.getColorIfPresent(); 
		if(m1 != null) this.setStyle(style, m1);
		FormatModifier[] m2 = seg.getFormats(); 
		this.setStyle(style, m2);
		try {
			doc.insertString(doc.getLength(), SingleLineSanitizer.sanitize(seg.getText()), style);
		} catch (BadLocationException e) {
			
		} 
	}
	
	public void setStyle(Style style, IBaseModifier[] modifiers) {
		for(IBaseModifier modifier : modifiers) 
			this.setStyle(style, modifier); 
	}
	
	public void setStyle(Style style, IBaseModifier modifier) {
		if(modifier instanceof IColorModifier) {
			IColorModifier modifier0 = (IColorModifier)modifier; 
			if(modifier0 == BuiltinColorModifier.WHITE)
				modifier0 = BuiltinColorModifier.BLACK; 
			StyleConstants.setForeground(style, new Color(modifier0.getColor())); 
		}
		if(modifier instanceof FormatModifier) {
			FormatModifier modifier0 = (FormatModifier)modifier; 
			switch(modifier0) {
				default: 
				case RESET: {
					break;	
				}
				case BOLD: {
					StyleConstants.setBold(style, true);
					break;
				}
				case ITALIC: {
					StyleConstants.setItalic(style, true);
					break;
				}
				case STRIKETHROUGH: {
					StyleConstants.setStrikeThrough(style, true);
					break;
				}
				case UNDERLINE: {
					StyleConstants.setUnderline(style, true);
					break; 
				}
			}
		}
	}

}
