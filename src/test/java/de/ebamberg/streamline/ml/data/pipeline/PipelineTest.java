package de.ebamberg.streamline.ml.data.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class PipelineTest {

	@Test
	public void testPipelineCreatedFromProducer() {
		
		var producer=new ArrayDataProducer<>(new String[] {"hello","world"});
		var p=Pipeline.fromProducer(producer);
		producer.start();
	}

	@Test
	public void testPipelineCanMap() {
		
		var producer=new ArrayDataProducer<>(new String[] {"123","456","789"});
		var p=Pipeline.fromProducer(producer)
						.map(s-> {return Integer.valueOf(s);} )
						.then(System.out::println);
						
		producer.start();
	}

	@Test
	public void testPipelineFlow() {
		
		var producer=new ArrayDataProducer<>(new String[] {"123","456","789"});
		var counter=new AtomicInteger();
		var p=Pipeline.fromProducer(producer)
						.map(s-> {return Integer.valueOf(s);} )
						.then(i->counter.incrementAndGet());
						
		producer.start();
		assertEquals(3, counter.get());
		
	}
	
	@Test
	public void testPipelineCanSplitFlow() {
		
		var producer=new ArrayDataProducer<>(new String[] {"123","456","789"});
		var counter=new AtomicInteger();
		var counter2=new AtomicInteger();
		var p=Pipeline.fromProducer(producer)
						.map(s-> {return Integer.valueOf(s);} );
						
						p.then(v->System.out.println("stream 1 value "+v))
						 .then(i->counter.incrementAndGet());
						
						p.map(i -> i*2)
							.then(v->System.out.println("stream 2 value "+v))
							.then(i->counter2.incrementAndGet());
						
		producer.start();
		assertEquals(3, counter.get());
		assertEquals(3, counter2.get());
		
	}
	
	@Test
	public void testPipelineLoggingStage() {
		
		var producer=new ArrayDataProducer<>(new String[] {"123","456","789"});

		var p=Pipeline.fromProducer(producer)
						.map(s-> {return Integer.valueOf(s);} )
						.log("this is a log string");
						
		producer.start();
		
	}
	
}
