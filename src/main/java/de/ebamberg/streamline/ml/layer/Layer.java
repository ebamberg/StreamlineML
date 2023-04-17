package de.ebamberg.streamline.ml.layer;

import ai.djl.ndarray.NDArray;

public interface Layer {

	NDArray forward(NDArray input);
	
	/**
	 * 
	 * backward propagation 
	 * 
	 * calculates the derivatives for this layer.
	 * 
	 * @param backward
	 * @return
	 */
	NDArray backward(NDArray backward);
	
}
