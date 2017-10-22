package io.segment.redis.support;

import io.segment.redis.support.RedisClientFactoryAdapter.RedisPolicy;

public class RedisClientTest {

	public static void main(String[] args) {
		RedisPoolConfig poolConfig = new RedisPoolConfig();
		poolConfig.setHost("localhost");
		poolConfig.setPort(6379);
		RedisPolicy policy = RedisPolicy.single;
		RedisClientFactoryAdapter redisClientFactoryAdapter = new RedisClientFactoryAdapter(poolConfig, policy);
		RedisClientFactory<RedisClient> redisClientFactory = redisClientFactoryAdapter.getRedisClientFactory();
		RedisClient redisClient = redisClientFactory.getResource();
		redisClient.set("key1", System.currentTimeMillis()+"");
		System.out.println(redisClient.get("key1"));
	}
	
}
