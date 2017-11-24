package io.segment.cache.session.common.exception;

/**
 * The Session Exception
 * 
 * @author lry
 */
public class SessionException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public SessionException() {}

    public SessionException(String message) {
        super(message);
    }

    public SessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionException(Throwable cause) {
        super(cause);
    }
    
}
