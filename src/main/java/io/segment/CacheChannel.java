package io.segment;

import io.neural.extension.NPI;
import io.segment.support.CacheException;
import io.segment.support.CacheObject;

import java.util.List;

/**
 * Cache Channel
 * 
 * @author lry
 */
@NPI
public interface CacheChannel {

	public final static byte LEVEL_1 = 1;
	public final static byte LEVEL_2 = 2;
	
	/**
	 * Get data in cache
	 * 
	 * @param region Cache Region name
	 * @param key Cache key
	 * @return cache object
	 */
	CacheObject get(String region, Object key);
	
	/**
	 * Write cache
	 * 
	 * @param region Cache Region name
	 * @param key Cache key
	 * @param value Cache value
	 */
	void set(String region, Object key, Object value);
	
	/**
	 * Write to cache and set expiration time
	 * 
	 * @param region
	 * @param key
	 * @param value
	 * @param expireInSec
	 */
	void set(String region, Object key, Object value, Integer expireInSec);

	/**
	 * The delete cache
	 * 
	 * @param region  Cache Region name
	 * @param key Cache key
	 */
	public void evict(String region, Object key) ;

	/**
	 * The batch cache
	 * 
	 * @param region Cache region name
	 * @param keys Cache key
	 */
	@SuppressWarnings({ "rawtypes" })
	void batchEvict(String region, List keys) ;

	/**
	 * Clear the cache
	 * 
	 * @param region Cache region name
	 */
	void clear(String region) throws CacheException ;
	
	/**
	 * Get cache region keys
	 * @param region Cache region name
	 * @return key list
	 */
	@SuppressWarnings("rawtypes")
	List keys(String region) throws CacheException ;

	/**
	 * Close to channel connection
	 */
	void close() ;
	
}
