package io.segment.cache;

import io.neural.extension.NPI;
import io.segment.cache.exception.CacheException;
import io.segment.cache.support.CacheExpiredListener;

import java.util.Properties;

/**
 * The Cache Factory.
 * 
 * @author lry
 */
@NPI
public interface CacheFactory {

	/**
	 * Start cache factory
	 * 
	 * @param props
	 * @throws CacheException
	 */
	void start(Properties props) throws CacheException;

	/**
	 * Build configure the cache
	 * 
	 * @param regionName
	 * @param autoCreate
	 * @param listener
	 * @return
	 * @throws CacheException
	 */
	Cache buildCache(String regionName, boolean autoCreate, CacheExpiredListener listener) throws CacheException;

	/**
	 * Stop cache factory
	 */
	void stop();

}
