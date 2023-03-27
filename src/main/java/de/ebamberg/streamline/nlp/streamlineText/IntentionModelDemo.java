package de.ebamberg.streamline.nlp.streamlineText;

import java.io.IOException;

import de.ebamberg.streamline.ml.data.reader.CSVDataset;

public class IntentionModelDemo {

	public static void main(String[] args) throws Exception {
		
		var dataset=CSVDataset.fromInputStream(IntentionModelDemo.class.getResourceAsStream("/intentions.csv"));
		dataset.stream().forEach(System.out::println);
	}
	
	
}
