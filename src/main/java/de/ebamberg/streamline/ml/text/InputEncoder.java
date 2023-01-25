package de.ebamberg.streamline.ml.text;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;

public interface InputEncoder<T> extends AutoCloseable {

	NDArray encode(T input);

	NDManager getManager();

}