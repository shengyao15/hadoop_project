package tristan;

import java.io.PrintWriter;

import redis.clients.jedis.Jedis;

public class BaiduRedis {
	public static void main(String[] args) {
		String databaseName = " gJaUNMsUWIpWqeyNMQJW";
		String host = "redis.duapp.com";
		String portStr = "80";
		int port = Integer.parseInt(portStr);
		String username = " M7VYjUNhrUxRzAK60lZezkAI";// 用户名(api key);
		String password = "W2lTKtmvxRBFlqUn0F60yRQiiLnh73La";// 密码(secret key)

		/****** 2. 接着连接并选择数据库名为databaseName的服务器 ******/
		Jedis jedis = new Jedis(host, port);
		jedis.connect();
		jedis.auth(username + "-" + password + "-" + databaseName);
		// 删除所有redis数据库中的key-value
		jedis.flushDB();
		// 简单的key-value设置
		jedis.set("name", "bae");
		System.out.println("name | " + jedis.get("name"));
	}
}
