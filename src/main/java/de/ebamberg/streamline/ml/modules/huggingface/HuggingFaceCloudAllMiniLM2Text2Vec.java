package de.ebamberg.streamline.ml.modules.huggingface;

import java.net.http.HttpResponse;

import de.ebamberg.streamline.ml.api.results.TokenPropability;

public class HuggingFaceCloudAllMiniLM2Text2Vec extends HuggingFaceCloudAdapter<String, float[]> {

	@Override
	protected String inferencePath() {
		return "pipeline/feature-extraction/sentence-transformers/all-MiniLM-L6-v2";
	}

	@Override
	protected Object buildInputOject(String input) {
		return new SimpleTextInput(input);
	}

	@Override
	protected float[] transformOutput(HttpResponse<String> response) {
		return gson.fromJson(response.body(),float[].class);
	}

}
