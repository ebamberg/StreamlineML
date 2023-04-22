package de.ebamberg.streamline.ml.api;

@FunctionalInterface
public interface Predictor<I, O> {

	public O predict(I input);
	
}
