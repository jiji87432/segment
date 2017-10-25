package io.segment.redis.client;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Protocol;

public class RedisConfig extends GenericObjectPoolConfig {

    private String host;
    private int port;
    private String password;
    private int database;
	private int timeout = Protocol.DEFAULT_TIMEOUT;
	private RedisType redisType;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
    public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

    public RedisType getRedisType() {
        return redisType;
    }

    public void setRedisType(RedisType redisType) {
        this.redisType = redisType;
    }

    public enum RedisType {
        CLUSTER,SHARDED,SINGLE
    }

}
