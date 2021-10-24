package dev.cache.util;

import redis.clients.jedis.Jedis;

/**
 * 
 * @author swGuan
 *
 */
public class RedisPractice {

	public static void main(String[] args) {
		Jedis redis = null;
		// set 10 (key, val) pair
		for (int i = 1; i <= 10; i++) {
			redis = RedisPool.getResource();
			redis.set("LOOP" + i, "val" + i);
			if (redis != null) {
				redis.close();
			}
		}
		// get 10 (key)
		for (int i = 1; i <= 10; i++) {
			long start = System.currentTimeMillis();
			redis = RedisPool.getResource();
			String str = new StringBuilder("LOOP").append(i).toString();
			System.out.printf("Get %s from redis: %s with %d millis\n", str, redis.get(str),
					System.currentTimeMillis() - start);
			if (redis != null) {
				redis.close();
			}
		}
	}

}
