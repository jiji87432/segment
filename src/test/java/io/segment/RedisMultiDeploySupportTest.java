package io.segment;

import io.segment.support.CacheObject;

import java.util.HashMap;
import java.util.Map;

public class RedisMultiDeploySupportTest {

    public static void main(String[] args) {

        Map<String, Object> objectMap = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
		{
            put("username", "zhangyw");
            put("email", "alemcity@foxmail.com");
            put("git_link", "http://git.oschina.net/git-zyw");
        }};

        Segment.getChannel().set("user_cache", "user", objectMap);

        CacheObject object = Segment.getChannel().get("user_cache", "user");
        @SuppressWarnings("rawtypes")
		Map map = (Map) object.getValue();
        System.out.println(map.get("git_link"));
    }
}
