package io.segment.cache.ehcache;

import io.neural.extension.Extension;
import io.segment.cache.Cache;
import io.segment.cache.exception.CacheException;
import io.segment.cache.support.CacheExpiredListener;

import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

@Extension("eh")
public class EhCache implements Cache, CacheEventListener {
	
	private net.sf.ehcache.Cache cache;
	private CacheExpiredListener listener;

	/**
	 * Creates a new Hibernate pluggable cache based on a cache name.
	 *
	 * @param cache The underlying EhCache instance to use.
	 * @param listener cache listener
	 */
	public EhCache(net.sf.ehcache.Cache cache, CacheExpiredListener listener) {
		this.cache = cache;
		this.cache.getCacheEventNotificationService().registerListener(this);
		this.listener = listener;
	}

	public List<?> keys() throws CacheException {
		return this.cache.getKeys();
	}

	/**
	 * Gets a value of an element which matches the given key.
	 *
	 * @param key the key of the element to return.
	 * @return The value placed into the cache with an earlier put, or null if not found or expired
	 * @throws CacheException cache exception
	 */
	public Object get(Object key) throws CacheException {
		try {
			if ( key == null ) {
				return null;
			} else {
                Element element = cache.get( key );
				if ( element != null ) {
					return element.getObjectValue();				
				}
			}
			return null;
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException( e );
		}
	}

	/**
	 * Puts an object into the cache.
	 *
	 * @param key   a key
	 * @param value a value
	 * @throws CacheException if the {@link CacheManager}
	 *                        is shutdown or another {@link Exception} occurs.
	 */
	public void update(Object key, Object value) throws CacheException {
		put( key, value );
	}

	/**
	 * Puts an object into the cache.
	 *
	 * @param key   a key
	 * @param value a value
	 * @throws CacheException if the {@link CacheManager}
	 *                        is shutdown or another {@link Exception} occurs.
	 */
	public void put(Object key, Object value) throws CacheException {
		try {
			Element element = new Element( key, value );
			cache.put( element );
		} catch (IllegalArgumentException e) {
			throw new CacheException( e );
		} catch (IllegalStateException e) {
			throw new CacheException( e );
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException( e );
		}
	}

	/**
	 * Removes the element which matches the key
	 * If no element matches, nothing is removed and no Exception is thrown.
	 *
	 * @param key the key of the element to remove
	 * @throws CacheException cache exception
	 */
	@Override
	public void evict(Object key) throws CacheException {
		try {
			cache.remove( key );
		} catch (IllegalStateException e) {
			throw new CacheException( e );
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException( e );
		}
	}

	@Override
	public void evict(List<?> keys) throws CacheException {
		cache.removeAll(keys);
	}

	/**
	 * Remove all elements in the cache, but leave the cache
	 * in a useable state.
	 *
	 * @throws CacheException cache exception
	 */
	public void clear() throws CacheException {
		try {
			cache.removeAll();
		} catch (IllegalStateException e) {
			throw new CacheException( e );
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException( e );
		}
	}

	/**
	 * Remove the cache and make it unuseable.
	 *
	 * @throws CacheException  cache exception
	 */
	public void destroy() throws CacheException {
		try {
			cache.getCacheManager().removeCache( cache.getName() );
		} catch (IllegalStateException e) {
			throw new CacheException( e );
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException( e );
		}
	}
	
	public Object clone() throws CloneNotSupportedException { 
		throw new CloneNotSupportedException(); 
	}

	@Override
	public void dispose() {}

	@Override
	public void notifyElementEvicted(Ehcache arg0, Element arg1) {}

	@Override
	public void notifyElementExpired(Ehcache cache, Element elem) {
		if(listener != null){
			listener.notify(cache.getName(), elem.getObjectKey());
		}
	}

	@Override
	public void notifyElementPut(Ehcache arg0, Element arg1) throws net.sf.ehcache.CacheException {}

	@Override
	public void notifyElementRemoved(Ehcache arg0, Element arg1) throws net.sf.ehcache.CacheException {}

	@Override
	public void notifyElementUpdated(Ehcache arg0, Element arg1) throws net.sf.ehcache.CacheException {}

	@Override
	public void notifyRemoveAll(Ehcache arg0) {}

	@Override
	public void put(Object key, Object value, Integer expireInSec) throws CacheException {
		try {
			Element element = new Element( key, value );
			element.setTimeToLive(expireInSec);
			cache.put( element );
		} catch (IllegalArgumentException e) {
			throw new CacheException( e );
		} catch (IllegalStateException e) {
			throw new CacheException( e );
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException( e );
		}
	}

	@Override
	public void update(Object key, Object value, Integer expireInSec) throws CacheException {
		put(key, value, expireInSec);
	}

}