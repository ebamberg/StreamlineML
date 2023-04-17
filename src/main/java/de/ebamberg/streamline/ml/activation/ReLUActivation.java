package de.ebamberg.streamline.ml.activation;

import ai.djl.ndarray.NDArray;
import de.ebamberg.streamline.ml.layer.Layer;

public class ReLUActivation implements Activation {

	/**
	 * defined as y=max(0,x)
	 * @param array
	 * @return
	 */
	@Override
    public NDArray forward(NDArray array) {
    	// return array.clip(0, Float.MAX_VALUE);
    	// use the engine ( like mxnet or tensorflow) internal function for performance.
        return array.getNDArrayInternal().relu();
    }

	@Override
	public NDArray backward(NDArray backward) {
		throw new UnsupportedOperationException();
	}	
}
