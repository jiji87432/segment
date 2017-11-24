package io.segment.cache.session.core.support;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.segment.cache.session.DSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.segment.cache.session.common.WebUtils;

import com.google.common.base.Strings;

/**
 * Session Filter
 * 
 * @author lry
 */
public abstract class DSessionFilter implements Filter{

	private static final Logger logger = LoggerFactory.getLogger(DSessionFilter.class);

    protected final static String SESSION_COOKIE_NAME = "sessionCookieName";

    protected final static String DEFAULT_SESSION_COOKIE_NAME = "sfid";

    /**
     * session cookie name
     */
    protected String sessionCookieName;

    protected final static String MAX_INACTIVE_INTERVAL = "maxInactiveInterval";

    /**
     * default 30 mins
     */
    protected final static int DEFAULT_MAX_INACTIVE_INTERVAL = 30 * 60;

    /**
     * max inactive interval
     */
    protected int maxInactiveInterval;

    /**
     * cookie domain
     */
    protected final static String COOKIE_DOMAIN = "cookieDomain";

    /**
     * cookie name
     */
    protected String cookieDomain;

    /**
     * cookie context path
     */
    protected final static String COOKIE_CONTEXT_PATH = "cookieContextPath";

    /**
     * default cookie context path
     */
    protected final static String DEFAULT_COOKIE_CONTEXT_PATH = "/";

    /**
     * cookie's context path
     */
    protected String cookieContextPath;

    /**
     * cookie max age
     */
    protected final static String COOKIE_MAX_AGE = "cookieMaxAge";

    /**
     * default cookie max age
     */
    protected final static int DEFAULT_COOKIE_MAX_AGE = -1;

    /**
     * cookie's life
     */
    protected int cookieMaxAge;

    /**
     * session manager
     */
    protected DSession isession;

    public void init(FilterConfig filterConfig) throws ServletException {
        try {
        	isession = createSession();
            initAttrs(filterConfig);
        } catch (Exception ex) {
        	logger.error("failed to init session filter.", ex);
            throw new ServletException(ex);
        }
    }

    /**
     * subclass create session manager
     * 
     * @return session manager
     */
    protected abstract DSession createSession() throws IOException;

    /**
     * init basic attribute
     * 
     * @param config the filter config
     */
    private void initAttrs(FilterConfig config){

        String param  = config.getInitParameter(SESSION_COOKIE_NAME);
        sessionCookieName = Strings.isNullOrEmpty(param) ? DEFAULT_SESSION_COOKIE_NAME : param;

        param = config.getInitParameter(MAX_INACTIVE_INTERVAL);
        maxInactiveInterval = Strings.isNullOrEmpty(param) ? DEFAULT_MAX_INACTIVE_INTERVAL : Integer.parseInt(param);

        cookieDomain = config.getInitParameter(COOKIE_DOMAIN);

        param = config.getInitParameter(COOKIE_CONTEXT_PATH);
        cookieContextPath = Strings.isNullOrEmpty(param) ? DEFAULT_COOKIE_CONTEXT_PATH : param;

        param = config.getInitParameter(COOKIE_MAX_AGE);
        cookieMaxAge = Strings.isNullOrEmpty(param) ? DEFAULT_COOKIE_MAX_AGE : Integer.parseInt(param);

        logger.info("SessionFilter (sessionCookieName={},maxInactiveInterval={},cookieDomain={})", sessionCookieName, maxInactiveInterval, cookieDomain);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof DHttpServletRequest) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        DHttpServletRequest request2 = new DHttpServletRequest(httpRequest, httpResponse, isession);
        request2.setSessionCookieName(sessionCookieName);
        request2.setMaxInactiveInterval(maxInactiveInterval);
        request2.setCookieDomain(cookieDomain);
        request2.setCookieContextPath(cookieContextPath);
        request2.setCookieMaxAge(cookieMaxAge);

        // do other filter
        chain.doFilter(request2, response);

        // update session when request is handled
        DHttpSession session = request2.currentSession();
        if (session != null) {
            if(!session.isValid()){
                // if invalidate , delete session
            	logger.debug("session is invalid, will delete.");
                WebUtils.failureCookie(httpRequest, httpResponse, sessionCookieName, cookieDomain, cookieContextPath);
            } else if (session.isDirty()) {
                // should flush to store
            	logger.debug("try to flush session to session store");
                Map<String, Object> snapshot = session.snapshot();
                if (isession.persist(session.getId(), snapshot, maxInactiveInterval)) {
                	logger.debug("succeed to flush session {} to store, key is:{}", snapshot, session.getId());
                } else {
                	logger.error("failed to persist session to redis");
                    WebUtils.failureCookie(httpRequest, httpResponse, sessionCookieName, cookieDomain, cookieContextPath);
                }
            } else {
                // refresh expire time
            	isession.expire(session.getId(), maxInactiveInterval);
            }
        }
    }

    public void destroy() {
    	isession.destroy();
    	logger.debug("filter is destroy.");
    }
    
}
