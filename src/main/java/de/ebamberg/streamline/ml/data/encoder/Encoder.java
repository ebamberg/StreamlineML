package de.ebamberg.streamline.ml.data.encoder;

import ai.djl.ndarray.NDArray;

public interface Encoder<T> {

	NDArray encode(T input);
	
}
