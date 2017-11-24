package cn.ms.session.serializer;

import java.util.Map;

/**
 * The Serializer
 * 
 * @author lry
 */
public interface Serializer {

	/**
	 * serialize object to json string
	 * 
	 * @param object
	 * @return json string
	 */
	String serialize(Object object);

	/**
	 * deserialize json string to map
	 * 
	 * @param json
	 * @return map
	 */
	Map<String, Object> deserialize(String json);

}