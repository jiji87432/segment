package io.segment;

import io.neural.extension.NPI;
import io.segment.support.CacheException;

import java.util.List;

/**
 * The Cache.<br>
 * <br>
 * All implementors must be threadsafe.
 * 
 * @author lry
 */
@NPI
public interface Cache {

	/**
	 * Get an item from the cache, nontransactionally
	 * 
	 * @param key cache key
	 * @return the cached object or null
	 */
	Object get(Object key) throws CacheException;

	/**
	 * Add an item to the cache, nontransactionally, with failfast semantics
	 * 
	 * @param key cache key
	 * @param value cache value
	 */
	void put(Object key, Object value) throws CacheException;

	/**
	 * Add an item to the cache, nontransactionally, with failfast semantics
	 * 
	 * @param key
	 * @param value
	 * @param expireInSec expire time. (seconds)
	 * @throws CacheException
	 */
	void put(Object key, Object value, Integer expireInSec) throws CacheException;

	/**
	 * Add an item to the cache
	 * 
	 * @param key cache key
	 * @param value cache value
	 */
	void update(Object key, Object value) throws CacheException;

	/**
	 * Add an item to the cache
	 * 
	 * @param key
	 * @param value
	 * @param expireInSec expire time. (seconds)
	 * @throws CacheException
	 */
	void update(Object key, Object value, Integer expireInSec) throws CacheException;

	@SuppressWarnings("rawtypes")
	List keys() throws CacheException;

	/**
	 * @param key Cache key Remove an item from the cache
	 */
	void evict(Object key) throws CacheException;

	/**
	 * Batch remove cache objects
	 * 
	 * @param keys the cache keys to be evicted
	 */
	@SuppressWarnings("rawtypes")
	void evict(List keys) throws CacheException;

	/**
	 * Clear the cache
	 */
	void clear() throws CacheException;

	/**
	 * Clean up
	 */
	void destroy() throws CacheException;

}
