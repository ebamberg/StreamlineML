package de.ebamberg.streamline.experimental.lsm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSTable {

	private static Logger log=LoggerFactory.getLogger(SSTable.class);

	private String tableName;
	
	private SSTableStorage activeStore;
	
	private volatile SortedSet<SSTableStorage> persistedStores;

	private int maxEntriesInMemory=1000;
	
	private ExecutorService executor;
	
	public static final Path databaseFolder=Path.of("./.db/"); 
	
	public SSTable(String tableName) throws IOException {
		this.tableName=tableName;
		activeStore=new SSTableInMemoryStorage();
		persistedStores=new TreeSet<SSTableStorage>();
		executor=Executors.newFixedThreadPool(1);
		initializeTableFiles();
	}

	public SSTable(String tableName,int maxEntriesInMemory) throws IOException {
		this.tableName=tableName;
		this.maxEntriesInMemory=maxEntriesInMemory;
		activeStore=new SSTableInMemoryStorage();
		persistedStores=new TreeSet<SSTableStorage>();
		executor=Executors.newFixedThreadPool(1);
		initializeDatabaseFolder();
		initializeTableFiles();
	}
	
	private void initializeDatabaseFolder() throws IOException {
		if (!Files.exists(databaseFolder)) {
			Files.createDirectories(databaseFolder);
		}
		
	}

	//TODO proper error handling for IOExceptions
	private void initializeTableFiles() throws IOException {		
		String filePrefix=String.format("part-%s-",tableName);
		Files.find(databaseFolder, 1, (file,attributes)->file.getFileName().startsWith(filePrefix) )
			.map(f-> {
				try {
					return new SSTablePersistentStorage(f);
				} catch (IOException e) {
					log.error("fatal error opening database files",e);
					
				}
				return null;
			})
			.filter(Objects::isNull)
			.forEach(storage->persistedStores.add(storage));
	}
	
	/**
	 * drop table
	 * 
	 * deletes all table data files. The table will be empty afterwards
	 */
	public void drop() {
		log.info("dropping table {}", tableName);
		persistedStores.forEach(SSTableStorage::drop);
	}

	/**
	 * @param key
	 * @return
	 */
	public String get(long key) {
		var result= activeStore.get(key);
		if (result==null) {
			result= persistedStores.stream()
							.map(store->store.get(key))
							.filter(Objects::nonNull)
							.findFirst()
							.orElse(null);
		}
		return result;
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public String put(Long key, String value) {
		
		if (activeStore.size()>=maxEntriesInMemory) {
				var passiveStore=activeStore;
				activeStore=new SSTableInMemoryStorage();
				CompletableFuture.supplyAsync( () -> {
					try {
						log.info("flush in-memoactiveStorery key-value store to disk");
						
						return new SSTablePersistentStorage(tableName,((SSTableInMemoryStorage)passiveStore).store);
					} catch (IOException e) {
						log.error("fatal error persisting key value store ",e);
						throw new RuntimeException("file error while persisting key-value storage. This will cause data lost. ",e);
					}
				},executor)
					.thenAccept( ps-> {
						log.info("flushed to disk. register diskfile to ss-table");
						persistedStores.add(ps);
					})
					.exceptionally(e-> { log.error("file error while persisting key-value storage. This will cause data lost.",e);
										return null;
									});

			
		}
		
		return activeStore.put(key, value);
	}





	
}
