package io.segment.redis.support;

import java.io.Closeable;
import java.util.*;
import java.util.regex.Pattern;

import io.segment.redis.support.cluster.ClusterRedisService;
import io.segment.redis.support.sharded.ShardedRedisService;
import io.segment.redis.support.single.SingleRedisService;
import redis.clients.jedis.*;

/**
 * The Redis Service Factory.
 *
 * @author lry
 */
public class RedisServiceFactory implements Closeable
{
    private static final Pattern IP_AND_PORT = Pattern.compile("^.+[:]\\d{1,5}\\s*$");

    private RedisPoolConfig redisPoolConfig;
    private JedisPool jedisPool = null;
    private JedisCluster jedisCluster = null;
    private ShardedJedisPool shardedJedisPool = null;

    public RedisServiceFactory(RedisPoolConfig redisPoolConfig) {
        this.redisPoolConfig = redisPoolConfig;
        switch (this.redisPoolConfig.getRedisType()){
            case SINGLE: jedisPool = this.buildSingleRedis(redisPoolConfig); return;
            case CLUSTER: jedisCluster = this.buildClusterRedis(redisPoolConfig); return;
            case SHARDED: shardedJedisPool = this.buildShardedRedis(redisPoolConfig); return;
            default: throw new IllegalArgumentException(redisPoolConfig.getRedisType().toString());
        }
    }

    public RedisPoolConfig getRedisPoolConfig() {
        return redisPoolConfig;
    }

    /**
     * The Get Object Resource.
     *
     * @return
     */
    public Object getResource(){
        switch (redisPoolConfig.getRedisType()){
            case SINGLE: return jedisPool.getResource();
            case CLUSTER: return jedisCluster;
            case SHARDED: return shardedJedisPool.getResource();
            default: throw new IllegalArgumentException(redisPoolConfig.getRedisType().toString());
        }
    }

    /**
     * The Get Redis Service.
     *
     * @return
     */
    public RedisService getRedisService(){
        switch (redisPoolConfig.getRedisType()){
            case SINGLE: return new SingleRedisService(jedisPool.getResource());
            case CLUSTER: return new ClusterRedisService(jedisCluster);
            case SHARDED: return new ShardedRedisService(shardedJedisPool.getResource());
            default: throw new IllegalArgumentException(redisPoolConfig.getRedisType().toString());
        }
    }

    /**
     * The Return Redis Service.
     *
     * @param redisService
     */
    public void returnRedisService(RedisService redisService){
        try{
            redisService.close();
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
            switch (redisPoolConfig.getRedisType()){
                case SINGLE: jedisPool.close(); return;
                case CLUSTER: jedisCluster.close(); return;
                case SHARDED: shardedJedisPool.close(); return;
                default: throw new IllegalArgumentException(redisPoolConfig.getRedisType().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建一个Single Redis Instance.
     *
     * @param redisPoolConfig
     * @return
     */
    protected JedisPool buildSingleRedis(RedisPoolConfig redisPoolConfig){
        return new JedisPool(redisPoolConfig, redisPoolConfig.getHost(), redisPoolConfig.getPort(), redisPoolConfig.getTimeout(),
                "".equals(redisPoolConfig.getPassword()) ? null : redisPoolConfig.getPassword(), redisPoolConfig.getDatabase());
    }

    /**
     * 构建一个Cluster Redis Instance.
     *
     * @param redisPoolConfig
     * @return
     */
    protected JedisCluster buildClusterRedis(RedisPoolConfig redisPoolConfig){
        Set<HostAndPort> hostAndPorts = new HashSet<>();
        String[] hosts = redisPoolConfig.getHost().split(",");
        for (String val : hosts) {
            if (!IP_AND_PORT.matcher(val).matches()) {
                throw new IllegalArgumentException("ip or port is illegal.");
            }
            String[] ipAndPort = val.split(":");
            hostAndPorts.add(new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1])));
        }

        return new JedisCluster(hostAndPorts, redisPoolConfig.getTimeout(), hostAndPorts.size(), redisPoolConfig);
    }

    /**
     * 构建一个Sharded Redis Instance.
     *
     * @param redisPoolConfig
     * @return
     */
    protected ShardedJedisPool buildShardedRedis(RedisPoolConfig redisPoolConfig){
        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>();
        for (String uri : Arrays.asList(redisPoolConfig.getHost().split(","))) {
            JedisShardInfo jedisShardInfo = new JedisShardInfo(uri);
            jedisShardInfo.setConnectionTimeout(redisPoolConfig.getTimeout());
            jedisShardInfoList.add(jedisShardInfo);
        }

        return new ShardedJedisPool(redisPoolConfig, jedisShardInfoList);
    }

    /*
     * 包装常用接口
     * =====================================
     */

    public byte[] hget(byte[] key, byte[] fieldKey) {
        RedisService redisService = null;
        try {
            redisService = this.getRedisService();
            return redisService.hget(key, fieldKey);
        } finally {
            this.returnRedisService(redisService);
        }
    }

    public void hset(byte[] key, byte[] fieldKey, byte[] val) {
        RedisService redisService = null;
        try {
            redisService = this.getRedisService();
            redisService.hset(key, fieldKey, val);
        } finally {
            this.returnRedisService(redisService);
        }
    }

    public void hset(byte[] key, byte[] fieldKey, byte[] val, int expireInSec) {
        RedisService redisService = null;
        try {
            redisService = this.getRedisService();
            redisService.hset(key, fieldKey, val);
            redisService.expire(key, expireInSec);
        } finally {
            this.returnRedisService(redisService);
        }
    }

    public void hdel(byte[] key, byte[]... fieldKey) {
        RedisService redisService = null;
        try {
            redisService = this.getRedisService();
            redisService.hdel(key, fieldKey);
        } finally {
            this.returnRedisService(redisService);
        }
    }

    public Set<String> hkeys(String key) {
        RedisService redisService = null;
        try {
            redisService = this.getRedisService();
            return redisService.hkeys(key);
        } finally {
            this.returnRedisService(redisService);
        }
    }

    public Set<byte[]> hkeys(byte[] key) {
        RedisService redisService = null;
        try {
            redisService = this.getRedisService();
            return redisService.hkeys(key);
        } finally {
            this.returnRedisService(redisService);
        }
    }

    public void del(String key) {
        RedisService redisService = null;
        try {
            redisService = this.getRedisService();
            redisService.del(key);
        } finally {
            this.returnRedisService(redisService);
        }
    }

    public void del(byte[] key) {
        RedisService redisService = null;
        try {
            redisService = this.getRedisService();
            redisService.del(key);
        } finally {
            this.returnRedisService(redisService);
        }
    }

    public void subscribe(BinaryJedisPubSub binaryJedisPubSub, byte[]... channels) {
        RedisService redisService = null;
        try {
            redisService = this.getRedisService();
            redisService.subscribe(binaryJedisPubSub, channels);
        } finally {
            this.returnRedisService(redisService);
        }
    }

    public void publish(byte[] channel, byte[] message) {
        RedisService redisService = null;
        try {
            redisService = this.getRedisService();
            redisService.publish(channel, message);
        } finally {
            this.returnRedisService(redisService);
        }
    }

}
