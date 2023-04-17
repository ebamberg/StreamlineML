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
	
	private boolean initialized=false;
	
//	public DenseLayer(NDManager manager,Shape shape, int neurons) {
//		initializeLayer(manager, shape);
//	}

	private void initializeLayer(NDManager manager, Shape shape) {
		long inputSize=shape.get(1);
		weights=initializer.init(manager, inputSize,numNeurons);
		biases=manager.zeros(new Shape(1,numNeurons),DataType.FLOAT32);
	}

	protected DenseLayer(int numberOfNeurons) {
		this.numNeurons=numberOfNeurons;
	}
	
	@Override
	public NDArray forward(NDArray input) {
		if (!initialized) {
			initialized=true;
			initializeLayer(input.getManager().newSubManager(),input.getShape());
		}
		return input.dot(weights).add(biases);
	}
	
	@Override
	public NDArray backward(NDArray backward) {
		throw new UnsupportedOperationException();
	}	
	
	public static DenseLayer ofSize(int numberOfNeurons) {
		return new DenseLayer(numberOfNeurons);
	}

}
