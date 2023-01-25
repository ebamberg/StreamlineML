package de.ebamberg.streamline.ml.text;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.Shape;

public class OneHotEncoder<T> implements InputEncoder<T> {
	
	protected Dictionary<T> dict;
	protected NDManager 	manager;
	

	protected OneHotEncoder(Dictionary<T> dict) {
		this.dict=dict;
	}

	public static <T> InputEncoder<T> fromDictionary(Dictionary<T> dict) {
		return new OneHotEncoder<T>(dict);
	}
	
	@Override
	public NDArray encode(T input) {
		var idx=new NDIndex(dict.indexOf(input));
		NDArray row=getManager().zeros(new Shape(dict.size()));
		row.set(idx, 1);
		return row;		
	}

	@Override
	public NDManager getManager() {
		if (manager==null) {
			manager=NDManager.newBaseManager();
		}
		return manager;
	}

	@Override
	public void close() throws Exception {
		if (manager!=null) {
			manager.close();
		}
	}
	
}
