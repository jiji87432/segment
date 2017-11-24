package io.segment.cache.serializer;

import io.neural.extension.ExtensionLoader;
import io.segment.cache.support.CacheManager;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对象序列化工具包
 *
 * @author lry
 */
public class SerializationUtils {

    private final static Logger log = LoggerFactory.getLogger(SerializationUtils.class);
    private static Serializer g_ser;

    static {
        String ser = CacheManager.getSerializer();
        if (ser == null || "".equals(ser.trim())) {
            g_ser = new JavaSerializer();
    	} else {
        	g_ser = ExtensionLoader.getLoader(Serializer.class).getExtension(ser);
        }
        log.info("Using Serializer -> [" + ser + ":" + g_ser.getClass().getName() + ']');
    }

    public static byte[] serialize(Object obj) throws IOException {
        return g_ser.serialize(obj);
    }

    public static Object deserialize(byte[] bytes) throws IOException {
        return g_ser.deserialize(bytes);
    }

}
