package de.ebamberg.streamline.ml.activation;

import ai.djl.ndarray.NDArray;
import de.ebamberg.streamline.ml.layer.Layer;

public class SoftMaxActivation implements Layer {

	@Override
	public NDArray forward(NDArray input) {
		var expValues=input.exp();
		var normValues = expValues.div ( expValues.sum(new int[1], true));
		return normValues;
	}

}
