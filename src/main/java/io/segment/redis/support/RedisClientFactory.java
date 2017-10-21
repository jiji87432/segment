package io.segment.redis.support;

import io.neural.extension.NPI;

import java.io.Closeable;

@NPI
public interface RedisClientFactory<C extends RedisClient> extends Closeable {

	void build();

	/**
	 * 在大并发情况下，实现类的getResource方法，务必加上synchronized关键子，保证resource的一致性
	 * 
	 * @return
	 */
	C getResource();

	void returnResource(C client);

}
