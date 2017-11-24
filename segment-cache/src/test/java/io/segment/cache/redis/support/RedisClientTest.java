package io.segment.cache.redis.support;


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
		redisClientFactory.close();
	}
	
}
