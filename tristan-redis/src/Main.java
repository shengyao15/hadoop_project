import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class Main {
	
	public static Jedis jedis;
	
	static{
		jedis =  new Jedis("192.168.1.103", 6379);
		jedis.flushAll();
	}
	public static void main(String[] args) {
		
		//testA();
		testB();
		
	}

	private static void testA() {
		jedis.set("xxx", "yyy");
		System.out.println(jedis.get("xxx"));
	}

	public static void testB() {
		/*long status = jedis.sadd("foo", "a");
		System.out.println(status);

		status = jedis.sadd("foo", "a");
		System.out.println(status);*/
		
		jedis.sadd("foo", "a");
		jedis.sadd("foo", "b");
		jedis.sadd("foo", "c");
		jedis.sadd("foo", "b");
		
		Set<String> expected = jedis.smembers("foo");
		for (String s : expected) {
			System.out.println(s);
		}
		
		
	}
}
