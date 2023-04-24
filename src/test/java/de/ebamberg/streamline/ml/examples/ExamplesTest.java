package de.ebamberg.streamline.ml.examples;

import org.junit.jupiter.api.Test;

public class ExamplesTest {


	
	@Test
	public void testExample1() throws Exception {
		SentenceMaskInferWithCloudServiceExample.main(new String[] {} );
	}

	@Test
	public void testExample2() throws Exception {
		PredictNextCharacter.main(new String[] {} );
	}
	
	@Test
	public void testExample3() throws Exception {
		SimpleClassification.main(new String[] {} );
	}
	
}
