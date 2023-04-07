package de.ebamberg.streamline.ml.layer;

@FunctionalInterface
public interface Layer<I,O> {

	O forward(I input);
	
}
