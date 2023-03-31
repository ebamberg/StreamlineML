package de.ebamberg.streamline.ml.data.pipeline;

public interface Producer<T> {

	void start();
	void stop();
	void attach(Pipeline<?, ?> pipeline);
	
}
