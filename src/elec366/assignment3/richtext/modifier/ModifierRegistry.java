package elec366.assignment3.richtext.modifier;

import java.util.HashMap;
import java.util.Map;

public class ModifierRegistry {

	public static final char prefixChar = '&'; 
	
	private static Map<Character, IBaseModifier> modifierMap; 
	
	static {
		modifierMap = new HashMap<>(); 
		for(IBaseModifier m : FormatModifier.values()) 
			modifierMap.put(m.getCode(), m); 
		for(IBaseModifier m : BuiltinColorModifier.values())
			modifierMap.put(m.getCode(), m); 
		for(IBaseModifier m : CustomColorModifier.values())
			modifierMap.put(m.getCode(), m); 
	}
	
	public static IBaseModifier get(Character code) {
		IBaseModifier object = modifierMap.get(code); 
		if(object == null) return null; 
		if(object instanceof CustomColorModifier) 
			return new CustomColorModifier(); 
		return object; 
	}

}
