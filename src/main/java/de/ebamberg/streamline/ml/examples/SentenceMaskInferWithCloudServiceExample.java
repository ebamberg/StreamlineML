package de.ebamberg.streamline.ml.examples;

import de.ebamberg.streamline.ml.data.reader.TextualDataset;
import de.ebamberg.streamline.ml.modules.huggingface.HuggingFaceCloud;

public class SentenceMaskInferWithCloudServiceExample {


	public static void main(String[] args) {
		TextualDataset.fromResource("/simpleMaskedSentences.txt")
			.read()
			.map(r->(String)r.getValue("lines"))
			.log()
			.predict(HuggingFaceCloud.xlmRobertaBase)
			.log()
			.execute();
			
	}

}
