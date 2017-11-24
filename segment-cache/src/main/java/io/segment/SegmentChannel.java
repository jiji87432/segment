package io.segment;

import io.neural.extension.NPI;
import io.segment.exception.CacheException;
import io.segment.support.CacheObject;

import java.util.List;

@NPI
public interface SegmentChannel {

	public final static byte LEVEL_1 = 1;
	public final static byte LEVEL_2 = 2;

	CacheObject get(String region, Object key);

	void set(String region, Object key, Object value);

	void set(String region, Object key, Object value, Integer expireInSec);

	public void evict(String region, Object key);

	void batchEvict(String region, List<?> keys);

	void clear(String region) throws CacheException;

	List<?> keys(String region) throws CacheException;

	void close();

}
