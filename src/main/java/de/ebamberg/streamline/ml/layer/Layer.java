package de.ebamberg.streamline.ml.layer;

import ai.djl.ndarray.NDArray;

@FunctionalInterface
public interface Layer {

	NDArray forward(NDArray input);
	
}
