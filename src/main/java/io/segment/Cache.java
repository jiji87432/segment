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

	Object get(Object key) throws CacheException;

	void put(Object key, Object value) throws CacheException;

	void put(Object key, Object value, Integer expireInSec)throws CacheException;

	void update(Object key, Object value) throws CacheException;

	void update(Object key, Object value, Integer expireInSec)throws CacheException;

	List<?> keys() throws CacheException;

	void evict(Object key) throws CacheException;

	void evict(List<?> keys) throws CacheException;

	void clear() throws CacheException;

	void destroy() throws CacheException;

}
