package de.ebamberg.streamline.ml.activation;

import ai.djl.ndarray.NDArray;

public class SigmoidActivation implements Activation {

	/**
	 * sigmoid id defined as 
	 * output = 1 / (1 + e^(-activation))
	 */
	@Override
	public NDArray forward(NDArray input) {
		var ones=input.getManager().ones(input.getShape());
		var sigmoid= ones.div( ones.add(input.neg().exp()) );
		return sigmoid;
	}

	/**
	 * derivative of sigmoid is defined as 
	 * output * (1.0 - output)
	 */
	@Override
	public NDArray backward(NDArray output) {
		var ones=output.getManager().ones(output.getShape());
		return output.mul(ones.subi(output));
	}	
}
