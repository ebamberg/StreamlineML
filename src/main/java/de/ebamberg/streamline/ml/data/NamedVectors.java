package de.ebamberg.streamline.ml.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NamedVectors<T> {

	private Map<String,T> vectors;
	
	public static enum TYPE {X,Y};
	
	protected NamedVectors() {
		vectors=new HashMap<>();
	}
	
	public static <T> NamedVectors<T> ofXandY(T x,T y) {
		var vectors=new NamedVectors();
		vectors.put(TYPE.X.name(),x);
		vectors.put(TYPE.Y.name(),y);
		return vectors;
	}
	
	public static <T> NamedVectors<T> empty() {
		return new NamedVectors();
	}

	public void put(String type, T x) {
		vectors.put(type, x);
	}

	public void put(TYPE type, T x) {
		put(type.name(), x);
	}
	
	public Optional<T> get(TYPE type) {
		return get(type.name());
	}
	
	public Optional<T> get(String type) {
		return Optional.ofNullable(vectors.get(type));
	}
	
}
