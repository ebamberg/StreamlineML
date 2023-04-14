package de.ebamberg.streamline.experimental.lsm;

import java.io.IOException;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSTable {

	private static Logger log=LoggerFactory.getLogger(SSTable.class);

	private SSTableStorage activeStore;
	
	private volatile SortedSet<SSTableStorage> persistedStores;

	private int maxEntriesInMemory=1000;
	
	private ExecutorService executor;
	
	public SSTable() {
		activeStore=new SSTableInMemoryStorage();
		persistedStores=new TreeSet<SSTableStorage>();
		executor=Executors.newFixedThreadPool(1);
	}

	public SSTable(int maxEntriesInMemory) {
		activeStore=new SSTableInMemoryStorage();
		this.maxEntriesInMemory=maxEntriesInMemory;
		persistedStores=new TreeSet<SSTableStorage>();
		executor=Executors.newFixedThreadPool(1);
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
						
						return new SSTablePersistentStorage(((SSTableInMemoryStorage)passiveStore).store);
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
