package de.ebamberg.streamline.ml.text;

import static java.util.stream.Collectors.toConcurrentMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import de.ebamberg.streamline.ml.data.text.Dictionary;

public class StreamDictionary<T> implements Dictionary<T> {

	protected List<T> dictionary;
	protected Map<T, Integer> reverseDictionary;

	@SuppressWarnings("unchecked")
	public static <T> StreamDictionary<T> fromStream(Stream<T> streamIn) {
		try {
			var instance=new StreamDictionary<T>();
			var counter=new AtomicInteger();
			instance.dictionary=streamIn.distinct().toList();
			instance.reverseDictionary=(Map<T,Integer>) instance.dictionary.stream().collect(toConcurrentMap(e->e,e->counter.getAndIncrement()) );
			return instance;
		} finally {
			streamIn.close();
		}
	}

//	public void fromStream(Stream<T> streamIn) {
//		try {
//			var counter=new AtomicInteger();
//			dictionary=streamIn.distinct().toList();
//			reverseDictionary=dictionary.stream().collect(toConcurrentMap(e->e,e->counter.getAndIncrement()) );

//		} finally {
//			streamIn.close();
//		}
//	}

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
