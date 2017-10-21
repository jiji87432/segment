package io.segment.support.serializer;

import io.neural.extension.NPI;

import java.io.IOException;

/**
 * 对象序列化接口
 * 
 * @author lry
 */
@NPI
public interface Serializer {
	
	byte[] serialize(Object obj) throws IOException ;
	
	Object deserialize(byte[] bytes) throws IOException ;
	
}
