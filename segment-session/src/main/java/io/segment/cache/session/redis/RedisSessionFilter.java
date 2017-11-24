package io.segment.cache.session.redis;

import java.io.IOException;

import io.segment.cache.session.DSession;
import io.segment.cache.session.core.support.DSessionFilter;

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
