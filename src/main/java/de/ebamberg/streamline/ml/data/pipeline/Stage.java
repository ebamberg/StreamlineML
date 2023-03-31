package de.ebamberg.streamline.ml.data.pipeline;

@FunctionalInterface
public interface Stage<I, O> {
	
	public O forward(I input);
	
}
