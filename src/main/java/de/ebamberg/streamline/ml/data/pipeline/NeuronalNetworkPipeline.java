package de.ebamberg.streamline.ml.data.pipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import de.ebamberg.streamline.ml.activation.Activation;
import de.ebamberg.streamline.ml.data.Record;
import de.ebamberg.streamline.ml.data.Schema;
import de.ebamberg.streamline.ml.data.text.Dictionary;
import de.ebamberg.streamline.ml.data.text.GenericDictionary;
import de.ebamberg.streamline.ml.layer.Layer;

public class NeuronalNetworkPipeline {

	private static final Logger log=LoggerFactory.getLogger("Pipeline");
	
	private Stage<NDArray,NDArray> stage;
	
	
	List<Stage<NDArray,?>> nextStages;
	Stage<Object,?> initialStage;
	Producer<?> firstProducer;
	
	protected NeuronalNetworkPipeline( Stage<NDArray, NDArray> newStage,NeuronalNetworkPipeline parent) {
		super();
		clonePrivateState(parent);
		
		this.nextStages=new ArrayList<Stage<NDArray, ?>>();
		
		this.stage = input -> { 
				NDArray output=newStage.forward (input);
				if (output!=null) {
					nextStages.forEach(st->st.forward(output));
				}
				return output;
		};
		
		parent.nextStages.add(this.stage);
	}

	private void clonePrivateState(NeuronalNetworkPipeline parent) {
		this.initialStage = parent.initialStage;
		this.firstProducer = parent.firstProducer;
	}

	private void clonePrivateState(Pipeline<?,?> parent) {
		this.initialStage = parent.initialStage;
		this.firstProducer = parent.firstProducer;
	}
	
	protected NeuronalNetworkPipeline( IterableStage<NDArray, Iterator<NDArray>> newStage,NeuronalNetworkPipeline parent) {
		super();
		clonePrivateState(parent);
		
		this.nextStages=new ArrayList<Stage<NDArray, ?>>();
		
		this.stage = input -> { 
				var iterator=newStage.forward (input);
				iterator.forEachRemaining( singleElement-> {
					if (singleElement!=null) {
						nextStages.forEach(st->st.forward(singleElement));
					}
				});
				
				return null;
		};
		
		parent.nextStages.add(this.stage);
	}
	
	
	public <I,O> NeuronalNetworkPipeline(Stage<O, NDArray> newStage, Pipeline<I, O> parent) {
		super();
		clonePrivateState(parent);
		
		this.nextStages=new ArrayList<Stage<NDArray, ?>>();
		
		this.stage = input -> { 
			NDArray output=newStage.forward ((O)input);
			if (output!=null) {
				nextStages.forEach(st->st.forward(output));
			}
			return output;
		};
				
		parent.nextStages.add((Stage<O, ?>) this.stage);
	}


	public NeuronalNetworkPipeline map(Function<NDArray,NDArray> mapFunction) {
		var nextStage=new Stage<NDArray,NDArray>() {
			@Override
			public NDArray forward(NDArray input) {
				return mapFunction.apply(input);
			}
		};
		
		return new NeuronalNetworkPipeline(nextStage,this);
	}
	
	public NeuronalNetworkPipeline flatMap(Function<NDArray,Iterator<NDArray>> mapFunction) {
		var nextStage=new IterableStage<NDArray,Iterator<NDArray>>() {
			@Override
			public Iterator<NDArray> forward(NDArray input) {
				return mapFunction.apply(input);
			}
		};
		
		return new NeuronalNetworkPipeline(nextStage,this);
	}
	
	
	
	public NeuronalNetworkPipeline then(Consumer<NDArray> consumer) {
		var nextStage=new Stage<NDArray,NDArray>() {
			@Override
			public NDArray forward(NDArray input) {
				consumer.accept(input);
				return input;
			}
		};
		return new NeuronalNetworkPipeline(nextStage,this);	
	}

	
	public NeuronalNetworkPipeline log(String logPattern, Object...  parameters) {
		var nextStage=new Stage<NDArray,NDArray>() {
			@Override
			public NDArray forward(NDArray input) {
				if (log.isInfoEnabled()) {
					Object[] combined = Arrays.copyOf(parameters, parameters.length + 1);
					combined[parameters.length]=input;
					log.info(logPattern, combined );
				}
				return input;
			}
		};
		return new NeuronalNetworkPipeline(nextStage,this);	
	}

	public NeuronalNetworkPipeline log() {
		var nextStage=new Stage<NDArray,NDArray>() {
			@Override
			public NDArray forward(NDArray input) {
				if (log.isInfoEnabled()) {
					log.info("pipeline stage: {}", input );
				}
				return input;
			}
		};
		return new NeuronalNetworkPipeline(nextStage,this);	
	}

	


	void forward(Object element) {
		if (initialStage!=null && element!=null) {
			initialStage.forward(element);
		}
	}

	public NeuronalNetworkPipeline execute() {
		if (firstProducer!=null) {
			try {
				firstProducer.start();
			} finally {
				try {
					firstProducer.close();
				} catch (Exception e) {
					log.error("ERROR unable to free resource of data producer",e);
				}
			}
		}
		return this;
	}
	
	
	public NeuronalNetworkPipeline throughLayer(Layer layer) {
		var nextStage=new Stage<NDArray,NDArray>() {
			@Override
			public NDArray forward(NDArray input) {
				return layer.forward(input);
			}
		};
		return new NeuronalNetworkPipeline(nextStage,this);
	}

	public NeuronalNetworkPipeline activate(Activation layer) {
		var nextStage=new Stage<NDArray,NDArray>() {
			@Override
			public NDArray forward(NDArray input) {
				return layer.forward(input);
			}
		};
		return new NeuronalNetworkPipeline(nextStage,this);
	}
	
	public <K> Pipeline<NDArray,K> transformOutput(Function<NDArray,K> mapFunction) {
		var nextStage=new Stage<NDArray,K>() {
			@Override
			public K forward(NDArray input) {
				return mapFunction.apply(input);
			}
		};
		
		return new Pipeline<>(nextStage,this);
	}
	
//	public <K> Pipeline<O,K> transformOutput(Function<O,K> mapFunction) {
//		var nextStage=new Stage<O,K>() {
//			@Override
//			public K forward(O input) {
//				return mapFunction.apply(input);
//			}
//		};
//		
//		return new Pipeline<>(nextStage,this);
//	}
	
	
}
