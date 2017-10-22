package io.segment.redis.support.sharded;

import io.neural.extension.Extension;
import io.segment.redis.support.RedisStoreFactory;
import io.segment.redis.support.RedisPoolConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

@Extension("sharded")
public class ShardedRedisStoreFactory implements RedisStoreFactory<ShardedRedisStore> {

    private static ShardedJedisPool jedisPool;
    private RedisPoolConfig poolConfig;

    private List<JedisShardInfo> jedisShardInfoList;

    public synchronized ShardedJedisPool getJedisPool() {
        return jedisPool;
    }

    @Override
    public ShardedRedisStore getResource() {
        return new ShardedRedisStore(getJedisPool().getResource());
    }

    @Override
    public void returnResource(ShardedRedisStore client) {
        if (client != null)
            client.close();
    }

    public void build() {
        // redis uri 格式
        // redis://password@127.0.0.1:6379/0  多个用逗号分割
        String host = this.poolConfig.getHost();
        int timeout = this.poolConfig.getTimeout();
        if (host != null) {
            List<String> list = Arrays.asList(host.split(","));
            jedisShardInfoList = new ArrayList<>();
            for (String uri : list) {
                JedisShardInfo jedisShardInfo = new JedisShardInfo(uri);
                jedisShardInfo.setConnectionTimeout(timeout);
                jedisShardInfoList.add(jedisShardInfo);
            }
        }

        jedisPool = new ShardedJedisPool(poolConfig, jedisShardInfoList);
    }

    public RedisPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(RedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public List<JedisShardInfo> getJedisShardInfoList() {
        return jedisShardInfoList;
    }

    public void setJedisShardInfoList(List<JedisShardInfo> jedisShardInfoList) {
        this.jedisShardInfoList = jedisShardInfoList;
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
