package de.ebamberg.streamline.ml.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CharacterBasedDictionaryTest {

	@Test
	public void fromStringInputSize() {
		var textData="abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz ";

		var dict=CharacterBasedDictionary.fromString(textData);
		assertEquals(27,dict.size());
		
	}
	
	@Test
	public void accessByKeyIndex() {
		var textData="a";

		var dict=CharacterBasedDictionary.fromString(textData);
		assertEquals('a',dict.elementOf(0));
		
		assertEquals(0,dict.indexOf('a'));
	}
	
}
