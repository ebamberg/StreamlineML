package de.ebamberg.streamline.ml.examples;

import ai.djl.ndarray.NDManager;
import de.ebamberg.streamline.ml.activation.ReLUActivation;
import de.ebamberg.streamline.ml.layer.DenseLayer;

public class SimpleClassification {

	public static void main(String[] args) {
		var inputData= new float[][] {
		            {1f,2f,3f,2.5f},
		            {2f,5f,-1f,2f},
		            {-1.5f,2.7f,3.3f,-0.8f}
			};
			
		try (var manager=NDManager.newBaseManager()) {
			var x= manager.create(inputData);
			
			var layer1=new DenseLayer(manager, x.getShape());
			var activation1=new ReLUActivation();
			
			var output1=layer1.forward(x);
			var output2=activation1.forward(output1);
			
			
			System.out.printf("input data: %s\n", x.toString());
			System.out.printf("after dense layer: %s\n", output1.toString());
			System.out.printf("after activation: %s\n", output2.toString());

		}

	}

}
