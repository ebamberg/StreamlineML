package de.ebamberg.streamline.ml.data.reader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import de.ebamberg.streamline.ml.data.Record;
import de.ebamberg.streamline.ml.data.Role;
import de.ebamberg.streamline.ml.data.Schema;

public class CSVDataset {

	private Supplier<Reader> readerSupplier;
	private Map<String, Role> featureRoleMap; 
	private Schema schema;
	
	
	protected CSVDataset(Supplier<Reader> readerSupplier) {
		super();
		this.readerSupplier = readerSupplier;
		this.featureRoleMap=new HashMap<>();
	}


	

	protected CSVDataset self() {
		return this;
	}

	 public CSVDataset withSchema (Schema schema) {
		 this.schema=schema;
		 return self();
	 }

	
	 public CSVDataset withFeatureAsRole (String featureLabel, Role role) {
		 featureRoleMap.put(featureLabel,role);
		 return self();
	 }

	public Stream<Record> stream() throws IOException {
	
		Reader in = readerSupplier.get();
		CSVParser parser = CSVFormat.EXCEL
				.withFirstRecordAsHeader().
				parse(in);
		
		Schema currentSchema;
		if (this.schema!=null) {
			currentSchema=schema;
		} else {
			currentSchema=new Schema(parser.getHeaderNames());
		}
		return StreamSupport.stream(parser.spliterator(), false)
				.map(csvrecord-> {
					var rec=new Record (currentSchema);
				
					currentSchema.forEach( f-> {
						
						if (csvrecord.isSet(f.getName()))  {
							rec.addValue(null, csvrecord.get(f.getName()));
						} else {
							rec.addValue(null, "");
						}
					} ) ;
					
					return rec;
				});
		
	}

	public static CSVDataset fromLocalFile(Path path) {
		return new CSVDataset( () -> {
			try {
				return new FileReader(path.toFile());
			} catch (FileNotFoundException e) {
				throw new IllegalArgumentException(e.getMessage(),e);
			}
		}) ;
	} 

	
	
	public static CSVDataset fromURL(URL url) {
		return new CSVDataset( () -> {
			try {
				return new InputStreamReader(url.openStream());
			} catch (IOException e) {
				throw new IllegalArgumentException(e.getMessage(),e);
			}
		}) ;
		
	}
	
}
