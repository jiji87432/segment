package io.segment.session.core.worker;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import io.segment.session.core.IdWorker;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

/**
 * Default Session ID Generator
 * 
 * @author lry
 */
public class UuidIdWorker implements IdWorker {

    public static final Character SEPARATOR ='Z';

    private final String hostIpMd5;

    public UuidIdWorker() {
        String hostIp;
        try {
            hostIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostIp = UUID.randomUUID().toString();
        }
        
        hostIpMd5 = Hashing.md5().hashString(hostIp, Charsets.UTF_8).toString().substring(0,8);
    }

    @Override
    public String generate(String clientId) {
        StringBuilder builder = new StringBuilder(30);
        String remoteIpMd5 =  Hashing.md5().hashString(clientId, Charsets.UTF_8).toString().substring(0,8);
        builder.append(remoteIpMd5).append(SEPARATOR)
               .append(hostIpMd5).append(SEPARATOR)
               .append(Long.toHexString(System.currentTimeMillis())).append(SEPARATOR)
               .append(UUID.randomUUID().toString().substring(0,4));
        
        return builder.toString();
    }
    
}