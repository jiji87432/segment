package io.segment.cache.session.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;

/**
 * Properties Utils
 * 
 * @author lry
 */
public class PropertiesUtils {

    /**
     * read properties from classpath
     * 
     * @param classpath properties file classpath
     * @return Properties object
     * @throws java.io.IOException
     */
    public static Properties read(String classpath) throws IOException{
        URL url = Resources.getResource(classpath);
        ByteSource byteSource = Resources.asByteSource(url);
        Properties properties = new Properties();
        InputStream inputStream = null;
        
        try {
            inputStream = byteSource.openBufferedStream();
            properties.load(inputStream);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioException) {
                    throw ioException;
                }
            }
        }
        
        return properties;
    }
    
}