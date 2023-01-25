package de.ebamberg.streamline.ml.layer;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;

@FunctionalInterface
public interface Initializer {

	NDArray init(NDManager manager,Shape shape);
	default NDArray init(NDManager manager,long... dims)  {
		return init(manager, new Shape(dims));
	};
	
	static Initializer random() {
		return new Initializer() {

			@Override
			public NDArray init(NDManager manager,Shape shape) {
				return manager.randomUniform(0.01f, 1f, shape); 
			}
			
		};
	}
	
}
