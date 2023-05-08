package de.ebamberg.streamline.ml.data.pipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import ai.djl.ndarray.NDArray;
import de.ebamberg.streamline.ml.activation.Activation;
import de.ebamberg.streamline.ml.data.Record;
import de.ebamberg.streamline.ml.data.encoder.GenericDataRecordEncoder;
import de.ebamberg.streamline.ml.layer.Layer;
import de.ebamberg.streamline.ml.loss.LossFunction;

public class NeuronalNetworkPipeline extends AbstractPipeline {

	private Stage<?,NDArray> stage;

	private Consumer<NDArray> backpropagation=null;
	
	protected List<Stage<NDArray,?>> nextStages;
	
	protected NeuronalNetworkPipeline( Stage<NDArray, NDArray> newStage,NeuronalNetworkPipeline parent) {
		super();
		clonePrivateState(parent);
		
		this.nextStages=new ArrayList<Stage<NDArray, ?>>();
		
		this.stage = input -> { 
				NDArray output=newStage.forward ((NDArray) input);
				if (output!=null) {
					nextStages.forEach(st->st.forward(output));
				}
				return output;
		};
		
		if (newStage instanceof TrainableStage) {
			var backStage=(TrainableStage)newStage;
			this.backpropagation = input -> {		
				NDArray backward=backStage.backward(input);
				if (parent.backpropagation!=null) {
					parent.backpropagation.accept(backward);
				}
			};
		}
		
		parent.nextStages.add((Stage<NDArray, ?>) this.stage);
	}
	
	protected NeuronalNetworkPipeline( IterableStage<NDArray, Iterator<NDArray>> newStage,NeuronalNetworkPipeline parent) {
		super();
		clonePrivateState(parent);
		
		this.nextStages=new ArrayList<Stage<NDArray, ?>>();
		
		this.stage = input -> { 
				var iterator=newStage.forward ((NDArray) input);
				iterator.forEachRemaining( singleElement-> {
					if (singleElement!=null) {
						nextStages.forEach(st->st.forward(singleElement));
					}
				});
				
				return null;
		};
		
		parent.nextStages.add((Stage<NDArray, ?>) this.stage);
	}
	
	
	public <I,O> NeuronalNetworkPipeline(Stage<O, NDArray> newStage, Pipeline<I, O> parent) {
		super();
		clonePrivateState(parent);
		
		this.nextStages=new ArrayList<Stage<NDArray, ?>>();
		
		//FIXME this.stage is a ndArray,NDArray but can be record,ndArray
		this.stage = input -> {
			
			NDArray output=newStage.forward ( (O) input);
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
		var nextStage=new TrainableStage() {
			@Override
			public NDArray forward(NDArray input) {
				return layer.forward(input);
			}

			@Override
			public NDArray backward(NDArray backInput) {
				return layer.backward(backInput);
			}
		};
		return new NeuronalNetworkPipeline(nextStage,this);
	}
	
	public NeuronalNetworkPipeline calculateLoss(LossFunction lossFunction) {
		var nextStage=new Stage<NDArray,NDArray>() {
			@Override
			public NDArray forward(NDArray input) {
				NDArray y_pred=input;
				// get z-real from forward input namedList
				NDArray y_real=input;
				var loss=lossFunction.apply(y_pred,y_real );
				
				if (backpropagation!=null) {
					backpropagation.accept(loss);
				}
				
				return input;
			}
		};
		return new NeuronalNetworkPipeline(nextStage,this);
	}

	public NeuronalNetworkPipeline activate(Activation layer) {
		var nextStage=new TrainableStage() {
			@Override
			public NDArray forward(NDArray input) {
				return layer.forward(input);
			}
			@Override
			public NDArray backward(NDArray backInput) {
				return layer.backward(backInput);
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
	
	private abstract class TrainableStage implements Stage <NDArray,NDArray> {
		public abstract NDArray backward(NDArray backInput);
	}
	
}
