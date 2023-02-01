package de.ebamberg.streamline.ml.activation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import ai.djl.ndarray.NDManager;

public class TanhActivationTest {

	@Test
	public void testActivation() {
	 
		try (var manager=NDManager.newBaseManager()) {
			var activation=new TanhActivation();
			
			var input=manager.create(new float[][] {{4.8f, 1.21f, 2.385f}});
			var expected=new float[] {0.9999f, 0.8367f, 0.9832f};
			var output=activation.forward(input);
			System.out.printf("tanh activation output %s\n",output.toString());
			IntStream.range(0, expected.length)
				.forEach(i-> assertEquals(expected[i],output.getFloat(0,i),0.0001));
			

			
		}
	 
	}
	
}
