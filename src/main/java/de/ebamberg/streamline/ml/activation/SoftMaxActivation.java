package de.ebamberg.streamline.ml.activation;

import ai.djl.ndarray.NDArray;
import de.ebamberg.streamline.ml.layer.Layer;

public class SoftMaxActivation implements Layer {

	@Override
	public NDArray forward(NDArray input) {
		var normalized=input.sub(input.max(new int[] {1}, true));
		var expValues=normalized.exp();
		var probabilities = expValues.div ( expValues.sum(new int[] {1},true));
		return probabilities;
	}

}
