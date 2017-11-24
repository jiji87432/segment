package io.segment.session.redis;

import redis.clients.jedis.Jedis;

/**
 * Jedis Execute Callback
 * 
 * @author lry
 *
 * @param <V> Return Type
 */
public interface RedisCallback<V> {

    /**
     * execute jedis operation
     * 
     * @param jedis jedis instance
     * @return result
     */
    V execute(Jedis jedis);
    
}
