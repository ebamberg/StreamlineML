package de.ebamberg.streamline.experimental.lsm;

public interface SSTableStorage extends Comparable<SSTableStorage> {

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	String get(long key);

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	String put(Long key, String value);

	/**
	 * deletes all data hold be this storage
	 */
	void drop();
	
	int size();
}