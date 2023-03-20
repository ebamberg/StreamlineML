package de.ebamberg.streamline.ml.data.batch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamBatcher<T> implements Batcher<T> {

	public static <T> Stream<List<T>> batch(Stream<T> stream, int batchSize) {
        return stream(new StreamBatchIterator<>(stream.iterator(), batchSize));
    }

	public static <T> Stream<List<Integer>> batch(IntStream stream, int batchSize) {
        return stream(new StreamBatchIterator<>(stream.iterator(), batchSize));
    }
	
	private static <T> Stream<T> stream(Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator,Spliterator.ORDERED ), false);
    }
	
	
	static class StreamBatchIterator<T> implements Iterator<List<T>> {
	    private final int batchSize;
	    private List<T> currentBatch;
	    private final Iterator<T> iterator;
	    
	    private StreamBatchIterator(Iterator<T> sourceIterator, int batchSize) {
	        this.batchSize = batchSize;
	        this.iterator = sourceIterator;
	    }
	    @Override
	    public List<T> next() {
	        prepareNextBatch();
	        return currentBatch;
	    }
	    @Override
	    public boolean hasNext() {
	        return iterator.hasNext();
	    }
	    private void prepareNextBatch() {
	        currentBatch = new ArrayList<>(batchSize);
	        while (iterator.hasNext() && currentBatch.size() < batchSize) {
	            currentBatch.add(iterator.next());
	        }
	    }
	}
	
}
