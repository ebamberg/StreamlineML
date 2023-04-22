package de.ebamberg.streamline.ml.modules.huggingface;

import de.ebamberg.streamline.ml.api.results.TokenPropability;

public class HuggingFaceCloud {

	public static HuggingFaceCloudAdapter<String,TokenPropability[]> xlmRobertaBase=new HuggingFaceCloudXlmRobertaBase();

}
