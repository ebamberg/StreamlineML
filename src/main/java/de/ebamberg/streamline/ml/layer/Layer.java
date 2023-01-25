package de.ebamberg.streamline.ml.layer;

import ai.djl.ndarray.NDArray;

public interface Layer {

	NDArray forward(NDArray input);
	
}
