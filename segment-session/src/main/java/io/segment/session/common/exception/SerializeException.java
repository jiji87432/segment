package io.segment.session.common.exception;

/**
 * The Serialize Exception
 * 
 * @author lry
 */
public class SerializeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SerializeException() {}

    public SerializeException(String s) {
        super(s);
    }

    public SerializeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SerializeException(Throwable throwable) {
        super(throwable);
    }
    
}
