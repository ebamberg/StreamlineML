package de.ebamberg.streamline.experimental.lsm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.TreeMap;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

public class SSTablePersistentStorageTest {

	
	
	@Test
	public void testLSMstorage_putandquery() throws Exception {
		String[] testdata= new String[] {
				"test","hello","world","foo","bar","at","the","beach","linux"
		};
		
		var table=new TreeMap<Long,String>();
		IntStream.range(0, testdata.length).forEach(i->table.put(Long.valueOf(i),testdata[i]));
		var storage=new SSTablePersistentStorage(table); // constructor persists TreeMap
		
		IntStream.range(0, testdata.length).forEach(i->assertEquals(testdata[i],storage.get((long)i)));
	}
	
	
	@Test
	public void testLSMstorage_QueryKeyNotFound() throws Exception {
		String[] testdata= new String[] {
				"test","hello","world","foo","bar","at","the","beach","linux"
		};
		
		var table=new TreeMap<Long,String>();
		IntStream.range(0, testdata.length).forEach(i->table.put(Long.valueOf(i),testdata[i]));
		var storage=new SSTablePersistentStorage(table); // constructor persists TreeMap
	//	table.persist();
		assertNull(storage.get(5555l));
	}
	
}
