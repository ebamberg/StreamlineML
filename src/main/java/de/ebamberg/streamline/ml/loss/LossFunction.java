package de.ebamberg.streamline.ml.loss;

import java.util.function.BiFunction;

import ai.djl.ndarray.NDArray;

public interface LossFunction extends BiFunction<NDArray, NDArray, NDArray>{

	public NDArray apply(NDArray y_pred, NDArray y_real);
	
	public static LossFunction categoricalCrossentropyLoss =new CategoricalCrossentropyLoss();
	
}
