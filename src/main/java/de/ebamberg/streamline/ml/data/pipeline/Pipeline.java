package de.ebamberg.streamline.ml.data.pipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pipeline<I, O>  {

	private static final Logger log=LoggerFactory.getLogger("Pipeline");
	
	private Stage<I,O> stage;
	
	private List<Stage<O,?>> nextStages;
	
	private Stage<Object,?> initialStage;
	
	protected Pipeline( Stage<I, O> newStage,Pipeline<?,I> parent) {
		super();
		
		this.initialStage = parent.initialStage;
		this.nextStages=new ArrayList<Stage<O, ?>>();
		
		this.stage = input -> { 
				O output=newStage.forward (input);
				nextStages.forEach(st->st.forward(output));
				return output;
		};
		
		parent.nextStages.add(this.stage);
	}

	protected Pipeline( IterableStage<I, Iterator<O>> newStage,Pipeline<?,I> parent) {
		super();
		
		this.initialStage = parent.initialStage;
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
	
	
	protected Pipeline(Producer<?> producer) {
		producer.attach((Pipeline<?, ?>) this);
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



	void forward(Object element) {
		if (initialStage!=null) {
			initialStage.forward(element);
		}
	}

	
}
