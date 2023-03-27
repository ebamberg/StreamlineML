package de.ebamberg.streamline.ml.text;

public interface Dictionary<T> {

	int indexOf(T element);

	T elementOf(int index);

	int size();
	
	default void show() {
		System.out.print(String.format("Dictionary: %d elements",this.size()));
	};


}