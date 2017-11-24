package io.segment.session.serializer;

import java.util.Map;

import io.segment.session.common.exception.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;

/**
 * Json Serializer
 * 
 * @author lry
 */
public final class FastjsonSerializer implements Serializer {

	private static final Logger logger = LoggerFactory.getLogger(FastjsonSerializer.class);

	@Override
	public String serialize(Object o) {
		try {
			return JSON.toJSONString(o);
		} catch (Exception e) {
			logger.error("failed to serialize http session {} to json,cause:{}", o, Throwables.getStackTraceAsString(e));
			throw new SerializeException("failed to serialize http session to json", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> deserialize(String o) {
		try {
			return JSON.parseObject(o, Map.class);
		} catch (Exception e) {
			logger.error("failed to deserialize string  {} to http session,cause:{} ", o, Throwables.getStackTraceAsString(e));
			throw new SerializeException("failed to deserialize string to http session", e);
		}
	}

}
