package io.segment;

import io.segment.ehcache.JGroupsSegmentChannel;
import io.segment.exception.CacheException;
import io.segment.redis.RedisSegmentChannel;
import io.segment.support.CacheManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Distributed Cache.
 * 
 * @author lry
 */
public class Segment {

	private final static Logger log = LoggerFactory.getLogger(Segment.class);

	private final static String CONFIG_FILE = "/segment.properties";
	private final static SegmentChannel channel;
	private final static Properties config;

	static {
		try {
			config = loadConfig();
			String cache_broadcast = config.getProperty("cache.broadcast");
			if ("redis".equalsIgnoreCase(cache_broadcast)) {
				channel = RedisSegmentChannel.getInstance();
			} else if ("jgroups".equalsIgnoreCase(cache_broadcast)) {
				channel = JGroupsSegmentChannel.getInstance();
			} else {
				throw new CacheException("Cache Channel not defined. name = " + cache_broadcast);
			}
		} catch (IOException e) {
			throw new CacheException("Unabled to load j2cache configuration " + CONFIG_FILE, e);
		}
	}

	public static SegmentChannel getChannel(){
		return channel;
	}

	public static Properties getConfig(){
		return config;
	}

	private static Properties loadConfig() throws IOException {
		log.info("Load J2Cache Config File : [{}].", CONFIG_FILE);
		InputStream configStream = Segment.class.getClassLoader().getParent().getResourceAsStream(CONFIG_FILE);
		if(configStream == null) {
			configStream = CacheManager.class.getResourceAsStream(CONFIG_FILE);
		}
		if(configStream == null) {
			throw new CacheException("Cannot find " + CONFIG_FILE + " !!!");
		}

		Properties props = new Properties();
		try{
			props.load(configStream);
		}finally{
			configStream.close();
		}

		return props;
	}

}
