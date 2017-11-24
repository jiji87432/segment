package io.segment.session;

import java.util.Map;

import io.segment.session.core.IdWorker;

/**
 * The Session Manager
 * 
 * @author lry
 */
public interface DSession {

	/**
	 * persist session to session store
	 * 
	 * @param id session id
	 * @param snapshot session attributes' snapshot
	 * @param maxInactiveInterval session max life(seconds)
	 * @return true if save successfully, or false
	 */
	Boolean persist(String id, Map<String, Object> snapshot, int maxInactiveInterval);

	/**
	 * load session by id
	 * 
	 * @param id session id
	 * @return the session map object
	 */
	Map<String, Object> loadById(String id);

	/**
	 * delete session physically
	 * 
	 * @param id session id
	 */
	void deleteById(String id);

	/**
	 * set session expired time
	 * 
	 * @param sid current session id
	 * @param maxInactiveInterval max life(seconds)
	 */
	void expire(String sid, int maxInactiveInterval);

	/**
	 * session manager destroy when filter destroy
	 */
	void destroy();

	/**
	 * get session id generator
	 * 
	 * @return session id generator
	 */
	IdWorker getSessionId();

}
