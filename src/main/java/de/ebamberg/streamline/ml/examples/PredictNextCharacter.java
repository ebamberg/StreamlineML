package de.ebamberg.streamline.ml.examples;

import java.util.Arrays;
import java.util.List;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.types.Shape;
import de.ebamberg.streamline.ml.layer.rnn.RNNLayer;
import de.ebamberg.streamline.ml.text.CharacterBasedDictionary;
import de.ebamberg.streamline.ml.text.InputEncoder;
import de.ebamberg.streamline.ml.text.OneHotEncoder;

public class PredictNextCharacter {

	public static void main(String[] args) throws Exception {
		var textData="abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz ";

		var dictionary=CharacterBasedDictionary.fromString(textData);
		System.out.printf("**** Input Data ****\n\n\tcharacters in input %d\n\tunique characters in string %d\n\n\n", textData.length(), dictionary.size());
		
		try (InputEncoder<Character> encoder=OneHotEncoder.fromDictionary(dictionary)) {
			List<NDArray> onehotencoded=textData
										.chars()
										.mapToObj(e->(char)e)
										.map(encoder::encode)
										.toList();
			
			System.out.println("**** One Hot encoded input data ****");
			onehotencoded.forEach(arr->System.out.println(Arrays.toString(arr.toFloatArray())));
			var layer1=new RNNLayer(encoder.getManager(),new Shape(1,dictionary.size()));
			
		}
	}

}
