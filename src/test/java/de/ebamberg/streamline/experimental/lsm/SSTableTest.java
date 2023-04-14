package de.ebamberg.streamline.experimental.lsm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

public class SSTableTest {

	
	
	@Test
	public void testsstable_putandquery() throws Exception {
		String[] testdata= new String[] {
				"test","hello","world","foo","bar","at","the","beach","linux"
		};
		
		var table=new SSTable(5);
		IntStream.range(0, testdata.length).forEach(i->table.put(Long.valueOf(i),testdata[i]));
		Thread.sleep(1000l);
		
		IntStream.range(0, testdata.length).forEach(i->assertEquals(testdata[i],table.get((long)i)));
	}
	
	
	@Test
	public void testsstable_QueryKeyNotFound() throws Exception {
		String[] testdata= new String[] {
				"test","hello","world","foo","bar","at","the","beach","linux"
		};
		
		var table=new SSTable(3);
		IntStream.range(0, testdata.length).forEach(i->table.put(Long.valueOf(i),testdata[i]));
		assertNull(table.get(5555l));
	}
	
}
