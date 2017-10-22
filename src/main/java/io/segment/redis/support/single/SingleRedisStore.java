package io.segment.redis.support.single;

import io.neural.extension.Extension;
import io.segment.redis.support.RedisStore;
import io.segment.redis.support.RedisPoolConfig;

import java.io.IOException;

import redis.clients.jedis.JedisPool;

/**
 * redis 数据连接池工厂
 * 
 * @author lry
 */
@Extension("single")
public class SingleRedisStore implements RedisStore<SingleRedisService> {

    private static JedisPool jedisPool;
    private RedisPoolConfig poolConfig;

    public synchronized JedisPool getJedisPool() {
        return jedisPool;
    }

    @Override
    public SingleRedisService getResource() {
        return new SingleRedisService(getJedisPool().getResource());
    }

    @Override
    public void returnResource(SingleRedisService client) {
        if (client != null)
            client.close();
    }

    public void build() {
        String host = this.poolConfig.getHost();
        int port = this.poolConfig.getPort();
        int timeout = this.poolConfig.getTimeout();
        int database = this.poolConfig.getDatabase();
        String password = this.poolConfig.getPassword();
        if (password != null && !"".equals(password))
            jedisPool = new JedisPool(poolConfig, host, port, timeout, password, database);
        else {
        	jedisPool = new JedisPool(poolConfig, host, port, timeout, null, database);
        }
    }

    public RedisPoolConfig getPoolConfig() {
        return this.poolConfig;
    }

    public void setPoolConfig(RedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        jedisPool.close();
    }
}
