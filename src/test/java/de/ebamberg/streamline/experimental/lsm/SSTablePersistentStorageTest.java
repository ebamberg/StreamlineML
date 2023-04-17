package de.ebamberg.streamline.experimental.lsm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.TreeMap;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

public class SSTablePersistentStorageTest {

	@Test
	public void testLSMstorage_putandquery() throws Exception {
		String[] testdata = new String[] { "test", "hello", "world", "foo", "bar", "at", "the", "beach", "linux" };

		var table = new TreeMap<Long, String>();
		IntStream.range(0, testdata.length).forEach(i -> table.put(Long.valueOf(i), testdata[i]));
		var storage = new SSTablePersistentStorage("StorageTestTable", table); // constructor persists TreeMap
		try {
			IntStream.range(0, testdata.length).forEach(i -> assertEquals(testdata[i], storage.get((long) i)));
		} finally {
			if (storage != null) {
				storage.drop();
			}
		}
	}

	@Test
	public void testLSMstorage_QueryKeyNotFound() throws Exception {
		String[] testdata = new String[] { "test", "hello", "world", "foo", "bar", "at", "the", "beach", "linux" };

		var table = new TreeMap<Long, String>();
		IntStream.range(0, testdata.length).forEach(i -> table.put(Long.valueOf(i), testdata[i]));
		var storage = new SSTablePersistentStorage("StorageTestTable", table); // constructor persists TreeMap
		try {
			assertNull(storage.get(5555l));
		} finally {
			if (storage != null) {
				storage.drop();
			}
		}
	}

	@Test
	public void testLSMstorage_loadExistingTable() throws Exception {
		String[] testdata = new String[] { "test", "hello", "world", "foo", "bar", "at", "the", "beach", "linux" };

		var table = new TreeMap<Long, String>();
		IntStream.range(0, testdata.length).forEach(i -> table.put(Long.valueOf(i), testdata[i]));
		var storage = new SSTablePersistentStorage("StorageTestTable", table); // constructor persists TreeMap

		try {
			var datafile=storage.filename;
			var loadedStorage = new SSTablePersistentStorage(datafile); // constructor persists TreeMap
			IntStream.range(0, testdata.length).forEach(i -> assertEquals(testdata[i], loadedStorage.get((long) i)));
		} finally {
			if (storage != null) {
				storage.drop();
			}
		}
	}

}
