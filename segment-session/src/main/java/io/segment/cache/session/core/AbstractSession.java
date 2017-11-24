package io.segment.cache.session.core;

import java.io.IOException;
import java.util.Properties;

import io.segment.cache.session.DSession;
import io.segment.cache.session.core.worker.UuidIdWorker;
import io.segment.cache.session.serializer.FastjsonSerializer;
import io.segment.cache.session.serializer.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.segment.cache.session.common.PropertiesUtils;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;

/**
 * Abstract Session
 * 
 * @author lry
 */
public abstract class AbstractSession implements DSession {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSession.class);

    private static final String DEFAULT_PROPERTIES = "session.properties";

    protected IdWorker idWorker;

    protected Serializer serializer;

    public AbstractSession() throws IOException {
        this(DEFAULT_PROPERTIES);
    }

    /**
     * @param propertiesFile properties file in classpath, default is session.properties
     */
    public AbstractSession(String propertiesFile) throws IOException {
        Properties props = PropertiesUtils.read(propertiesFile);
        initSessionIdGenerator(props);
        initSerializer(props);
        init(props);
    }

    /**
     * init subclass
     */
    protected void init(Properties props){}

    protected void initSerializer(Properties props) {
        String sessionSerializer = (String)props.get("session.serializer");
        if (Strings.isNullOrEmpty(sessionSerializer)){
            serializer = new FastjsonSerializer();
        } else {
            try {
                serializer = (Serializer)(Class.forName(sessionSerializer).newInstance());
            } catch (Exception e) {
            	logger.error("failed to init json generator: {}", Throwables.getStackTraceAsString(e));
            } finally {
                if (idWorker == null){
                	logger.info("use default json serializer [JsonSerializer]");
                    serializer = new FastjsonSerializer();
                }
            }
        }
    }

    protected void initSessionIdGenerator(Properties props) {
        String sessionIdGeneratorClazz = (String)props.get("session.id.generator");
        if (Strings.isNullOrEmpty(sessionIdGeneratorClazz)){
            idWorker = new UuidIdWorker();
        } else {
            try {
                idWorker = (IdWorker)(Class.forName(sessionIdGeneratorClazz).newInstance());
            } catch (Exception e) {
            	logger.error("failed to init session id generator: {}", Throwables.getStackTraceAsString(e));
            } finally {
                if (idWorker == null){
                	logger.info("use default session id generator[DefaultSessionIdGenerator]");
                    idWorker = new UuidIdWorker();
                }
            }
        }
    }
    
}
