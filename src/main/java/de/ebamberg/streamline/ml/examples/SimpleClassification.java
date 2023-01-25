package de.ebamberg.streamline.ml.examples;

import java.util.Arrays;

import ai.djl.ndarray.NDManager;
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
			var output=layer1.forward(x);
			
			System.out.println(x);
			System.out.println(output);
		}

	}

}
