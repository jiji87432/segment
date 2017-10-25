package io.segment.redis.support;

import io.segment.redis.client.RedisConfig;
import io.segment.redis.client.RedisClient;
import io.segment.redis.support.RedisStoreFactory.RedisPolicy;

public class RedisClientTest {

	public static void main(String[] args) {
		RedisConfig poolConfig = new RedisConfig();
		poolConfig.setHost("localhost");
		poolConfig.setPort(6379);
		RedisPolicy policy = RedisPolicy.single;
		RedisStoreFactory redisStoreFactory = new RedisStoreFactory(poolConfig, policy);
		RedisStore<RedisClient> redisClientFactory = redisStoreFactory.getRedisClientFactory();
		RedisClient redisClient = redisClientFactory.getResource();
		redisClient.set("key1", System.currentTimeMillis()+"");
		System.out.println(redisClient.get("key1"));
	}
	
}
