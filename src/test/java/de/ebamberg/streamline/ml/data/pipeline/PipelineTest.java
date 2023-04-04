package de.ebamberg.streamline.ml.data.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import de.ebamberg.streamline.ml.data.reader.CSVDataset;
import de.ebamberg.streamline.ml.data.reader.CSVDatasetTest;

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
	public void testPipelineCast() {
		
		var producer=new ArrayDataProducer<>(new String[] {"123","456","789"});
		var p=Pipeline.fromProducer(producer)
						.map(s-> {return Integer.valueOf(s);} )
						.cast(Number.class )
						.then(i-> assertTrue(i instanceof Number) );
						
		producer.start();
		
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
//						.flatMap(s-> { 
//							return Arrays.asList(s.split(",")); 
//						} )
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
	
	@Test
	public void testPipelineCategorizeAValue() {
		
		var index=new AtomicInteger();
		
			CSVDataset
				.fromResource("csvDatasetTest.csv")
				.read()
				.categorize()
				.then(e->assertEquals(index.getAndIncrement(),e))
				.execute();
					
	}
	
//	@Test
//	public void testPipelineSchemaChangesWithSchema() {
//		
//			CSVDataset
//				.fromResource("csvDatasetTest.csv")
//				.read()
//				.withSchema(schema-> schema
//										.categorize("header1")
//									)
//				
//				.execute();		
//	}
	
	@Test
	public void testPipelineChangeValuesWithSchema() {
		
			CSVDataset
				.fromResource("csvDatasetTest.csv")
				.read()
				.then(record-> {
					var val=(String)record.getValue("header2");
					record.updateValue("header2", val.substring(0,1));
				})
				.then(record->assertEquals(1, ((String)record.getValue("header2")).length())   )
				.log()
				.execute();		
	}
	
	@Test
	public void testPipelineWithFeaturePipelines() {
		
			CSVDataset
				.fromResource("csvDatasetTest.csv")
				.read()
				.withFeature("header2", feature->feature
													.cast(String.class)
													.map(v->v.substring(0,1))
													.log() 
							)
				.then(record->assertEquals(1, ((String)record.getValue("header2")).length())   )
				.then(record->  record.updateValue("header2","overridden literal value")  )
				.then(record->assertEquals("overridden literal value".length(), ((String)record.getValue("header2")).length())   )
				.log()
				.execute();		
	}
	
}
