
Machine learning algorithm implemented in easy to understand clean object-oriented java.

StreamlineML uses an easy to learn and easy to read  
**FluentAPI** to make dataflow and learning process in **machine-learning** algorithms clear. 

StreamlineML comes with out-of-the-box modules to use **Cloud Machine Learning services** like HuggingFace.
To start a working machine learning application was never so easy then now. 

A constant API allows to swap cloud service with self implemented and trained neuronal networks at any time.


- [Getting Started](#getting-started)
  * [Data Reading and Transformation](#data-reading-and-transformation)
    + [reading data from CSV](#reading-data-from-csv)
    + [reading a text file](#reading-a-text-file)
    + [building catalog feature](#building-catalog-feature)
  * [dealing with tabular data](#dealing-with-tabular-data)
  * [Using Machine Learning Cloud Services](#using-machine-learning-cloud-services)
    + [Hugging Face Module](#hugging-face-module)
    + [Vectorize a String using Hugging Face Cloud](#vectorize-a-string-using-hugging-face-cloud)
  * [Machine learning Layers](#machine-learning-layers)
    + [simple classification example](#simple-classification-example)
  * [Unit Testing of Pipelines](#unit-testing-of-pipelines)
- [Utility Functions](#utility-functions)
  * [Vector Distance and Vector Similarity](#vector-distance-and-vector-similarity)
- [Experimental Features - features in development](#experimental-features---features-in-development)
  * [high write-throughput persistent key-value Feature Store.](#high-write-throughput-persistent-key-value-feature-store)



# Getting Started

## Data Reading and Transformation
### reading data from CSV

~~~
			var dataset=CSVDataset
						.from("file://csvDatasetTest.csv")
						.read()
						.log()
						.execute();
~~~	

We can also define our our schema giving the name of the columns, the role this column plays in the training/inference process like "Feature" or "Label" or just "ignore" this column and a datatype for this column.

~~~
		CSVDataset.from("https://raw.githubusercontent.com/jbrownlee/Datasets/master/wheat-seeds.csv")
		.withFirstRecordHasHeader(false)
		.withSchema( Schema.of(
				Feature.of("area",Role.FEATURE,Float.class),
				Feature.of("perimeter",Role.FEATURE,Float.class),
				Feature.of("compactness",Role.IGNORE,Float.class),
				Feature.of("kernel_length",Role.IGNORE,Float.class),
				Feature.of("kernel_width",Role.IGNORE,Float.class),
				Feature.of("coefficient_asymmetry",Role.IGNORE,Float.class),
				Feature.of("kernelgroove_length",Role.IGNORE,Float.class),
				Feature.of("class",Role.CLASSLABEL,Float.class)
				
				) )
		.read()
		.log()
		.execute();
~~~

### reading a text file
~~~
		TextualDataset.fromResource("/simpleMaskedSentences.txt")
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
				
				
## Using Machine Learning Cloud Services

StreamlineML comes with out-of-the-box modules to use Cloud Machine Learning service like HuggingFace.

### Hugging Face Module

please set your HuggingFace API key in the environment variable HUGGINGFACE_APIKEY of your OS before using the services.

__https://api-inference.huggingface.co/__ is used as default host for inference but can overridden with a JVM parameter at starttime

please use
~~~
 -DHUGGINGFACE_HOST=....
~~~
 to set a different host if required.

The following code sample read line by line the text file and calls the cloud hosted xlm-roberta-base model to find possible matches
for the <mask> token in each sentence. 


input dataset

~~~
The answer of the universe is <mask>.
This is not my cup of <mask>.
~~~

TextualDataset reads each line of the input text into a new record with the feature "lines". The input text file is read from the classpath (@see "fromResource()").

|lines                                     |
|------------------------------------------|
|The answer of the universe is \<mask\>.     |
|This is not my cup of \<mask\>.             |



~~~
		TextualDataset.fromResource("/simpleMaskedSentences.txt")
			.read()
			.map(r->(String)r.getValue("lines"))
			.predict(HuggingFaceCloud.xlmRobertaBase)
			.log()
~~~			

or if we we have tabulatur data like multiple columns/features from a csv file, we can call the prediction for a particular feature/column of this dataset.
In this case we are calling the xlmRobertaBase on HuggingFace for each value from the column "lines", the result replaces the value in the "line" column
and keep the other features/columns of the dataset untouched.

Please also notice that though the use of static import from HuggingFaceCloud, the syntax gets even simplier.
we just say `predict("lines",xlmRobertaBase)`. 
Let's think of other models like vectorizers in this case we can vectorize the strings in the column "lines with `.predict("lines",text2Vec_AllMiniLM2)`.
After the call the column "lines" will contains the vectorized String.
  
~~~

import static de.ebamberg.streamline.ml.modules.huggingface.HuggingFaceCloud.*;

...

		TextualDataset.fromResource("/simpleMaskedSentences.txt")
			.read()
			.predict("lines",xlmRobertaBase)
			.log()
			.execute();
~~~	

the output of the example will be something like this:
```
13:24:36.848 [main] INFO Pipeline -- pipeline stage: This is not my cup of <mask>.
13:24:37.779 [main] INFO Pipeline -- pipeline stage: [TokenPropability [score=0.9401772, token=26156, sequence=This is not my cup of tea.], TokenPropability [score=0.05441865, token=79497, sequence=This is not my cup of coffee.], TokenPropability [score=0.0016887067, token=86963, sequence=This is not my cup of cake.], TokenPropability [score=9.179833E-4, token=41851, sequence=This is not my cup of Tea.], TokenPropability [score=3.8005377E-4, token=135124, sequence=This is not my cup of espresso.]]
```

As we can clearly see `This is not my cup of tea.` wins this prediction followed by `This is not my cup of coffee.`

### Vectorize a String using Hugging Face Cloud

~~~
		import static de.ebamberg.streamline.ml.modules.huggingface.HuggingFaceCloud.*;

		TextualDataset.fromResource("/simpleMaskedSentences.txt")
			.read()
			.predict("lines",text2Vec_AllMiniLM2)
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
			.transformOutput(o->new Classification(o))
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
				.transformOutput(NDArray::getShape)
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

# Utility Functions

## Vector Distance and Vector Similarity

~~~

		try (var manager=NDManager.newBaseManager()) {
			var vector1=manager.create(new float[][] {{3,4},{7,8}});
			assertEquals( 0f, VectorDistance.cosineDistance(vector1, vector1) );
		}
~~~

~~~

		try (var manager=NDManager.newBaseManager()) {
			var vector1=manager.create(new float[][] {{3,4},{7,8}});
			var vector2=manager.create(new float[][] {{1,2},{7,8}});
			assertEquals( 0.97f, VectorDistance.cosineSimilarity(vector1, vector2),0.01f );
		}
~~~

# Experimental Features - features in development

## high write-throughput persistent key-value Feature Store.

the Feature Store is Map-structure persisting data when a in-memory threshold is reached.

LSM-Tree is used to persist the data which guarantees high write throughput with the cost
of eventual consistency.

The Feature is under development at the moment.   

~~~

		var table=new SSTable(5);
		table.put(4711l,"Hello");
		table.put(56787l,"World");
	
		var text=table.get(4711l);
		assertEquals("Hello",text);
		

~~~
