package de.ebamberg.streamline.ml.modules.huggingface;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import de.ebamberg.streamline.ml.modules.huggingface.HuggingFaceCloudAdapter.ModelLoadingResponse;

public class HuggingFaceCloudAdapterTest {

	// @Test
	//TODO this call needs an API TOKEN. So we need to mock the endpoint
	public void test() throws Exception {
		var adapter=HuggingFaceCloud.xlmRobertaBase;
		var response=adapter.predict("The answer of the universe is <mask>.");
		System.out.println(response);
		assertNotNull(response);
	}

	@Test
	public void testParseModelisLoadingResponse() {
		var resp="{\"error\":\"Model xlm-roberta-base is currently loading\",\"estimated_time\":20.0}";
		var gson=new Gson();
		var errorResp=gson.fromJson(resp, ModelLoadingResponse.class);
		assertEquals("Model xlm-roberta-base is currently loading",errorResp.getError());

		assertEquals(20f,errorResp.getEstimatedTime(),0.1f);
		
	}
	
}
