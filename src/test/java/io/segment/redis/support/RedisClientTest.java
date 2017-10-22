package io.segment.redis.support;

import io.segment.redis.support.RedisStoreFactoryAdapter.RedisPolicy;

public class RedisClientTest {

	public static void main(String[] args) {
		RedisPoolConfig poolConfig = new RedisPoolConfig();
		poolConfig.setHost("localhost");
		poolConfig.setPort(6379);
		RedisPolicy policy = RedisPolicy.single;
		RedisStoreFactoryAdapter redisStoreFactoryAdapter = new RedisStoreFactoryAdapter(poolConfig, policy);
		RedisStoreFactory<RedisService> redisClientFactory = redisStoreFactoryAdapter.getRedisClientFactory();
		RedisService redisService = redisClientFactory.getResource();
		redisService.set("key1", System.currentTimeMillis()+"");
		System.out.println(redisService.get("key1"));
	}
	
}
