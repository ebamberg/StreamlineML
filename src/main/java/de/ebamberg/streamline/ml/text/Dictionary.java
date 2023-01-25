package de.ebamberg.streamline.ml.text;

public interface Dictionary<T> {

	int indexOf(T element);

	T elementOf(int index);

	int size();

}