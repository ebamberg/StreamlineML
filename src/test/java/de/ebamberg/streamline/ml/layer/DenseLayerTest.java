package de.ebamberg.streamline.ml.layer;

import org.junit.jupiter.api.Test;

import ai.djl.ndarray.NDManager;

public class DenseLayerTest {

	@Test
	public void testDense() {
		var inputData = new float[][] { { 1f, 2f, 3f, 2.5f }, { 2f, 5f, -1f, 2f }, { -1.5f, 2.7f, 3.3f, -0.8f } };
		var categories = new float[] { 0f, 1f, 1f }; // categories

		try (var manager = NDManager.newBaseManager()) {
			var x = manager.create(inputData);

			var layer1 = new DenseLayer(manager, x.getShape(), 5);
			var output1 = layer1.forward(x);

			System.out.printf("after dense layer: %s\n", output1.toString());

		}

	}

}
