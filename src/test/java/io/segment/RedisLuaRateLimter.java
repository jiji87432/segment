package io.segment;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;

import com.google.common.io.CharStreams;

/**
 * Lua 嵌入 Redis 优势: 
 * 减少网络开销: 不使用 Lua 的代码需要向 Redis 发送多次请求, 而脚本只需一次即可, 减少网络传输;
 * 原子操作: Redis 将整个脚本作为一个原子执行, 无需担心并发, 也就无需事务;
 * 复用: 脚本会永久保存 Redis 中, 其他客户端可继续使用.
 * 
 * @author lry
 */
public class RedisLuaRateLimter {

	public static SimpleDateFormat second = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
	public static SimpleDateFormat minute = new SimpleDateFormat("yyyy-MM-dd/HH:mm:00");
	public static SimpleDateFormat hour = new SimpleDateFormat("yyyy-MM-dd/HH:00:00");
	public static SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd/01:00:00");
	public static SimpleDateFormat month = new SimpleDateFormat("yyyy-MM-01/01:00:00");
	public static SimpleDateFormat year = new SimpleDateFormat("yyyy-01-01/01:00:00");
	
	public static void main(String[] args) {
		RedisLuaRateLimter d1= new RedisLuaRateLimter();
		Jedis connection = new Jedis("127.0.0.1", 6379);
		try {
			for (int i = 0; i < 100; i++) {
				boolean res = d1.accessLimit("10.24.10.11", 5, 10, connection);
				System.out.println(res);
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 数据格式
	 * 
	 * @param ip
	 * @param limit
	 * @param timeout
	 * @param jedis
	 * @return
	 * @throws IOException
	 */
	public boolean accessLimit(String ip, int limit, int timeout, Jedis jedis) throws IOException {
	    List<String> keys = new ArrayList<String>();
	    keys.add(ip);

	    Date date = new Date();
	    List<String> argv = new ArrayList<String>();
	    argv.add(String.valueOf(limit));
	    argv.add(String.valueOf(timeout));
	    argv.add(second.format(date));
	    argv.add(minute.format(date));
	    argv.add(hour.format(date));
	    argv.add(day.format(date));
	    argv.add(month.format(date));
	    argv.add(year.format(date));
	    
	    return 1 == (long) jedis.eval(loadScriptListString("script.lua"), keys, argv);
	}

	// 加载Lua代码
	public String loadScriptListString(String scriptName) throws IOException {
		Reader reader = new InputStreamReader(Client.class.getClassLoader().getResourceAsStream(scriptName));
		return CharStreams.toString(reader);
	}
	
}
