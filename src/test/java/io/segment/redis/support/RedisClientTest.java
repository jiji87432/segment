package io.segment.redis.support;

import io.segment.redis.RedisClientFactory;
import io.segment.redis.client.RedisConfig;
import io.segment.redis.client.RedisClient;

public class RedisClientTest {

	public static void main(String[] args) {
		RedisConfig poolConfig = new RedisConfig();
		poolConfig.setHost("localhost");
		poolConfig.setPort(6379);
		poolConfig.setRedisType(RedisConfig.RedisType.SINGLE);
		RedisClientFactory redisClientFactory = new RedisClientFactory(poolConfig);
		RedisClient redisClient = redisClientFactory.getRedisClient();
		redisClient.set("key1", System.currentTimeMillis()+"");
		System.out.println(redisClient.get("key1"));
	}
	
}
