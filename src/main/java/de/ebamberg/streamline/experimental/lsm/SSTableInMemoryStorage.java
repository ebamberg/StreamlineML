package de.ebamberg.streamline.experimental.lsm;

import java.util.Map;
import java.util.TreeMap;

public class SSTableInMemoryStorage implements SSTableStorage {

	
	TreeMap<Long,String> store;
	
	public SSTableInMemoryStorage() {
		store=new TreeMap<>();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public String get(long key) {
		return store.get(key);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public String put(Long key, String value) {
		return store.put(key, value);
	}

	@Override
	public int size() {
		return store.size();
	}

	@Override
	public int compareTo(SSTableStorage o) {
		// in memory storage always comes first
		return -1;
	}
	
	


}
