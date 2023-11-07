package elec366.assignment1.client.richtext;

import java.util.HashMap;
import java.util.Map;

public interface RichTextModifier {
	
	public static class Registry {
		
		private static Map<Character, RichTextModifier> modifierMap; 
		
		static {
			Registry.modifierMap = new HashMap<>(); 
			
			for(RichTextModifier m : ColorModifier.BuiltinColorModifier.values()) 
				Registry.modifierMap.put(m.getCode(), m); 
			
			for(RichTextModifier m : FormatModifier.values()) 
				Registry.modifierMap.put(m.getCode(), m); 

			ColorModifier.CustomColorModifier customColorModifier = new ColorModifier.CustomColorModifier(); 
			Registry.modifierMap.put(customColorModifier.getCode(), customColorModifier); 
		}
		
		public static RichTextModifier get(Character code) {
			RichTextModifier object = Registry.modifierMap.get(code);
			if(object instanceof ColorModifier.CustomColorModifier) return new ColorModifier.CustomColorModifier(); 
			return object; 
		}
		
	}
	
	// Modifier sources:
	// https://www.digminecraft.com/lists/color_list_pc.php
	
	public interface ColorModifier extends RichTextModifier {
	
		// Built-in color modifiers: 16 different colors, such as &a
		public static enum BuiltinColorModifier implements ColorModifier {
			DARK_RED	('4', 0xAA0000), 
			RED			('c', 0xFF5555), 
			GOLD		('6', 0xFFAA00), 
			YELLOW		('e', 0xFFFF55), 
			DARK_GREEM	('2', 0x00AA00), 
			GREEN		('a', 0x55FF55), 
			AQUA		('b', 0x55FFFF), 
			DARK_AQUA	('3', 0x00AAAA), 
			DARK_BLUE	('1', 0x0000AA), 
			BLUE		('9', 0x5555FF), 
			LIGHT_PURPLE('d', 0xFF55FF), 
			DARK_PURPLE	('5', 0xAA00AA), 
			WHITE		('f', 0xFFFFFF), 
			GRAY		('7', 0xAAAAAA), 
			DARK_GRAY	('5', 0x555555), 
			BLACK		('0', 0x000000), 
			; 
			
			private final char code; 
			private final int color; 
			
			BuiltinColorModifier(char code, int color) {
				this.code = code; 
				this.color = color; 
			}
			
			@Override
			public char getCode() {
				return this.code; 
			}
			
			@Override
			public int getColor() {
				return this.color; 
			}
			
		}
		
		// Custom color modifier: custom RGB value, such as &x23f0c6
		public static class CustomColorModifier implements ColorModifier {
			
			private static final char CODE = 'x';

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
			
		}
		
		public int getColor(); 
		
	}
	
	// Built-in format modifiers: supporting four different formats
	public static enum FormatModifier implements RichTextModifier {
		BOLD			('l'), 
		STRIKETHROUGH	('m'), 
		UNDERLINE		('n'), 
		ITALIC			('o'), 
		RESET			('r'), 
		;
		
		private final char code; 
		
		FormatModifier(char code) {
			this.code = code; 
		}
		
		@Override
		public char getCode() {
			return this.code; 
		}
		
	}
	
	public char getCode(); 
	
}
