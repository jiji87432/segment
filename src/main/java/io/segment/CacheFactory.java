package io.segment;

import io.neural.extension.NPI;
import io.segment.exception.CacheException;
import io.segment.support.CacheExpiredListener;

import java.util.Properties;

/**
 * Support for pluggable caches.
 * 
 * @author lry
 */
@NPI
public interface CacheFactory {

	/**
	 * Cache ID name
	 * 
	 * @return return cache provider name
	 */
	String name();
	
	/**
	 * Configure the cache
	 *
	 * @param regionName the name of the cache region
	 * @param autoCreate autoCreate settings
	 * @param listener listener for expired elements
	 * @return return cache instance
	 * @throws CacheException cache exception
	 */
	Cache buildCache(String regionName, boolean autoCreate, CacheExpiredListener listener) throws CacheException;

	/**
	 * Callback to perform any necessary initialization of the underlying cache implementation
	 * during SessionFactory construction.
	 *
	 * @param props current configuration settings.
	 */
	void start(Properties props) throws CacheException;

	/**
	 * Callback to perform any necessary cleanup of the underlying cache implementation
	 * during SessionFactory.close().
	 */
	void stop();
	
}
