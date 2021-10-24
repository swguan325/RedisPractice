package dev.url;

import java.util.Scanner;

import org.apache.commons.codec.digest.DigestUtils;

import dev.cache.util.RedisPool;
import redis.clients.jedis.Jedis;

/**
 * 1. URL Shortener with Redis cache
 *
 */
public class Shortener {

	private static final int NUMBER_61 = 0x0000003d;
	private static final int URL_LENGTH = 6;

	static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B',
			'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z' };

	// 正常情況下同樣的字串不論 shorten 幾次，結果都會一樣
	public String shorten(String longUrl) {
		String sha256Hex = DigestUtils.sha256Hex(longUrl);
		String subString = sha256Hex.substring(0, 8);
		long lHexLong = 0x3FFFFFFF & Long.parseLong(subString, 16);
		String outChars = "";
		for (int j = 0; j < URL_LENGTH; j++) {
			long index = NUMBER_61 & lHexLong;
			outChars += DIGITS[(int) index];
			lHexLong = lHexLong >> 5;
		}
		return outChars;
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Please input your url: ");
		String inputUrl = scanner.next();
		String key = new Shortener().shorten(inputUrl);

		Jedis redis = RedisPool.getResource();
		if (redis.get(key) == null) {
			redis.set(key, inputUrl);
		} else {
			System.out.printf("Url %s already exist, please use key %s\n", inputUrl, key);
		}

		long start = System.currentTimeMillis();
		for (int i = 1; i <= 10000; i++) {
			System.out.printf("Get %s from redis: %s\n", key, redis.get(key));
		}
		long end = System.currentTimeMillis();
		System.out.printf("Time cose : %s %s\n", end - start, "ms");
		if (redis != null) {
			redis.close();
		}
	}

}
