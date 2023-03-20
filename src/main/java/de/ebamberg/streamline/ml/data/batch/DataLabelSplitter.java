package de.ebamberg.streamline.ml.data.batch;

public interface DataLabelSplitter<T,U> {

	public Record<T,U> split(T input); 
	
}
