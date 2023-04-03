#Getting Started

## reading data from CSV

~~~
			var dataset=CSVDataset
						.fromURL(new URL("file://csvDatasetTest.csv"))
						.read()
						.log()
						.execute();
~~~					
					