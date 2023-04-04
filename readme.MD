
Machine learning algorithm implemented in easy to understand clean object-oriented java.

StreamlineML uses an easy to learn and easy to read  
**FluentAPI** to make dataflow and learning process in **machine-learning** algorithms clear. 

# Getting Started

## reading data from CSV

~~~
			var dataset=CSVDataset
						.from("file://csvDatasetTest.csv")
						.read()
						.log()
						.execute();
~~~				

## building catalog feature

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
				
					

					