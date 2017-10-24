package io.segment.ehcache;

import io.neural.extension.Extension;
import io.segment.CacheFactory;
import io.segment.exception.CacheException;
import io.segment.support.CacheExpiredListener;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EhCache Provider plugin
 * <br>
 * Taken from EhCache 1.3 distribution
 * 
 * @author lry
 */
@Extension("ehcache")
public class EhCacheFactory implements CacheFactory {

	private final static Logger log = LoggerFactory.getLogger(EhCacheFactory.class);
	public static String KEY_EHCACHE_NAME = "ehcache.name";
	public static String KEY_EHCACHE_CONFIG_XML = "ehcache.configXml";

	private CacheManager manager;
	private ConcurrentHashMap<String, EhCache> _CacheManager ;

	@Override
	public void start(Properties props) throws CacheException {
		if (manager != null) {
            log.warn("Attempt to restart an already started EhCacheProvider.");
            return;
        }
		
		// 如果指定了名称,那么尝试获取已有实例
		String ehcacheName = (String)props.get(KEY_EHCACHE_NAME);
		if (ehcacheName != null && ehcacheName.trim().length() > 0) {
			manager = CacheManager.getCacheManager(ehcacheName);
		}
		if (manager == null) {
			// 指定了配置文件路径? 加载之
			if (props.containsKey(KEY_EHCACHE_CONFIG_XML)) {
				manager = new CacheManager(props.getProperty(KEY_EHCACHE_CONFIG_XML));
			} else {
				// 加载默认实例
				manager = CacheManager.getInstance();
			}
		}
        _CacheManager = new ConcurrentHashMap<String, EhCache>();
	}
	
	@Override
    public EhCache buildCache(String name, boolean autoCreate, CacheExpiredListener listener) throws CacheException {
    	EhCache ehcache = _CacheManager.get(name);
    	if(ehcache == null && autoCreate){
		    try {
	            synchronized(_CacheManager){
	            	ehcache = _CacheManager.get(name);
	            	if(ehcache == null){
			            net.sf.ehcache.Cache cache = manager.getCache(name);
			            if (cache == null) {
			                log.warn("Could not find configuration [" + name + "]; using defaults.");
			                manager.addCache(name);
			                cache = manager.getCache(name);
			                log.debug("started EHCache region: " + name);                
			            }
			            ehcache = new EhCache(cache, listener);
			            _CacheManager.put(name, ehcache);
	            	}
	            }
		    } catch (net.sf.ehcache.CacheException e) {
	            throw new CacheException(e);
	        }
    	}
        return ehcache;
    }

    @Override
	public void stop() {
		if (manager != null) {
            manager.shutdown();
            _CacheManager.clear();
            manager = null;
        }
	}

}
