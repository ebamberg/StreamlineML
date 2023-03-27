package de.ebamberg.streamline.ml.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class SchemaTest {

	@Test
	public void testSchemaIndexOf() {
		var schema=new Schema(new String[] {"col1","col2"});
		assertEquals(0, schema.indexOf("col1"));
		assertEquals(1, schema.indexOf("col2"));
		assertEquals(-1, schema.indexOf("unknown"));
	}
	
	@Test
	public void testSchemaCopyConstructor() {
		var schema=new Schema(new String[] {"col1","col2"});
		var schemaNew=new Schema(schema);
		assertEquals(0, schemaNew.indexOf("col1"));
		assertEquals(1, schemaNew.indexOf("col2"));
	}

	@Test
	public void testSchemaForEachIsInOrder() {
		var schema=new Schema(new String[] {"1","2","3"});
		var expected=new AtomicInteger(1);
		schema.forEach( h-> assertEquals( expected.getAndIncrement() ,Integer.valueOf(h.getName())));
	}
	
}
