
Machine learning algorithm implemented in easy to understand clean object-oriented java.

StreamlineML uses an easy to learn and easy to read  
**FluentAPI** to make dataflow and learning process in **machine-learning** algorithms clear. 

# Getting Started

## Data Reading and Trnasformation
### reading data from CSV

~~~
			var dataset=CSVDataset
						.from("file://csvDatasetTest.csv")
						.read()
						.log()
						.execute();
~~~				

### building catalog feature

we can read a csv file for intention classification and convert the feature intent into a categorical feature. A dictionary for the categories is build. 



~~~
		var dataset=CSVDataset
					.fromResource("/intentions.csv")
					.read()
					.categorize("intent")
					.log()
					.execute();
~~~	

input data

|intent   |sample                   |
|---------|-------------------------|
|greeting |hello                    |
|greeting |Good morning             |
|greeting |Good afternoon           |
|greeting |Hi                       |
|ls       |list content of directory|
|ls       |list folder              |
|ls       |list current folder      |
|ls       |let's see what we have   |
|playmusic|play music               |
|playmusic|play a song              |
|playmusic|play a tune              |



## dealing with tabular data

when dealing with tabular data we often want to transform the values in a specific column.
We can select a column with "withFeature" and define a pipeline to transform the specific values of column.  

Alternative we access a specific column using the record object itself which represents a row of data

|code                                                          | description                               |
|--------------------------------------------------------------|-------------------------------------------|
|record.getValue(featurename)                                  | get a Value                               |
|record.updateValue(featurename, newValue)                     | update the Value     					   |
|record.updateValue(featurename, value->value.substring(0,5) ) | update the Value using a lambda function  |


~~~
			CSVDataset
				.fromResource("csvDatasetTest.csv")
				.read()
				.withFeature("header2", feature->feature
									.cast(String.class)
									.map(v->v.substring(0,1))
									.log() 
							)
				.then(record->  record.updateValue("header2","overridden literal value")  )
				.log()
				.execute();	
~~~				
					
## Machine learning Layers
### simple classification example
~~~
		var inputData= new float[][] {
            {1f,2f,3f,2.5f},
            {2f,5f,-1f,2f},
            {-1.5f,2.7f,3.3f,-0.8f}
		};

		Pipeline.fromProducer(new FloatArrayProducer<NDArray>(inputData))
			.throughInputLayer(DenseLayer.ofSize(5),3)
			.activate(reLU)
			.throughLayer(DenseLayer.ofSize(5))
			.activate(softMax)
			.log()
			.execute();

~~~				

## Unit Testing of Pipelines

we can define our pipeline which is decoupled from unit test assertion.
To use the pipeline in  unittest we can attach 1..n pipeline branches
execution the unittest assertion.
Then we execute the pipeline.
Test data get pushed through the pipeline and the result get splitted and pushed through all assertions branches.

~~~
	@Test
	public void testDenseInputLayerWithActivation() {
		var inputData= new float[][] {
            {1f,2f,3f,2.5f},
            {2f,5f,-1f,2f},
            {-1.5f,2.7f,3.3f,-0.8f}
		};

		// we define our pure pipeline without any test-logic
		var pipelineUnderTest=Pipeline.fromProducer(new FloatArrayProducer<NDArray>(inputData))
								.inputLayer(new DenseLayer(),3)
								.layer(new ReLUActivation())
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

~~~