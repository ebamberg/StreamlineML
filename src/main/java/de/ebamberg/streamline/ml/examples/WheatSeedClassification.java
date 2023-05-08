package de.ebamberg.streamline.ml.examples;

import java.net.MalformedURLException;

import de.ebamberg.streamline.ml.data.Feature;
import de.ebamberg.streamline.ml.data.Role;
import de.ebamberg.streamline.ml.data.Schema;
import de.ebamberg.streamline.ml.data.reader.CSVDataset;
import de.ebamberg.streamline.ml.layer.DenseLayer;


/**
 * 
 * 	Classification Training on Seed Dataset
 * 
 * 	http://archive.ics.uci.edu/ml/datasets/seeds
 * 
 * features:
		 1. area A,
		 2. perimeter P,
		 3. compactness C = 4*pi*A/P^2,
		 4. length of kernel,
		 5. width of kernel,
		 6. asymmetry coefficient
		 7. length of kernel groove.
 * 
 * @author erik.bamberg@web.de
 *
 */
public class WheatSeedClassification {

	public static void main(String...args) throws MalformedURLException {
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
		.throughInputLayer(DenseLayer.ofSize(5), 10)
		.execute();
		
	}
	
}
