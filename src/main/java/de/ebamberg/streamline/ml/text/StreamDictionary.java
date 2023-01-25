package de.ebamberg.streamline.ml.text;

import static java.util.stream.Collectors.toConcurrentMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public abstract class StreamDictionary<T> implements Dictionary<T> {

	protected List<T> dictionary;
	protected Map<T,Integer> reverseDictionary;
	
	public void fromStream(Stream<T> streamIn) {
		try {
			var counter=new AtomicInteger();
			dictionary=streamIn.distinct().toList();
			reverseDictionary=dictionary.stream().collect(toConcurrentMap(e->e,e->counter.getAndIncrement()) );
			
		} finally {
			streamIn.close();
		}
	}

	@Override
	public int indexOf(T element) {
		return reverseDictionary.get(element);
	}

	@Override
	public T elementOf(int index) {
		return dictionary.get(index);
	}

	
	
	@Override
	public int size() {
		if (dictionary==null) {
			return 0;
		} else {
			return dictionary.size();
		}
	}
	
	
}
