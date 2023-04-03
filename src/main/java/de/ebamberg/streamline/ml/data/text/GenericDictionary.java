package de.ebamberg.streamline.ml.data.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GenericDictionary<T> implements Dictionary<T> {

	protected List<T> dictionary=new ArrayList<>();
	protected Map<T, Integer> reverseDictionary=new ConcurrentHashMap<T, Integer>();

	private AtomicInteger counter=new AtomicInteger();
	
	private boolean locked=false;

	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}


	/**
	 * @param locked the locked to set
	 */
	protected void setLocked(boolean locked) {
		this.locked = locked;
	}


	
	

	@Override
	public int indexOf(T element) {
			return reverseDictionary.computeIfAbsent(element, e-> {
				
					if (!locked) {
						int index=counter.getAndIncrement();
						dictionary.add(element);
						return index;
					} else {
						throw new RuntimeException("Dictionary is locked for write access");
					}
			} );
	}

	@Override
	public T elementOf(int index) {
			return dictionary.get(index);
	}

	@Override
	public int size() {
		if (dictionary == null) {
			return 0;
		} else {
			return dictionary.size();
		}
	}

	@Override
	public void show() {
		var sb=new StringBuilder();
		sb.append(String.format("Dictionary: %d elements [",this.size()));
		dictionary.forEach(e-> { if (e!=null) { sb.append(e.toString()); sb.append(","); }} );
		sb.append("]\n");
		System.out.print(sb.toString());
	}

}
