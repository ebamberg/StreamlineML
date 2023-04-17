package de.ebamberg.streamline.ml.layer.rnn;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import de.ebamberg.streamline.ml.layer.Layer;

// https://github.com/JY-Yoon/RNN-Implementation-using-NumPy
public class RNNLayer implements Layer {

	private long hiddenSize=100;
	private NDArray weight_xh,weight_hh,weight_hy,bias_h,bias_y, h_prev;
			
	public RNNLayer(NDManager manager, Shape shape) {
		initialize(manager, shape);
	}
	
	@Override
	public NDArray forward(NDArray input) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public NDArray backward(NDArray backward) {
		throw new UnsupportedOperationException();
	}	

	/**
	 * 	//	W_xh = np.random.randn(hidden_size, num_chars)*0.01     # weight input -> hidden. 
		//		W_hh = np.random.randn(hidden_size, hidden_size)*0.01   # weight hidden -> hidden
		//		W_hy = np.random.randn(num_chars, hidden_size)*0.01     # weight hidden -> output
		
		//		b_h = np.zeros((hidden_size, 1)) # hidden bias
		//		b_y = np.zeros((num_chars, 1)) # output bias
		
		//		h_prev = np.zeros((hidden_size,1)) # h_(t-1)
	 */
	private void initialize(NDManager manager, Shape shape) {
			//TODO multidimensional array
			var inputSize=shape.get(1);
			weight_xh=manager.randomUniform(0.01f, Float.MAX_VALUE, new Shape(hiddenSize,inputSize)); // weight input -> hidden. 
			weight_hh=manager.randomUniform(0.01f, Float.MAX_VALUE, new Shape(hiddenSize,hiddenSize)); // weight hidden -> hidden
			weight_hy=manager.randomUniform(0.01f, Float.MAX_VALUE, new Shape(inputSize,hiddenSize)); //  # weight hidden -> output
			
			bias_h = manager.zeros(new Shape(hiddenSize, 1)); // hidden bias
			bias_y = manager.zeros(new Shape(inputSize, 1)); // output bias
			
			h_prev = manager.zeros(new Shape(hiddenSize,1)); // previous hidden state h_(t-1)		
	}

}
