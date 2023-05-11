package de.ebamberg.streamline.ml.data;

import org.junit.jupiter.api.Test;

import de.ebamberg.streamline.ml.data.NamedVectors.TYPE;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NamedVectorsTest {

	@Test
	public void testNamedVectorsBaseFunctionality() {
		var x=new float[] {1.0f,2.0f};
		var y=new float[] {3.0f,4.0f};
				
		var vectors=NamedVectors.ofXandY(x, y);
		
		assertEquals(x,vectors.get(NamedVectors.TYPE.X).get());
		assertEquals(y,vectors.get(NamedVectors.TYPE.Y).get());
	}
	
	@Test
	public void testNamedVectorsEmptyAndPutByKnownName() {
		var x=new float[] {1.0f,2.0f};
				
		var vectors=NamedVectors.empty();
		vectors.put(TYPE.X, x);
		
		assertEquals(x,vectors.get(NamedVectors.TYPE.X).get());
		assertEquals(null,vectors.get(NamedVectors.TYPE.Y).orElse(null));
	}

}
