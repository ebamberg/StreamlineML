package de.ebamberg.streamline.ml.text;

public class CharacterBasedDictionary extends StreamDictionary<Character> {
	

	private CharacterBasedDictionary(String input) {	
		fromStream(input.chars().mapToObj(i->(char)i));
	}


	
	public static CharacterBasedDictionary fromString(String input) {
		return new CharacterBasedDictionary(input);
	}
	
}
