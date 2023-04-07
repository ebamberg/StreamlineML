package de.ebamberg.streamline.ml.data.pipeline;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import ai.djl.ndarray.NDManager;

public interface Producer<T> extends AutoCloseable {

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
		
		public void close() {
			
		}

		
	}
	
	public class StreamProducer<T> extends AbstractProducer<T>{
		
		
		private Stream<T> stream;
		
		public StreamProducer(Stream<T> stream) {
			super();
			if (stream==null) {
				throw new NullPointerException("cannot instantiate producer without source data");
			}
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
			if (list==null) {
				throw new NullPointerException("cannot instantiate producer without source data");
			}
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
			close();
		}
	}
	
	public class FloatArrayProducer<NDArray> extends AbstractProducer<NDArray>{
		
		
		private float[][] data;
		private NDManager manager;
		
		public FloatArrayProducer(float[][] data) {
			super();
			if (data==null) {
				throw new NullPointerException("cannot instantiate producer without source data");
			}
			this.data = data;
		}
		@Override
		public void start() {
			if (pipeline!=null) {
				if (manager==null) {
					manager=NDManager.newBaseManager();
				}
				Arrays.stream(data).forEach(e-> {
													pipeline.forward(manager.create(e));
												});
			}
		}
		@Override
		public void stop() {
			super.stop();
			
		}
		
		@Override
		public void close() {
			manager.close();
		}

	}
	
	
}
