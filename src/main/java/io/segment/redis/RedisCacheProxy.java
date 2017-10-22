package io.segment.redis;

import io.segment.redis.support.RedisService;
import io.segment.redis.support.RedisStoreFactoryAdapter;

import java.io.Closeable;
import java.util.Set;

import redis.clients.jedis.BinaryJedisPubSub;

/**
 * Redis cache 代理，用来获取 redis client
 *
 * @author lry
 */
public class RedisCacheProxy implements Closeable {

    private RedisStoreFactoryAdapter redisStoreFactoryAdapter;

    public RedisCacheProxy(RedisStoreFactoryAdapter redisStoreFactoryAdapter) {
        this.redisStoreFactoryAdapter = redisStoreFactoryAdapter;
        if (this.redisStoreFactoryAdapter == null) {
            throw new RuntimeException("jedis handler adapter must configuration");
        }
    }

    public RedisService getResource() {
        return this.redisStoreFactoryAdapter.getRedisClientFactory().getResource();
    }

	public void returnResource(RedisService redisService) {
        this.redisStoreFactoryAdapter.getRedisClientFactory().returnResource(redisService);
    }

    public byte[] hget(byte[] key, byte[] fieldKey) {
        RedisService redisService = null;
        try {
            redisService = getResource();
            return redisService.hget(key, fieldKey);
        } finally {
            returnResource(redisService);
        }
    }

    public void hset(byte[] key, byte[] fieldKey, byte[] val) {
        RedisService redisService = null;
        try {
            redisService = getResource();
            redisService.hset(key, fieldKey, val);
        } finally {
            returnResource(redisService);
        }
    }
    
    public void hset(byte[] key, byte[] fieldKey, byte[] val, int expireInSec) {
        RedisService redisService = null;
        try {
            redisService = getResource();
            redisService.hset(key, fieldKey, val);
            redisService.expire(key, expireInSec);
        } finally {
            returnResource(redisService);
        }
    }

    public void hdel(byte[] key, byte[]... fieldKey) {
        RedisService redisService = null;
        try {
            redisService = getResource();
            redisService.hdel(key, fieldKey);
        } finally {
            returnResource(redisService);
        }
    }

    public Set<String> hkeys(String key) {
        RedisService redisService = null;
        try {
            redisService = getResource();
            return redisService.hkeys(key);
        } finally {
            returnResource(redisService);
        }
    }

    public Set<byte[]> hkeys(byte[] key) {
        RedisService redisService = null;
        try {
            redisService = getResource();
            return redisService.hkeys(key);
        } finally {
            returnResource(redisService);
        }
    }

    public void del(String key) {
        RedisService redisService = null;
        try {
            redisService = getResource();
            redisService.del(key);
        } finally {
            returnResource(redisService);
        }
    }

    public void del(byte[] key) {
        RedisService redisService = null;
        try {
            redisService = getResource();
            redisService.del(key);
        } finally {
            returnResource(redisService);
        }
    }

    public void subscribe(BinaryJedisPubSub binaryJedisPubSub, byte[]... channels) {
        RedisService redisService = null;
        try {
            redisService = getResource();
            redisService.subscribe(binaryJedisPubSub, channels);
        } finally {
            returnResource(redisService);
        }
    }

    public void publish(byte[] channel, byte[] message) {
        RedisService redisService = null;
        try {
            redisService = getResource();
            redisService.publish(channel, message);
        } finally {
            returnResource(redisService);
        }
    }

    public void close() {
        redisStoreFactoryAdapter.close();
    }
}
