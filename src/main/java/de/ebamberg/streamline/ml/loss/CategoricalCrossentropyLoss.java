package de.ebamberg.streamline.ml.loss;

import java.util.function.BiFunction;

import ai.djl.ndarray.NDArray;

public class CategoricalCrossentropyLoss implements BiFunction<NDArray, NDArray, NDArray>{

	public NDArray apply(NDArray y_pred, NDArray y_real) {
		// TODO Auto-generated method stub
		return null;
	}

	
	 /**
	  * 
	  * # Calculate sample losses
 sample_losses = self.forward(output, y)
 # Calculate mean loss
 data_loss = np.mean(sample_losses)
 # Return loss
 return data_los
	  * 
	  * def forward(self, y_pred, y_true):
 # Number of samples in a batch
 samples = len(y_pred)
 # Clip data to prevent division by 0
 # Clip both sides to not drag mean towards any value
 y_pred_clipped = np.clip(y_pred, 1e-7, 1 - 1e-7)
 # Probabilities for target values -
 # only if categorical labels
 if len(y_true.shape) == 1:
 correct_confidences = y_pred_clipped[
 range(samples),
 y_true
 ]
 # Mask values - only for one-hot encoded labels
 elif len(y_true.shape) == 2:
 correct_confidences = np.sum(
 y_pred_clipped*y_true,
 axis=1
 )
 # Losses
 negative_log_likelihoods = -np.log(correct_confidences)
 return negative_log_likelihoods
	  */
}
