package cn.ms.session.redis;

import java.io.IOException;

import cn.ms.session.DSession;
import cn.ms.session.core.support.DSessionFilter;

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
