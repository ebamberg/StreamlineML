package de.ebamberg.streamline.experimental.lsm;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSTablePersistentStorage implements SSTableStorage {

	private static Logger log=LoggerFactory.getLogger(SSTablePersistentStorage.class);
	
	private static final int SPARSE_INDEX_RATION = 1;
	private static final int INDEX_ENTRYSIZE = 16;
	
	private long timestamp;
	Path filename;
	
	SSTablePersistentStorage(String tableName, TreeMap<Long,String> store) throws IOException {
		timestamp=System.currentTimeMillis();
		initializeDatabaseFolder();
		filename=SSTable.databaseFolder.resolve(String.format("part-%s-%d",tableName, timestamp));
		persist(store);
	}
	
	
	public SSTablePersistentStorage(Path f) throws IOException {
		filename=f;
		try (var file=new RandomAccessFile(filename.toFile(), "r")) {
			var header=new byte[4];
			file.read(header);
			var version=file.readInt();
			timestamp=file.readLong();
			var dataChunkStart=file.readLong();
			var indexChunkStart=file.readLong();
			var numEntries=file.readInt();
		}
	}

	
	private void initializeDatabaseFolder() throws IOException {
		if (!Files.exists(SSTable.databaseFolder)) {
			Files.createDirectories(SSTable.databaseFolder);
		}
		
	}

	
	@Override
	public String get(long key) {
		return (String)query(key);
	}

	@Override
	public String put(Long key, String value) {
		throw new RuntimeException("operation not supported. Storage is inmutable.");
	}
	
	
	public void persist(Map<Long,String> passiveStore) throws IOException {
		
		var numEntries=passiveStore.size();
		var index2position=new long[numEntries];
		
		try (var file=new RandomAccessFile(filename.toFile(), "rw")) {
			file.writeBytes("SSTA");					// Magic Cookie
			file.writeInt(1);							// version
			file.writeLong(timestamp);	// timestamp
			var chunkPointerStart=file.getFilePointer();
			file.writeLong(0l);							// start of datachunk position in this file
			file.writeLong(0l);							// start of indexchunk position in this file
			file.writeInt(numEntries);

			// start of dataChunk
			var dataChunkStart=file.getFilePointer();
			//write the dataChunk
			var i=0;
			for (var entry : passiveStore.entrySet()) {
				index2position[i]=file.getFilePointer();
				// serialze key an value
				serializeKey(file,entry.getKey());
				serializeValue(file,entry.getValue());
				i++;
			}
			var indexChunkStart=file.getFilePointer();
			// write index
			i=0;
			for (var key: passiveStore.keySet()) {
				if (i%SPARSE_INDEX_RATION==0) {
					serializeKey(file,key);
					file.writeLong(index2position[i++]);
				}
			}
			file.seek(chunkPointerStart);
			file.writeLong(dataChunkStart);
			file.writeLong(indexChunkStart);
		}
				
		
	}
	
	private Object query(long queryFor) {
		Object result=null;
		try (var file=new RandomAccessFile(filename.toFile(), "r")) {
			//TODO store header in instance variable during inititalization. don't read header all the time
			var header=new byte[4];
			file.read(header);
			var version=file.readInt();
			var timestamp=file.readLong();
			var dataChunkStart=file.readLong();
			var indexChunkStart=file.readLong();
			var numEntries=file.readInt();
			
			file.seek(indexChunkStart);
			
			
			var range=new int[2];	// start/end position for the current search range
			range[0]=0; 
			range[1]=numEntries;
			
			long key=-1;
			
			while(true)  {	// if range distance is 0 then there is no where to go
				var midIndex=range[0]+(range[1]-range[0])/2;
				var filePos=indexChunkStart+midIndex*INDEX_ENTRYSIZE;
				file.seek(filePos);
				key=deserializeKey(file);
				log.trace("checking key:{}",key);
				if (key==queryFor) {
					break;
				} else if (key>queryFor) {
					if (midIndex==range[1] || range[0]==range[1]) {	// key not found
						key=-1;
						break;
					}
					range[1]=midIndex;
				} else if (key<queryFor) {
					if (midIndex==range[0] || range[0]==range[1]) { // key not found
						key=-1;
						break;
					}
					range[0]=midIndex;
				}
			} 
			log.trace("key found:{}",key);
			
			// no load the data
			if (key>-1) {
				var dataPos=file.readLong();
				file.seek(dataPos);
				var loadedKey=deserializeKey(file);
				result=deserializeValue(file);
			}
			
		} catch (IOException e) {
			log.error("fatal error reading persistent sstable",e);
			return null;
		}
		return result;
	}

	private void serializeValue(RandomAccessFile file, String value) throws IOException {
		file.writeUTF(value);
	}

	private void serializeKey(RandomAccessFile file, Long key) throws IOException {
		file.writeLong(key);
	}
	
	private Long deserializeKey(RandomAccessFile file) throws IOException {
		return file.readLong();
	}
	
	private Object deserializeValue(RandomAccessFile file) throws IOException {
		return file.readUTF();
	}

	@Override
	public int size() {
		try (var file=new RandomAccessFile("part-01", "r")) {
			file.seek(32);
			return file.readInt();
		} catch (IOException e) {
			log.error("error reading persistent storage",e);
			return 0;
		}
	}

	@Override
	public int compareTo(SSTableStorage o) {
		if (o instanceof SSTablePersistentStorage) {
			// ordered by timestamp newer storage first
			return (int) (((SSTablePersistentStorage) o).timestamp-this.timestamp);
		} else {
			return +1; // persistent storages always comes last
		}
	}

	@Override
	public void drop() {
		try {
			Files.deleteIfExists(filename);
		} catch (IOException e) {
			log.error("cannot delete database data file {}",filename,e);
		}
		
	}

	
	
}
