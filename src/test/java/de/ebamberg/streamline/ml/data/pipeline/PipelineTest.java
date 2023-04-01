package de.ebamberg.streamline.ml.data.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
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
	
//	@Test
//	public void testPipelineCanFlatMapFromStream() {
//		
//		var producer=new ArrayDataProducer<>(new String[] {"12,34,56","78,90","00"});
//		var counter=new AtomicInteger();
//		var p=Pipeline.fromProducer(producer)
//						.flatMap(s-> { return Arrays.stream(s.split(",")); } )
//						.then(i->counter.incrementAndGet());
//						
//		producer.start();
//		assertEquals(3, counter.get());
//		
//	}
//
//	@Test
//	public void testPipelineCanFlatMapFromIterable() {
//		
//		var producer=new ArrayDataProducer<>(new String[] {"12,34,56","78,90","00"});
//		var counter=new AtomicInteger();
//		var p=Pipeline.fromProducer(producer)
//						.flatMap(s-> { return Arrays.asList(s.split(",")); } )
//						.then(i->counter.incrementAndGet());
//						
//		producer.start();
//		assertEquals(3, counter.get());
//		
//	}

	@Test
	public void testPipelineCanFlatMapFromIterator() {
		
		var producer=new ArrayDataProducer<>(new String[] {"12,34,56","78,90","00"});
		var counter=new AtomicInteger();
		var p=Pipeline.fromProducer(producer)
						.flatMap(s-> { 
										return Arrays.asList(s.split(",")).iterator(); 
									} )
						.log()
						.then(i->counter.incrementAndGet());
						
		producer.start();
		assertEquals(6, counter.get());
		
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
						.log("this is a log string")
						.log("logging the current value {}")
						.log("logging some parameters: {},{}","hello","world")
						.log("logging parameters and current value: {}->{}","value is")
						.log(); // log() just logs the current element of the flow
		
						
		producer.start();
		
	}
	
}
