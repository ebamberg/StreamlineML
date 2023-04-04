package de.ebamberg.streamline.ml.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ai.djl.ndarray.NDArray;

public class OneHotEncoderTest {

	@Test
	@Disabled ("old test we are replacing this with a Pipeline approach")
	public void testWithCharacterDictionary() throws Exception {
		var textData = "abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz ";
		var dict = CharacterBasedDictionary.fromString(textData);

		try ( InputEncoder<Character> encoder = OneHotEncoder.fromDictionary(dict) ) {
			List<NDArray> onehotencoded = textData
											.chars()
											.mapToObj(e -> (char) e)
											.map(encoder::encode)
											.toList();
			
			IntStream.range(0, onehotencoded.size()).forEach(i -> {
				NDArray row = onehotencoded.get(i);
				assertEquals(dict.size(), row.size(0));
				assertEquals(1f, row.getFloat(i % dict.size()));
			});
			onehotencoded.forEach(arr -> System.out.println(Arrays.toString(arr.toFloatArray())));
		}
	}

}
