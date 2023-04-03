package de.ebamberg.streamline.ml.data.batch;

public class Record_DEPRECATED<T,U> {

	private U data;
	private T labels;
	
	public Record_DEPRECATED(U data, T labels) {
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
