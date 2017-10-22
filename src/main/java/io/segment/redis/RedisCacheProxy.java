package io.segment.redis;

import io.segment.redis.support.RedisTransporter;
import io.segment.redis.support.RedisTransporterFactoryAdapter;

import java.io.Closeable;
import java.util.Set;

import redis.clients.jedis.BinaryJedisPubSub;

/**
 * Redis cache 代理，用来获取 redis client
 *
 * @author lry
 */
public class RedisCacheProxy implements Closeable {

    private RedisTransporterFactoryAdapter redisTransporterFactoryAdapter;

    public RedisCacheProxy(RedisTransporterFactoryAdapter redisTransporterFactoryAdapter) {
        this.redisTransporterFactoryAdapter = redisTransporterFactoryAdapter;
        if (this.redisTransporterFactoryAdapter == null) {
            throw new RuntimeException("jedis handler adapter must configuration");
        }
    }

    public RedisTransporter getResource() {
        return this.redisTransporterFactoryAdapter.getRedisClientFactory().getResource();
    }

	public void returnResource(RedisTransporter redisTransporter) {
        this.redisTransporterFactoryAdapter.getRedisClientFactory().returnResource(redisTransporter);
    }

    public byte[] hget(byte[] key, byte[] fieldKey) {
        RedisTransporter redisTransporter = null;
        try {
            redisTransporter = getResource();
            return redisTransporter.hget(key, fieldKey);
        } finally {
            returnResource(redisTransporter);
        }
    }

    public void hset(byte[] key, byte[] fieldKey, byte[] val) {
        RedisTransporter redisTransporter = null;
        try {
            redisTransporter = getResource();
            redisTransporter.hset(key, fieldKey, val);
        } finally {
            returnResource(redisTransporter);
        }
    }
    
    public void hset(byte[] key, byte[] fieldKey, byte[] val, int expireInSec) {
        RedisTransporter redisTransporter = null;
        try {
            redisTransporter = getResource();
            redisTransporter.hset(key, fieldKey, val);
            redisTransporter.expire(key, expireInSec);
        } finally {
            returnResource(redisTransporter);
        }
    }

    public void hdel(byte[] key, byte[]... fieldKey) {
        RedisTransporter redisTransporter = null;
        try {
            redisTransporter = getResource();
            redisTransporter.hdel(key, fieldKey);
        } finally {
            returnResource(redisTransporter);
        }
    }

    public Set<String> hkeys(String key) {
        RedisTransporter redisTransporter = null;
        try {
            redisTransporter = getResource();
            return redisTransporter.hkeys(key);
        } finally {
            returnResource(redisTransporter);
        }
    }

    public Set<byte[]> hkeys(byte[] key) {
        RedisTransporter redisTransporter = null;
        try {
            redisTransporter = getResource();
            return redisTransporter.hkeys(key);
        } finally {
            returnResource(redisTransporter);
        }
    }

    public void del(String key) {
        RedisTransporter redisTransporter = null;
        try {
            redisTransporter = getResource();
            redisTransporter.del(key);
        } finally {
            returnResource(redisTransporter);
        }
    }

    public void del(byte[] key) {
        RedisTransporter redisTransporter = null;
        try {
            redisTransporter = getResource();
            redisTransporter.del(key);
        } finally {
            returnResource(redisTransporter);
        }
    }

    public void subscribe(BinaryJedisPubSub binaryJedisPubSub, byte[]... channels) {
        RedisTransporter redisTransporter = null;
        try {
            redisTransporter = getResource();
            redisTransporter.subscribe(binaryJedisPubSub, channels);
        } finally {
            returnResource(redisTransporter);
        }
    }

    public void publish(byte[] channel, byte[] message) {
        RedisTransporter redisTransporter = null;
        try {
            redisTransporter = getResource();
            redisTransporter.publish(channel, message);
        } finally {
            returnResource(redisTransporter);
        }
    }

    public void close() {
        redisTransporterFactoryAdapter.close();
    }
}
