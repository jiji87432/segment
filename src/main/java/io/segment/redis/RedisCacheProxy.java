package io.segment.redis;

import io.segment.redis.support.RedisStore;
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

    public RedisStore getResource() {
        return this.redisStoreFactoryAdapter.getRedisClientFactory().getResource();
    }

	public void returnResource(RedisStore redisStore) {
        this.redisStoreFactoryAdapter.getRedisClientFactory().returnResource(redisStore);
    }

    public byte[] hget(byte[] key, byte[] fieldKey) {
        RedisStore redisStore = null;
        try {
            redisStore = getResource();
            return redisStore.hget(key, fieldKey);
        } finally {
            returnResource(redisStore);
        }
    }

    public void hset(byte[] key, byte[] fieldKey, byte[] val) {
        RedisStore redisStore = null;
        try {
            redisStore = getResource();
            redisStore.hset(key, fieldKey, val);
        } finally {
            returnResource(redisStore);
        }
    }
    
    public void hset(byte[] key, byte[] fieldKey, byte[] val, int expireInSec) {
        RedisStore redisStore = null;
        try {
            redisStore = getResource();
            redisStore.hset(key, fieldKey, val);
            redisStore.expire(key, expireInSec);
        } finally {
            returnResource(redisStore);
        }
    }

    public void hdel(byte[] key, byte[]... fieldKey) {
        RedisStore redisStore = null;
        try {
            redisStore = getResource();
            redisStore.hdel(key, fieldKey);
        } finally {
            returnResource(redisStore);
        }
    }

    public Set<String> hkeys(String key) {
        RedisStore redisStore = null;
        try {
            redisStore = getResource();
            return redisStore.hkeys(key);
        } finally {
            returnResource(redisStore);
        }
    }

    public Set<byte[]> hkeys(byte[] key) {
        RedisStore redisStore = null;
        try {
            redisStore = getResource();
            return redisStore.hkeys(key);
        } finally {
            returnResource(redisStore);
        }
    }

    public void del(String key) {
        RedisStore redisStore = null;
        try {
            redisStore = getResource();
            redisStore.del(key);
        } finally {
            returnResource(redisStore);
        }
    }

    public void del(byte[] key) {
        RedisStore redisStore = null;
        try {
            redisStore = getResource();
            redisStore.del(key);
        } finally {
            returnResource(redisStore);
        }
    }

    public void subscribe(BinaryJedisPubSub binaryJedisPubSub, byte[]... channels) {
        RedisStore redisStore = null;
        try {
            redisStore = getResource();
            redisStore.subscribe(binaryJedisPubSub, channels);
        } finally {
            returnResource(redisStore);
        }
    }

    public void publish(byte[] channel, byte[] message) {
        RedisStore redisStore = null;
        try {
            redisStore = getResource();
            redisStore.publish(channel, message);
        } finally {
            returnResource(redisStore);
        }
    }

    public void close() {
        redisStoreFactoryAdapter.close();
    }
}
