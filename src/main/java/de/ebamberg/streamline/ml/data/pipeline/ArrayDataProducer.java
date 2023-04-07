package de.ebamberg.streamline.ml.data.pipeline;

import java.util.Arrays;

public class ArrayDataProducer<T> implements Producer<T> {

	private Pipeline<T,?> pipeline;
	
	private T[] data;
	
	ArrayDataProducer(T[] data) {
		this.data=data;
	}
	
	
	@Override
	public void start() {
		if (pipeline!=null) {
			Arrays.stream(data).forEach(e->pipeline.forward(e));
		}
	}

	@Override
	public void stop() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void attach(Pipeline<?, ?> pipeline) {
		this.pipeline=(Pipeline<T, ?>) pipeline;
	}


	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
