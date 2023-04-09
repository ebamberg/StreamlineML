package de.ebamberg.streamline.ml.activation;

import ai.djl.ndarray.NDArray;
import de.ebamberg.streamline.ml.layer.Layer;

public interface Activation extends Layer<NDArray, NDArray> {
	
	public static Activation reLU = new ReLUActivation();
	public static Activation softMax = new SoftMaxActivation();
	public static Activation tanh = new TanhActivation();

	
}
