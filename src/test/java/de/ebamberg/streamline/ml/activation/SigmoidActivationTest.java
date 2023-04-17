package de.ebamberg.streamline.ml.activation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ai.djl.ndarray.NDManager;

public class SigmoidActivationTest {

	@Test
	public void testActivation() {
	 
		try (var manager=NDManager.newBaseManager()) {
			var activation=new SigmoidActivation();

			var input=manager.create(new float[][] {
										{0.458f, 2f,3f,2.5f},
										{2f,5f,-1f,2f},
										{-1.5f,2.7f,3.3f,-0.8f} 
										});

			var output=activation.forward(input);
			
			assertEquals(0.6125f, output.getFloat(0,0),0.0001 );

			
			System.out.printf("soft max activation output %s\n",output.toString());

			
		}
	 
	}
	
	@Test
	public void testActivationBackProbagation() {
	 
		try (var manager=NDManager.newBaseManager()) {
			var activation=new SigmoidActivation();

			var input=manager.create(new float[][] {
										{0.458f, 2f,3f,2.5f},
										{2f,5f,-1f,2f},
										{-1.5f,2.7f,3.3f,-0.8f} 
										});
			var output=activation.forward(input);
			var backpropagated=activation.backward(output);
			
			assertEquals(0.2373f, backpropagated.getFloat(0,0),0.0001 );
			assertEquals(0.1050f, backpropagated.getFloat(1,0),0.0001 );
			

			
			System.out.printf("soft max activation output %s\n",backpropagated.toString());

			
		}
	 
	}

}
