package de.ebamberg.streamline.ml.text;

public class StringDictionary extends StreamDictionary<Character> {
	

	private StringDictionary(String input) {	
		fromStream(input.chars().mapToObj(i->(char)i));
	}


	
	public static StringDictionary fromString(String input) {
		return new StringDictionary(input);
	}
	
}
