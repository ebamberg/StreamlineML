package de.ebamberg.streamline.ml.activation;

import de.ebamberg.streamline.ml.layer.Layer;

public interface Activation extends Layer {
	
	public static Activation reLU = new ReLUActivation();
	public static Activation softMax = new SoftMaxActivation();
	public static Activation tanh = new TanhActivation();

	
}
