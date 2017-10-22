package io.segment.redis.support;

import io.segment.redis.support.RedisTransporterFactoryAdapter.RedisPolicy;

public class RedisClientTest {

	public static void main(String[] args) {
		RedisPoolConfig poolConfig = new RedisPoolConfig();
		poolConfig.setHost("localhost");
		poolConfig.setPort(6379);
		RedisPolicy policy = RedisPolicy.single;
		RedisTransporterFactoryAdapter redisTransporterFactoryAdapter = new RedisTransporterFactoryAdapter(poolConfig, policy);
		RedisTransporterFactory<RedisTransporter> redisClientFactory = redisTransporterFactoryAdapter.getRedisClientFactory();
		RedisTransporter redisTransporter = redisClientFactory.getResource();
		redisTransporter.set("key1", System.currentTimeMillis()+"");
		System.out.println(redisTransporter.get("key1"));
	}
	
}
