package de.ebamberg.streamline.ml.data.reader;

import static java.util.stream.Collectors.counting;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Test;

import de.ebamberg.streamline.nlp.streamlineText.IntentionModelDemo;

public class CSVDatasetTest {

	
	@Test
	public void checkReadingFromURLReturnsCorrectNumberOfRecords() throws Exception {
		var dataset=CSVDataset.fromURL(CSVDatasetTest.class.getResource("csvDatasetTest.csv"));
		assertEquals(2,dataset.stream().collect(counting()));
	}

	@Test
	public void checkReadingFromNonExitsingURL() throws Exception {
		var dataset=CSVDataset.fromURL(new URL("file://csvDatasetTest_doesnt_exists.csv"));
		assertThrows(IllegalArgumentException.class, ()-> dataset.stream().collect(counting()));
	}
	
	@Test
	public void checkReadingFromURLTwice() throws Exception {
		var dataset=CSVDataset.fromURL(CSVDatasetTest.class.getResource("csvDatasetTest.csv"));
		assertEquals(2,dataset.stream().collect(counting()));
		assertEquals(2,dataset.stream().collect(counting()));
	}
	
}
