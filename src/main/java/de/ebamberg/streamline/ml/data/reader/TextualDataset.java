package de.ebamberg.streamline.ml.data.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ebamberg.streamline.ml.api.DataReadException;
import de.ebamberg.streamline.ml.data.Record;
import de.ebamberg.streamline.ml.data.Schema;
import de.ebamberg.streamline.ml.data.pipeline.DataReader;

public class TextualDataset implements DataReader<Record> {

	private static Logger log=LoggerFactory.getLogger(TextualDataset.class);
	
	private Supplier<Reader> readerSupplier;
	private Schema schema;
	
	protected TextualDataset(Supplier<Reader> readerSupplier) {
		super();
		this.readerSupplier = readerSupplier;
	}

	@Override
	public Iterator<Record> iterator() {
		final BufferedReader in = new BufferedReader(readerSupplier.get());;
		try  {
			var innerIterator=in.readLine();
			
			Schema currentSchema;
			if (this.schema!=null) {
				currentSchema=schema;
			} else {
				currentSchema=new Schema(new String[] {"lines"});
			}
			
			return new Iterator<Record>() {
				String currentline;
				@Override
				public boolean hasNext() {
					try {
						do {
							currentline=in.readLine();
						} while (currentline!=null && currentline.isBlank());
					} catch (IOException e) {
						throw new DataReadException("error while reading data",e);
					}
					return currentline!=null;
				}

				@Override
				public Record next() {
					var rec=new Record (currentSchema);

					currentSchema.forEach( f-> {	
						if ("lines".equals(f.getName()))  {
							rec.addValue(null, currentline);
						} else {
							rec.addValue(null, "");
						}
					} ) ;
					
					return rec;
				}
				
			};
		} catch (IOException e) {
			if (in!=null ) {
				try {
					in.close();
				} catch (IOException e1) {
					log.warn("error while closing input resource",e1);
				}
			}
			throw new DataReadException("error while reading data",e);
		} 
	}

	protected TextualDataset self() {
		return this;
	}

	public TextualDataset withSchema(Schema schema) {
		this.schema = schema;
		return self();
	}

	public static TextualDataset from(Path path) {
		return fromPath(path);
	}

	public static TextualDataset from(URL url) {
		return fromURL(url);
	}

	public static TextualDataset from(String url) throws MalformedURLException {
		return from(new URL(url));
	}

	public static TextualDataset fromPath(Path path) {
		return new TextualDataset(() -> {
			try {
				return new InputStreamReader(new FileInputStream(path.toFile()));
			} catch (FileNotFoundException e) {
				throw new IllegalArgumentException(e.getMessage(), e);
			}
		});
	}

	public static TextualDataset fromURL(URL url) {
		return new TextualDataset(() -> {
			try {
				return new InputStreamReader(url.openStream());
			} catch (IOException e) {
				throw new IllegalArgumentException(e.getMessage(), e);
			}
		});

	}

	public static TextualDataset fromResource(String resourcePath) {
		return fromURL(TextualDataset.class.getResource(resourcePath));
	}

}
