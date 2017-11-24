package io.segment.session.redis;

import java.io.IOException;

import io.segment.session.DSession;
import io.segment.session.core.support.DSessionFilter;

/**
 * Redis Session Filter
 * 
 * @author lry
 */
public class RedisSessionFilter extends DSessionFilter {

    /**
     * subclass create session manager
     * 
     * @return session manager
     */
    @Override
    protected DSession createSession() throws IOException{
        return new RedisSession();
    }
    
}
