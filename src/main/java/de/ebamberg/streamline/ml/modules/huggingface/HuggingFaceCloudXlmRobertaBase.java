package de.ebamberg.streamline.ml.modules.huggingface;

import java.net.http.HttpResponse;

import de.ebamberg.streamline.ml.api.results.TokenPropability;

public class HuggingFaceCloudXlmRobertaBase extends HuggingFaceCloudAdapter<String, TokenPropability[]> {

	@Override
	protected String inferencePath() {
		return "models/xlm-roberta-base";
	}

	@Override
	protected Object buildInputOject(String input) {
		return new SimpleTextInput(input);
	}

	@Override
	protected TokenPropability[] transformOutput(HttpResponse<String> response) {
	//	return response.body();
		return jsonToOutput(response.body(), TokenPropability[].class);
	}

}
