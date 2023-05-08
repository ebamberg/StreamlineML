package de.ebamberg.streamline.ml.data;

import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class Schema {

	private Map<String, Feature<?>> features;
	private Map<String, Integer>	nameToIndexMap;
	//TODO change into List<Feature>
	private List<String>			names;

	public Schema(String[] names) {
		super();
		this.names =Arrays.asList(names);
		reindex();
	}
		
	
	public Schema(List<String> names) {
		super();
		this.names=names.stream().sequential().toList();
		reindex();
	}

	public Schema(List<String> names, Class<?>[] fixedDatatypes) {
		super();
		this.names=names.stream().sequential().toList();
		reindex(fixedDatatypes);
	}


	public Schema(Feature<?>[] features) {
		super();
		this.names=Arrays.stream(features).map(Feature::getName).toList();
		this.nameToIndexMap=new HashMap<>();
		var index=new AtomicInteger();
		this.features = Arrays.stream(features)
				.peek( f-> nameToIndexMap.put(f.getName(),index.getAndIncrement()))
				.collect(toMap(Feature::getName, f->f));
	}
	
	/**
	 * copy constructor
	 * 
	 * @param schema
	 */
	protected Schema(Schema schema) {
		this.names=new ArrayList<>(schema.names);
		this.features=new HashMap<>(schema.features);
		this.nameToIndexMap=new HashMap<>(schema.nameToIndexMap);
	}
	

	private void reindex( ) {
		var datatypes= new Class<?>[this.names.size()];
		Arrays.setAll(datatypes, i-> String.class );
		reindex(datatypes);
	}


	private void reindex(Class<?>[] fixedDatatypes ) {
		this.nameToIndexMap=new HashMap<>();
		var index=new AtomicInteger();
		this.features = names.stream()
				.peek( n-> nameToIndexMap.put(n,index.getAndIncrement()))
				.map( n-> new Feature<>(n, Role.FEATURE, fixedDatatypes[index.get()-1] ))
				.collect(toMap(Feature::getName, f->f));
	}
	
	/**
	 * 
	 * get the index of the feature
	 * 
	 * @param featureName
	 * @return index of feature or -1 of not found
	 */
	public int indexOf(String featureName) {
		return nameToIndexMap.getOrDefault(featureName, -1);
	}
	
	/**
	 * 
	 * get the index of the feature
	 * 
	 * @param feature
	 * @return index of feature or -1 of not found
	 */
	public int indexOf(Feature feature) {
		return nameToIndexMap.getOrDefault(feature.getName(),-1);
	}
	
	public int size() {
		if (features==null)	{
			return 0;
		} 
		return features.values().size();
	}
	
	public void forEach (Consumer<Feature<?>> consumer) {
		names.stream().map(n->features.get(n)).forEach(consumer);
	}

	public List<String> getFeaturesNames() {
		return Collections.unmodifiableList(names);
	}
	

	public static Schema of(Feature<?>... features) {
		Schema schema=new Schema(features);
		return schema;
	}


	
}
