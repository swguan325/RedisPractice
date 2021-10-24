package dev.cache.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisPool {

	private static JedisPool pool = null;
	private static String ADDR = "127.0.0.1";
	private static Integer PORT = 6379;

	private static Integer MAX_TOTAL = 512;
	private static Integer MAX_IDLE = 100;
	private static Integer MAX_WAIT_MILLIS = 3000;

	static {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(MAX_TOTAL);
			config.setMaxIdle(MAX_IDLE);
			config.setMaxWaitMillis(MAX_WAIT_MILLIS);
			pool = new JedisPool(config, ADDR, PORT);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public synchronized static Jedis getResource() {
		try {
			if (pool != null) {
				Jedis jedis = pool.getResource();
				return jedis;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
