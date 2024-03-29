package de.ebamberg.streamline.ml.loss;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.index.NDIndex;

public class CategoricalCrossentropyLoss implements LossFunction{

	public NDArray apply(NDArray y_pred, NDArray y_real) {
		checkPreconditions(y_pred, y_real);
		var samples = y_pred.size(0);
		var y_pred_clipped = y_pred.clip(1e-7f, 1 - 1e-7f);		// to avoid log(0) =0/INF problem
		NDArray confidences;
		if (y_real.getShape().dimension()==1) {  // categorical labels
			var index=new NDIndex()
						.addAllDim(1)
						.addPickDim(y_real);
			confidences=y_pred.get(index);
		} else {		// one-hot encoded
			System.out.println("one-hot-encoded");
			confidences = y_pred_clipped.mul(y_real).sum(new int[]{1});
		}
		var log_likelihoods = confidences.log().negi();
		return log_likelihoods.mean();
	}

	private void checkPreconditions(NDArray y_pred, NDArray y_real) {
		if (y_real.size(0)!=y_pred.size(0)) {
			throw new IllegalArgumentException(String.format("number of labels [%d] doesn't match number of samples [%d] in the predicted input.",y_real.size(0),y_pred.size(0)));
		}
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
