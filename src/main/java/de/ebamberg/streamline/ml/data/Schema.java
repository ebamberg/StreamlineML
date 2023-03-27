package de.ebamberg.streamline.ml.data;

import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class Schema {

	private Set<Feature<?>> features;

	public Schema(String[] names) {
		super();
		this.features = Arrays
						.stream(names)
						.map( n-> new Feature<Role.FeatureRole>(n, Role.FEATURE ))
						.collect(toSet());
	}
	
	public Schema(List<String> names) {
		super();
		this.features = names.stream().map( n-> new Feature<>(n, Role.FEATURE ))
				.collect(toSet());
	}

	public Schema(Set<Feature<?>> features) {
		super();
		this.features = new HashSet<Feature<?>>(features);
	}
	
	
	public void forEach (Consumer<Feature<?>> consumer) {
		features.forEach(consumer);
	}
	
}
