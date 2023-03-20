package de.ebamberg.streamline.ml.data.batch;

import static de.ebamberg.streamline.ml.data.batch.StreamBatcher.batch;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

public class StreamBatcherTest {

	@Test
	public void testBatchingOfStreams() {
		var testStream=IntStream.range(0, 20);
		var batchStream=batch(testStream, 5);
		assertEquals(4,batchStream.count());	
	}
	
}
