package de.ebamberg.streamline.ml.data.reader;

import java.awt.List;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.compress.utils.Lists;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import de.ebamberg.streamline.ml.data.Record;
import de.ebamberg.streamline.ml.data.Role;
import de.ebamberg.streamline.ml.data.Schema;
import de.ebamberg.streamline.ml.data.pipeline.DataReader;

public class CSVDataset implements DataReader <Record> {

	private Supplier<Reader> readerSupplier;
	private Map<String, Role> featureRoleMap; 
	private Schema schema;
	private boolean firstRecordHasHeader=true;
	private String[] fixedHeader;
	private char delimiter=',';
	private Class<?>[] fixedDatatypes;
	
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
	 
	 public CSVDataset withFirstRecordHasHeader(boolean firstRecordHasHeader) {
		 this.firstRecordHasHeader=firstRecordHasHeader;
		 return self();
	 }
	 
	 public CSVDataset withHeader(String... header) {
		 this.fixedHeader=header;
		 return self();
	 }
	 
	 public CSVDataset withDelimiter(char delimiter) {
		 this.delimiter=delimiter;
		 return self();
	 }
	 
		public CSVDataset withDataTypes(Class<?>... datatypes) {
			this.fixedDatatypes=datatypes;
			return self();		
		}


	public Iterator<Record> iterator()  {
	
		CSVParser parser;
		try {
			Reader in = readerSupplier.get();
			var format = CSVFormat.EXCEL.withDelimiter(delimiter);
			if (firstRecordHasHeader) {
				format=format.withFirstRecordAsHeader();
			} else {
				if (schema!=null) {
					format=format.withHeader(schema.getFeaturesNames().toArray(new String[] {}));
				} else {
					format=format.withHeader(fixedHeader);
				}
			}
			parser=format.parse(in);
			
			Schema currentSchema;
			if (this.schema!=null) {
				currentSchema=schema;
			} else {
				if (fixedDatatypes!=null)  {
					currentSchema=new Schema(parser.getHeaderNames(),fixedDatatypes);
				} else {
					currentSchema=new Schema(parser.getHeaderNames());
				}
			}
			var innerIterator=parser.iterator();
			return new Iterator<Record>() {
				@Override
				public boolean hasNext() {
					return innerIterator.hasNext();
				}

				@Override
				public Record next() {
					var csvrecord=innerIterator.next();
					var rec=new Record (currentSchema);
				
					currentSchema.forEach( f-> {
						
						if (csvrecord.isSet(f.getName()))  {
							rec.addValue(null, csvrecord.get(f.getName()));
						} else {
							rec.addValue(null, "");
						}
					} ) ;
					
					return rec;
				}
				
			};

		} catch (IOException e) {
			throw new RuntimeException("error reading csv input",e);
		}				
	}

	public static CSVDataset from(Path path) {
		return fromPath(path);
	}
	public static CSVDataset from(URL url) {
		return fromURL(url);
	}
	public static CSVDataset from(String url) throws MalformedURLException {
		return from(new URL(url));
	}
	
	public static CSVDataset fromPath(Path path) {
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
	
	public static CSVDataset fromResource(String resourcePath) {
		return fromURL(CSVDataset.class.getResource(resourcePath));
	}




	
}
