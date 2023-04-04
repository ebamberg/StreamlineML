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

import de.ebamberg.streamline.ml.data.Record;
import de.ebamberg.streamline.ml.data.Schema;
import de.ebamberg.streamline.ml.data.text.Dictionary;
import de.ebamberg.streamline.ml.data.text.GenericDictionary;
import de.ebamberg.streamline.ml.layer.rnn.RNNLayer;

public class Pipeline<I, O>  {

	private static final Logger log=LoggerFactory.getLogger("Pipeline");
	
	private Stage<I,O> stage;
	
	private List<Stage<O,?>> nextStages;
	
	private Stage<Object,?> initialStage;
	private Producer<?> firstProducer;
	
	protected Pipeline( Stage<I, O> newStage,Pipeline<?,I> parent) {
		super();
		clonePrivateState(parent);
		
		this.nextStages=new ArrayList<Stage<O, ?>>();
		
		this.stage = input -> { 
				O output=newStage.forward (input);
				nextStages.forEach(st->st.forward(output));
				return output;
		};
		
		parent.nextStages.add(this.stage);
	}

	private void clonePrivateState(Pipeline<?, I> parent) {
		this.initialStage = parent.initialStage;
		this.firstProducer = parent.firstProducer;
	}

	protected Pipeline( IterableStage<I, Iterator<O>> newStage,Pipeline<?,I> parent) {
		super();
		clonePrivateState(parent);
		
		this.nextStages=new ArrayList<Stage<O, ?>>();
		
		this.stage = input -> { 
				var iterator=newStage.forward (input);
				iterator.forEachRemaining( singleElement-> {
					nextStages.forEach(st->st.forward(singleElement));
				});
				
				return null;
		};
		
		parent.nextStages.add(this.stage);
	}

	
	private Pipeline() {
		this.stage=input->{
			nextStages.forEach(st-> st.forward((O)input));
			return (O)input;
		};
		initialStage=(Stage<Object, ?>) stage;
		this.nextStages=new ArrayList<Stage<O, ?>>();
	}
	
	protected Pipeline(Producer<?> producer) {
		producer.attach((Pipeline<?, ?>) this);
		firstProducer=producer;
		this.stage=input->{
			nextStages.forEach(st-> st.forward((O)input));
			return (O)input;
		};
		initialStage=(Stage<Object, ?>) stage;
		this.nextStages=new ArrayList<Stage<O, ?>>();
	}
	
	
	public static <K> Pipeline<K,K> fromProducer(Producer<K> producer) {
		return new Pipeline<>(producer);
	}

	public static <K> Pipeline<K,K> fromStream(Stream<K> inputStream) {
		return new Pipeline<>(new Producer.StreamProducer(inputStream));
	}

	public static <K> Pipeline<K,K> from(Iterable<K> dataset) {
		return new Pipeline<>(new Producer.StreamProducer(StreamSupport.stream(dataset.spliterator(), false)));
	}
	
	protected static <K> Pipeline<K,K> forType(Class<K> type) {
		return new Pipeline<>();
		
	}

	public <K> Pipeline<O,K> cast(Class<K> targetType) {
		var nextStage=new Stage<O,K>() {
			@SuppressWarnings("unchecked")
			@Override
			public K forward(O input) {
				return (K)input;
			}
		};
		
		return new Pipeline<>(nextStage,this);
	}
	
	
	public <K> Pipeline<O,K> map(Function<O,K> mapFunction) {
		var nextStage=new Stage<O,K>() {
			@Override
			public K forward(O input) {
				return mapFunction.apply(input);
			}
		};
		
		return new Pipeline<>(nextStage,this);
	}
	
	public <K> Pipeline<O,K> flatMap(Function<O,Iterator<K>> mapFunction) {
		var nextStage=new IterableStage<O,Iterator<K>>() {
			@Override
			public Iterator<K> forward(O input) {
				return mapFunction.apply(input);
			}
		};
		
		return new Pipeline<>(nextStage,this);
	}
	
	
	
	public Pipeline<O,O> then(Consumer<O> consumer) {
		var nextStage=new Stage<O,O>() {
			@Override
			public O forward(O input) {
				consumer.accept(input);
				return input;
			}
		};
		return new Pipeline<>(nextStage,this);	
	}

	public Pipeline<O,O> withFeature(String featureName, Function<Pipeline<?,?>,Pipeline<?,?>> pipelineBuilder) {
		var nextStage=new Stage<O,O>() {
			@Override
			public O forward(O input) {
				var record=(Record)input;
				Object value=record.getValue(featureName);
				var featurePipeline=Pipeline.forType(String.class);
				featurePipeline=(Pipeline<String, String>) pipelineBuilder.apply(featurePipeline);
				featurePipeline.then( result-> record.updateValue(featureName, result));
				featurePipeline.forward(value);
				return input;
			}
		};
		return new Pipeline<>(nextStage,this);	
	}
	
	public Pipeline<O,O> log(String logPattern, Object...  parameters) {
		var nextStage=new Stage<O,O>() {
			@Override
			public O forward(O input) {
				if (log.isInfoEnabled()) {
					Object[] combined = Arrays.copyOf(parameters, parameters.length + 1);
					combined[parameters.length]=input;
					log.info(logPattern, combined );
				}
				return input;
			}
		};
		return new Pipeline<>(nextStage,this);	
	}

	public Pipeline<O,O> log() {
		var nextStage=new Stage<O,O>() {
			@Override
			public O forward(O input) {
				if (log.isInfoEnabled()) {
					log.info("pipeline stage: {}", input );
				}
				return input;
			}
		};
		return new Pipeline<>(nextStage,this);	
	}

	
	
//	public Schema categorize(String featureName) {
//		var newSchema= new Schema(this);
//		var f=newSchema.features.get(featureName);
//		newSchema.features.put(featureName, f.setCategorize(true) );
//		return newSchema;
//	}
	
	public  Pipeline<O,Integer> categorize() {
		
		var nextStage=new Stage<O,Integer>() {
			Dictionary<O> dictionary=new GenericDictionary<O>();
			@Override
			public Integer forward(O input) {
				return Integer.valueOf(dictionary.indexOf(input));
			}
		};
		
		return new Pipeline<>(nextStage,this);
	}

	public  Pipeline<O,Integer> categorize(String featureName) {
		
		var nextStage=new Stage<O,Integer>() {
			Dictionary<Object> dictionary=new GenericDictionary<Object>();
			@Override
			public Integer forward(O input) {
				if (input instanceof Record) {
					var r=(Record)input;
				return Integer.valueOf(dictionary.indexOf(r.getValue(featureName)));
				} else {
					throw new RuntimeException("input of categorize is not a schemabased record");
				}
			}
		};
		
		return new Pipeline<>(nextStage,this);
	}

	
	
	public Pipeline<O,O> withSchema( Function<Schema,Schema> schemaBuilder) {
		var nextStage=new Stage<O,O>() {
			@Override
			public O forward(O input) {
				if (input instanceof Record) {
					var currentRecord=(Record)input;
					var newSchema=schemaBuilder.apply( currentRecord.getSchema());
					currentRecord.setSchema(newSchema); 
					return input;
				} else {
					throw new IllegalArgumentException("Datastream has no schema.");
				}
			}
		};
		return new Pipeline<>(nextStage,this);	
	}


	void forward(Object element) {
		if (initialStage!=null) {
			initialStage.forward(element);
		}
	}

	public Pipeline<I,O> execute() {
		if (firstProducer!=null) {
			firstProducer.start();
		}
		return this;
	}

	
}
