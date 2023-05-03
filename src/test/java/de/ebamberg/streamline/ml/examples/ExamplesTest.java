package de.ebamberg.streamline.ml.examples;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
		assertThrows(UnsupportedOperationException.class, ()->SimpleClassification.main(new String[] {} ));
		
	}
	
}
