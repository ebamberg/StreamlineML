
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
					