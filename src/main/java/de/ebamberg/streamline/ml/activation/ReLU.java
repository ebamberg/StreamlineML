package de.ebamberg.streamline.ml.activation;

import ai.djl.ndarray.NDArray;

public class ReLU {

	/**
	 * defined as y=max(0,x)
	 * @param array
	 * @return
	 */
    public static NDArray forward(NDArray array) {
    	// return array.clip(0, Float.MAX_VALUE);
    	// use the engine ( like mxnet or tensorflow) internal function for performance.
        return array.getNDArrayInternal().relu();
    }	
}
