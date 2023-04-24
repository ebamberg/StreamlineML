package de.ebamberg.streamline.ml.modules.huggingface;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.ebamberg.streamline.ml.api.results.TokenPropability;

public class HuggingFaceCloudXlmRobertaBaseTest {

	@Test
	public void testAdapter() {
		var adapter=new HuggingFaceCloudXlmRobertaBase();
		TokenPropability[] result=adapter.predict("This is not my cup of <mask>!");
		assertEquals(5,result.length);
		
	}

}
