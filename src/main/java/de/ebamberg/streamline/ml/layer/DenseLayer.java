package de.ebamberg.streamline.ml.layer;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;

public class DenseLayer implements Layer {

	private Initializer initializer=Initializer.random();
	
	private long numNeurons=5;
	protected NDArray weights;
	protected NDArray biases;
	
	public DenseLayer(NDManager manager,Shape shape) {
		long inputSize=shape.get(1);
		weights=initializer.init(manager, inputSize,numNeurons);
		biases=manager.zeros(new Shape(1,numNeurons),DataType.FLOAT32);
	}
	
	@Override
	public NDArray forward(NDArray input) {
		return input.dot(weights).add(biases);
	}

}
