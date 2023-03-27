package de.ebamberg.streamline.ml.activation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import ai.djl.ndarray.NDManager;

public class ReLUActivationTest {

	@Test
	public void testActivation() {
	 
		try (var manager=NDManager.newBaseManager()) {
			var activation=new SoftMaxActivation();
			
			var input=manager.create(new float[][] {{4.8f, 1.21f, 2.385f}});
			var expected=new float[] {0.8953f, 0.0247f, 0.08f};
			var output=activation.forward(input);
			IntStream.range(0, expected.length)
				.forEach(i-> assertEquals(expected[i],output.getFloat(0,i),0.0001));
			
			System.out.printf("soft max activation output %s\n",output.toString());

			
		}
	 
	}
	
}
