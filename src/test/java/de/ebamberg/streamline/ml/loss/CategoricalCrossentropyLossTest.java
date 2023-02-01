package de.ebamberg.streamline.ml.loss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ai.djl.ndarray.NDManager;

public class CategoricalCrossentropyLossTest {
	
	@Test
	public void testWithCatagoricalLabels() {
		try (var manager=NDManager.newBaseManager()){
			var lossFunc=new CategoricalCrossentropyLoss();
			var pred=manager.create(new float[][] {{0.0f, 0.60f, 0.20f},{0.4f,0.43f,0.39f},{0.6f,0.43f,0.5f}});
			var real=manager.create(new float[] {1f,1f,0f});
			var loss=lossFunc.apply(pred, real);
			assertEquals(0.6219f,loss.getFloat(),0.0001f);
			System.out.println(loss);
		}
	}

	@Test
	public void testWithCatagoricalLabelsThrowsExceptionWhenShapesDontMatch() {
		try (var manager=NDManager.newBaseManager()){
			var lossFunc=new CategoricalCrossentropyLoss();
			var pred=manager.create(new float[][] {{0.0f, 0.60f, 0.20f},{0.4f,0.43f,0.39f}});
			var real=manager.create(new float[] {1f,1f,0f});
			assertThrows( IllegalArgumentException.class, ()->lossFunc.apply(pred, real));
			
		}
	}
	
	@Test
	public void testWithHotEncodedLabels() {
		try (var manager=NDManager.newBaseManager()){
			var lossFunc=new CategoricalCrossentropyLoss();
			var pred=manager.create(new float[][] {{0.0f, 0.60f, 0.20f},{0.4f,0.43f,0.39f},{0.6f,0.43f,0.5f}});
			var real=manager.create(new float[][] {{0f,1f,0f},{0f,1f,0f},{1f,0f,0f}});
			var loss=lossFunc.apply(pred, real);
			assertEquals(0.6219f,loss.getFloat(),0.0001f);
			System.out.println(loss);
		}

	}

	
}
