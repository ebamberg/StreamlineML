package de.ebamberg.streamline.ml.layer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import ai.djl.ndarray.NDArray;
import de.ebamberg.streamline.ml.activation.ReLUActivation;
import de.ebamberg.streamline.ml.data.pipeline.Pipeline;
import de.ebamberg.streamline.ml.data.pipeline.Producer.FloatArrayProducer;

public class SimpleClassificationPipelineTest {


	@Test
	public void testDenseInputLayer() {
		var inputData= new float[][] {
            {1f,2f,3f,2.5f},
            {2f,5f,-1f,2f},
            {-1.5f,2.7f,3.3f,-0.8f}
		};

		Pipeline.fromProducer(new FloatArrayProducer<NDArray>(inputData))
			.inputLayer(new DenseLayer(),3)
			.log()
			.then(array->assertNotNull(array))
			.map(NDArray::getShape)
			.then(shape->assertEquals(3,shape.get(0)))
			.then(shape->assertEquals(5,shape.get(1)))
			.execute();
	}

	@Test
	public void testDenseInputLayerWithActivation() {
		var inputData= new float[][] {
            {1f,2f,3f,2.5f},
            {2f,5f,-1f,2f},
            {-1.5f,2.7f,3.3f,-0.8f}
		};

		Pipeline.fromProducer(new FloatArrayProducer<NDArray>(inputData))
			.inputLayer(new DenseLayer(),3)
			.layer(new ReLUActivation())
			.log()
			.then(array->assertNotNull(array))
			.map(NDArray::getShape)
			.then(shape->assertEquals(3,shape.get(0)))
			.then(shape->assertEquals(5,shape.get(1)))
			.execute();
	}

	
}
