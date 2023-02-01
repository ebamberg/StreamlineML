package de.ebamberg.streamline.ml.layer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ai.djl.ndarray.NDManager;

public class InitializerTest {

		@Test
		public void testRandom() {
			try (var manager=NDManager.newBaseManager()) {

				var init=Initializer.random();
				var data=init.init(manager, 2,3);
				assertEquals(2,data.getShape().get(0));
				assertEquals(3,data.getShape().get(1));
			}
		}
	
}
