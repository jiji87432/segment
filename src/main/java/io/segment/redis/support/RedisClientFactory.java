package io.segment.redis.support;

import io.segment.redis.support.client.ClusterRedisClient;
import io.segment.redis.support.client.ShardedRedisClient;
import io.segment.redis.support.client.SingleRedisClient;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

/**
 * The Redis Service Factory.
 *
 * @author lry
 */
public class RedisClientFactory implements Closeable
{
    private static final Pattern IP_AND_PORT = Pattern.compile("^.+[:]\\d{1,5}\\s*$");

    private RedisConfig redisConfig;
    private JedisPool jedisPool = null;
    private JedisCluster jedisCluster = null;
    private ShardedJedisPool shardedJedisPool = null;

    public RedisClientFactory(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
        switch (this.redisConfig.getRedisType()){
            case SINGLE: jedisPool = this.buildSingleRedis(redisConfig); return;
            case CLUSTER: jedisCluster = this.buildClusterRedis(redisConfig); return;
            case SHARDED: shardedJedisPool = this.buildShardedRedis(redisConfig); return;
            default: throw new IllegalArgumentException(redisConfig.getRedisType().toString());
        }
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    /**
     * The Get Object Resource.
     *
     * @return
     */
    public Object getResource(){
        switch (redisConfig.getRedisType()){
            case SINGLE: return jedisPool;
            case CLUSTER: return jedisCluster;
            case SHARDED: return shardedJedisPool;
            default: throw new IllegalArgumentException(redisConfig.getRedisType().toString());
        }
    }

    /**
     * The Get Redis Client.
     *
     * @return
     */
    public RedisClient getRedisClient(){
        switch (redisConfig.getRedisType()){
            case SINGLE: return new SingleRedisClient(jedisPool.getResource());
            case CLUSTER: return new ClusterRedisClient(jedisCluster);
            case SHARDED: return new ShardedRedisClient(shardedJedisPool.getResource());
            default: throw new IllegalArgumentException(redisConfig.getRedisType().toString());
        }
    }

    /**
     * The Return Redis Client.
     *
     * @param redisClient
     */
    public void returnRedisClient(RedisClient redisClient){
        try{
            redisClient.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * The Close Redis Pool.
     */
    @Override
    public void close(){
        try {
            switch (redisConfig.getRedisType()){
                case SINGLE: jedisPool.close(); return;
                case CLUSTER: jedisCluster.close(); return;
                case SHARDED: shardedJedisPool.close(); return;
                default: throw new IllegalArgumentException(redisConfig.getRedisType().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建一个Single Redis Instance.
     *
     * @param redisConfig
     * @return
     */
    protected JedisPool buildSingleRedis(RedisConfig redisConfig){
        return new JedisPool(redisConfig, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout(),
                "".equals(redisConfig.getPassword()) ? null : redisConfig.getPassword(), redisConfig.getDatabase());
    }

    /**
     * 构建一个Cluster Redis Instance.
     *
     * @param redisConfig
     * @return
     */
    protected JedisCluster buildClusterRedis(RedisConfig redisConfig){
        Set<HostAndPort> hostAndPorts = new HashSet<>();
        String[] hosts = redisConfig.getHost().split(",");
        for (String val : hosts) {
            if (!IP_AND_PORT.matcher(val).matches()) {
                throw new IllegalArgumentException("ip or port is illegal.");
            }
            String[] ipAndPort = val.split(":");
            hostAndPorts.add(new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1])));
        }

        return new JedisCluster(hostAndPorts, redisConfig.getTimeout(), hostAndPorts.size(), redisConfig);
    }

    /**
     * 构建一个Sharded Redis Instance.
     *
     * @param redisConfig
     * @return
     */
    protected ShardedJedisPool buildShardedRedis(RedisConfig redisConfig){
        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>();
        for (String uri : Arrays.asList(redisConfig.getHost().split(","))) {
            JedisShardInfo jedisShardInfo = new JedisShardInfo(uri);
            jedisShardInfo.setConnectionTimeout(redisConfig.getTimeout());
            jedisShardInfoList.add(jedisShardInfo);
        }

        return new ShardedJedisPool(redisConfig, jedisShardInfoList);
    }

    /*
     * 包装常用接口
     * =====================================
     */

    public byte[] hget(byte[] key, byte[] fieldKey) {
        RedisClient redisClient = null;
        try {
            redisClient = this.getRedisClient();
            return redisClient.hget(key, fieldKey);
        } finally {
            this.returnRedisClient(redisClient);
        }
    }

    public void hset(byte[] key, byte[] fieldKey, byte[] val) {
        RedisClient redisClient = null;
        try {
            redisClient = this.getRedisClient();
            redisClient.hset(key, fieldKey, val);
        } finally {
            this.returnRedisClient(redisClient);
        }
    }

    public void hset(byte[] key, byte[] fieldKey, byte[] val, int expireInSec) {
        RedisClient redisClient = null;
        try {
            redisClient = this.getRedisClient();
            redisClient.hset(key, fieldKey, val);
            redisClient.expire(key, expireInSec);
        } finally {
            this.returnRedisClient(redisClient);
        }
    }

    public void hdel(byte[] key, byte[]... fieldKey) {
        RedisClient redisClient = null;
        try {
            redisClient = this.getRedisClient();
            redisClient.hdel(key, fieldKey);
        } finally {
            this.returnRedisClient(redisClient);
        }
    }

    public Set<String> hkeys(String key) {
        RedisClient redisClient = null;
        try {
            redisClient = this.getRedisClient();
            return redisClient.hkeys(key);
        } finally {
            this.returnRedisClient(redisClient);
        }
    }

    public Set<byte[]> hkeys(byte[] key) {
        RedisClient redisClient = null;
        try {
            redisClient = this.getRedisClient();
            return redisClient.hkeys(key);
        } finally {
            this.returnRedisClient(redisClient);
        }
    }

    public void del(String key) {
        RedisClient redisClient = null;
        try {
            redisClient = this.getRedisClient();
            redisClient.del(key);
        } finally {
            this.returnRedisClient(redisClient);
        }
    }

    public void del(byte[] key) {
        RedisClient redisClient = null;
        try {
            redisClient = this.getRedisClient();
            redisClient.del(key);
        } finally {
            this.returnRedisClient(redisClient);
        }
    }

    public void subscribe(BinaryJedisPubSub binaryJedisPubSub, byte[]... channels) {
        RedisClient redisClient = null;
        try {
            redisClient = this.getRedisClient();
            redisClient.subscribe(binaryJedisPubSub, channels);
        } finally {
            this.returnRedisClient(redisClient);
        }
    }

    public void publish(byte[] channel, byte[] message) {
        RedisClient redisClient = null;
        try {
            redisClient = this.getRedisClient();
            redisClient.publish(channel, message);
        } finally {
            this.returnRedisClient(redisClient);
        }
    }

}
