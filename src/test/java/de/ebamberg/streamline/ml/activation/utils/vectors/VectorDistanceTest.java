package de.ebamberg.streamline.ml.activation.utils.vectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ai.djl.ndarray.NDManager;

public class VectorDistanceTest {

	@Test
	public void testCosineDistance() {
		try (var manager=NDManager.newBaseManager()) {
			var vector1=manager.create(new float[][] {{3,4},{7,8}});
			assertEquals( 0f, VectorDistance.cosineDistance(vector1, vector1) );
		}
	}

	@Test
	public void testCosineSimilarity() {
		try (var manager=NDManager.newBaseManager()) {
			var vector1=manager.create(new float[][] {{3,4},{7,8}});
			var vector2=manager.create(new float[][] {{1,2},{7,8}});
			assertEquals( 0.97f, VectorDistance.cosineSimilarity(vector1, vector2),0.01f );
		}
	}
	
}
