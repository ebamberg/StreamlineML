package de.ebamberg.streamline.nlp.streamlineText;

import java.util.Arrays;

import de.ebamberg.streamline.ml.data.pipeline.Pipeline;
import de.ebamberg.streamline.ml.data.reader.CSVDataset;
import de.ebamberg.streamline.ml.text.StreamDictionary;

public class IntentionModelDemo {

	public static void main(String[] args) throws Exception {
		
		var dataset=CSVDataset.fromURL(IntentionModelDemo.class.getResource("/intentions.csv"));
		
		var p=Pipeline.fromStream(dataset.stream())
				.log()
				.execute();
		

		// build dictionary for class
		var classes=StreamDictionary.fromStream(dataset.stream().map(r->r.getValue("intention")));
		
		var words=StreamDictionary.fromStream(dataset
												.stream()
												.flatMap(r-> { 
															var sentence=(String)r.getValue("sample");
															return Arrays.stream(sentence.toLowerCase().split(" "));
														})
												
											);
		
		classes.show();
		words.show();
		dataset.stream().forEach(System.out::println);
		
		
		
	}
	
	
}
