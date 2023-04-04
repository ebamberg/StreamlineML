package de.ebamberg.streamline.ml.data.pipeline;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public interface Producer<T> {

	void start();
	void stop();
	void attach(Pipeline<?, ?> pipeline);

	
	public abstract class AbstractProducer<T> implements Producer<T> {
		protected Pipeline<T,?> pipeline;
		
		@SuppressWarnings("unchecked")
		@Override
		public void attach(Pipeline<?, ?> pipeline) {
			this.pipeline=(Pipeline<T, ?>) pipeline;
		}

		@Override
		public void stop() {
		}

		
	}
	
	public class StreamProducer<T> extends AbstractProducer<T>{
		
		
		private Stream<T> stream;
		
		public StreamProducer(Stream<T> stream) {
			super();
			this.stream = stream;
		}
		@Override
		public void start() {
			if (pipeline!=null) {
				stream.forEach(e->pipeline.forward(e));
			}
		}
		@Override
		public void stop() {
		}
	}

	
	public class ListProducer<T> extends AbstractProducer<T>{
		
		
		private List<T> list;
		
		public ListProducer(List<T> list) {
			super();
			this.list = list;
		}
		@Override
		public void start() {
			if (pipeline!=null) {
				list.forEach(e->pipeline.forward(e));
			}
		}
		@Override
		public void stop() {
		}
	}
	
}
