package de.ebamberg.streamline.ml.layer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ai.djl.ndarray.NDArray;
import de.ebamberg.streamline.ml.activation.ReLUActivation;
import de.ebamberg.streamline.ml.activation.SoftMaxActivation;
import de.ebamberg.streamline.ml.data.pipeline.Pipeline;
import de.ebamberg.streamline.ml.data.pipeline.Producer.FloatArrayProducer;
import static de.ebamberg.streamline.ml.activation.Activations.*;

public class SimpleClassificationPipelineTest {


	@Test
	public void testDenseInputLayer() {
		var inputData= new float[][] {
            {1f,2f,3f,2.5f},
            {2f,5f,-1f,2f},
            {-1.5f,2.7f,3.3f,-0.8f}
		};

		Pipeline.fromProducer(new FloatArrayProducer<NDArray>(inputData))
			.throughInputLayer(DenseLayer.ofSize(5),3)
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

		// we define our pure pipeline without any test-logic
		var pipelineUnderTest=Pipeline.fromProducer(new FloatArrayProducer<NDArray>(inputData))
								.throughInputLayer(DenseLayer.ofSize(5),3)
								.throughLayer(reLU)
								.log();
		
			// add assertions to the pipelline
			pipelineUnderTest
				.then(array->assertNotNull(array))
				.map(NDArray::getShape)
				.then(shape->assertEquals(3,shape.get(0)))
				.then(shape->assertEquals(5,shape.get(1)));

			// add more assertions to the pipelline
			pipelineUnderTest
				.then(array->assertNotNull(array))
				.then(array-> {
					for (int y=0;y<array.getShape().get(0);y++) {
						for (int x=0;x<array.getShape().get(0);x++) {
							assertTrue(array.getFloat(y,x)>=0, "assert that after reLu activation we have no minus values in result");
						}
					}
					
				} );
			//now start the pipeline to run the tests
			pipelineUnderTest.execute();
			
	}

	@Test
	public void testMultipleDenseInputLayerWithActivations() {
		var inputData= new float[][] {
            {1f,2f,3f,2.5f},
            {2f,5f,-1f,2f},
            {-1.5f,2.7f,3.3f,-0.8f}
		};

		// we define our pure pipeline without any test-logic
		var pipelineUnderTest=Pipeline.fromProducer(new FloatArrayProducer<NDArray>(inputData))
								.throughInputLayer(DenseLayer.ofSize(5),3)
								.activate(reLU)
								.throughLayer(DenseLayer.ofSize(5))
								.activate(softMax)
								.log();
		
			// add assertions to the pipelline
			pipelineUnderTest
				.then(array->assertNotNull(array))
				.map(NDArray::getShape)
				.then(shape->assertEquals(3,shape.get(0)))
				.then(shape->assertEquals(5,shape.get(1)));

			//now start the pipeline to run the tests
			pipelineUnderTest.execute();
			
	}
	
	
}
