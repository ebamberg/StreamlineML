package de.ebamberg.streamline.ml.data.batch;

public class Batch<T, U> {

	private U data;
	private T labels;
	
	public Batch(U data, T labels) {
		super();
		this.data = data;
		this.labels = labels;
	}
	
	public U getData() {
		return data;
	}
	public T getLabels() {
		return labels;
	}
	
	
	
	
}
