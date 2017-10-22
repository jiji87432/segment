package io.segment.redis.support;

import io.segment.redis.support.RedisStoreFactory.RedisPolicy;

public class RedisClientTest {

	public static void main(String[] args) {
		RedisPoolConfig poolConfig = new RedisPoolConfig();
		poolConfig.setHost("localhost");
		poolConfig.setPort(6379);
		RedisPolicy policy = RedisPolicy.single;
		RedisStoreFactory redisStoreFactory = new RedisStoreFactory(poolConfig, policy);
		RedisStore<RedisService> redisClientFactory = redisStoreFactory.getRedisClientFactory();
		RedisService redisService = redisClientFactory.getResource();
		redisService.set("key1", System.currentTimeMillis()+"");
		System.out.println(redisService.get("key1"));
	}
	
}
