package de.ebamberg.streamline.ml.activation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import ai.djl.ndarray.NDManager;

public class SoftMaxActivationTest {

	@Test
	public void testActivation() {
	 
		try (var manager=NDManager.newBaseManager()) {
			var activation=new ReLUActivation();
			
			var input=manager.create(new float[][] {{4.8f, -1.21f, 2.385f, 0.0f}});
			var expected=new float[] {4.8f, 0f, 2.385f, 0f};
			var output=activation.forward(input);
			IntStream.range(0, expected.length)
				.forEach(i-> assertEquals(expected[i],output.getFloat(0,i),0.0001));
			
			System.out.printf("reLU activation output %s\n",output.toString());

			
		}
	 
	}
	
}
