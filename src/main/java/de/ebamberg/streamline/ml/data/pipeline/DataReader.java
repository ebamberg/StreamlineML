package de.ebamberg.streamline.ml.data.pipeline;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface DataReader<T> extends Iterable<T> {

	
	default public Stream<T> stream( ) {
		return StreamSupport.stream(spliterator(), false);
	}
	
	default public Pipeline<T,T> read() {
		return Pipeline.from(this);
	}
}
