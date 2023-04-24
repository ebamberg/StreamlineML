package de.ebamberg.streamline.ml.modules.huggingface;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class HuggingFaceCloudAllMiniLM2Text2VecTest {

	@Test
	public void testAdapter() {
		var adapter=new HuggingFaceCloudAllMiniLM2Text2Vec();
		float[] vector=adapter.predict("Hello world!");
		assertEquals(384,vector.length);
		
	}

}
