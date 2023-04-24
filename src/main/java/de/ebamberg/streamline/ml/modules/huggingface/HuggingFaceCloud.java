package de.ebamberg.streamline.ml.modules.huggingface;

import de.ebamberg.streamline.ml.api.Predictor;
import de.ebamberg.streamline.ml.api.results.TokenPropability;

public class HuggingFaceCloud {

	public static Predictor<String,TokenPropability[]> xlmRobertaBase=new HuggingFaceCloudXlmRobertaBase();

	public static Predictor<String,float[]> text2Vec_AllMiniLM2=new HuggingFaceCloudAllMiniLM2Text2Vec();
	
	
}
