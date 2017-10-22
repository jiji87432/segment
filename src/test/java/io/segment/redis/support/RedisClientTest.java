package io.segment.redis.support;

import io.segment.redis.support.RedisStoreFactoryAdapter.RedisPolicy;

public class RedisClientTest {

	public static void main(String[] args) {
		RedisPoolConfig poolConfig = new RedisPoolConfig();
		poolConfig.setHost("localhost");
		poolConfig.setPort(6379);
		RedisPolicy policy = RedisPolicy.single;
		RedisStoreFactoryAdapter redisStoreFactoryAdapter = new RedisStoreFactoryAdapter(poolConfig, policy);
		RedisStoreFactory<RedisStore> redisClientFactory = redisStoreFactoryAdapter.getRedisClientFactory();
		RedisStore redisStore = redisClientFactory.getResource();
		redisStore.set("key1", System.currentTimeMillis()+"");
		System.out.println(redisStore.get("key1"));
	}
	
}
