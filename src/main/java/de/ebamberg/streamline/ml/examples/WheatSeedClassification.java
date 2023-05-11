package de.ebamberg.streamline.ml.examples;

import java.net.MalformedURLException;

import de.ebamberg.streamline.ml.activation.Activation;
import de.ebamberg.streamline.ml.data.Feature;
import de.ebamberg.streamline.ml.data.Role;
import de.ebamberg.streamline.ml.data.reader.CSVDataset;
import de.ebamberg.streamline.ml.layer.DenseLayer;
import static de.ebamberg.streamline.ml.data.Feature.*;

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
		.withSchema( 
				feature("area",Float.class),
				feature("perimeter",Float.class),
				ignore("compactness",Float.class),
				ignore("kernel_length",Float.class),
				ignore("kernel_width",Float.class),
				ignore("coefficient_asymmetry",Float.class),
				ignore("kernelgroove_length",Float.class),
				classlabel("class",Float.class)
				)
		.read()
		.log()
		.throughInputLayer(DenseLayer.ofSize(2), 10)
		.log()
		.activate(Activation.sigmoid)
		.log()
		.execute();
		
	}
	
}
