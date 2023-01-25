package de.ebamberg.streamline.ml.activation;

import ai.djl.ndarray.NDArray;
import de.ebamberg.streamline.ml.layer.Layer;

/**
 * 
 * use case in classifiactions: accepts non-normalized values as input and outputs a probability distribution
 * 
 * @author erikb
 *
 */
public class SoftMaxActivation implements Layer {

	@Override
	public NDArray forward(NDArray input) {
		var normalized=input.sub(input.max(new int[] {1}, true));
		var expValues=normalized.exp();
		var probabilities = expValues.div ( expValues.sum(new int[] {1},true));
		return probabilities;
	}

}
