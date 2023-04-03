package de.ebamberg.streamline.ml.data.text;

public interface Dictionary<T> {

	int indexOf(T element);

	T elementOf(int index);

	int size();
	
	boolean isLocked();
	
	default void show() {
		System.out.print(String.format("Dictionary: %d elements",this.size()));
	};


}