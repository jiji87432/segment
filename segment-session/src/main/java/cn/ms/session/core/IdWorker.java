package cn.ms.session.core;


/**
 * Session id generator
 * 
 * @author lry
 */
public interface IdWorker {

	/**
	 * Get Session Id
	 * 
	 * @param clientId
	 * @return session id
	 */
	String generate(String clientId);

}
