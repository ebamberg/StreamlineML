package de.ebamberg.streamline.ml.data.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import de.ebamberg.streamline.ml.data.reader.CSVDataset;
import de.ebamberg.streamline.ml.data.reader.CSVDatasetTest;

public class DataReaderTest {

	@Test
	public void testReadDatasetIntoPipeline() {
		var counter=new AtomicInteger();
		
		var dataset=CSVDataset
					.fromURL(CSVDatasetTest.class.getResource("csvDatasetTest.csv"))
					.read()
					.then(e-> counter.incrementAndGet())
					.execute();
		
		assertEquals(2,counter.get());
		
	}
	
}
