package com.taotao.rest.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.rest.dao.JedisClient;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

//jedis单机版功能
public class JedisClientSingle implements JedisClient {

	@Autowired
	private JedisPool jedisPool;
	
	@Override
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		String value = jedis.get(key);
		jedis.close();
		return value;
	}

	@Override
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		String ret = jedis.set(key, value);
		jedis.close();
		return ret;
	}

	@Override
	public String hget(String hkey, String key) {
		Jedis jedis = jedisPool.getResource();
		String value = jedis.hget(hkey, key);
		jedis.close();
		return value;
	}

	@Override
	public Long hset(String hkey, String key, String value) {
		Jedis jedis = jedisPool.getResource();
		Long code = jedis.hset(hkey,key,value);
		jedis.close();
		return code;
	}

	@Override
	public Long incr(String key) {
		Jedis jedis = jedisPool.getResource();
		Long value = jedis.incr(key);
		jedis.close();
		return value;
	}

	@Override
	public Long expire(String key, Integer seconds) {
		Jedis jedis = jedisPool.getResource();
		Long code = jedis.expire(key, seconds);
		jedis.close();
		return code;
	}

	@Override
	public Long ttl(String key) {
		Jedis jedis = jedisPool.getResource();
		Long seconds = jedis.ttl(key);
		jedis.close();
		return seconds;
	}

	@Override
	public Long del(String key) {
		Jedis jedis = jedisPool.getResource();
		Long code = jedis.del(key);
		jedis.close();
		return code;
	}

	@Override
	public Long hdel(String hkey, String key) {
		Jedis jedis = jedisPool.getResource();
		Long code = jedis.hdel(hkey,key);
		jedis.close();
		return code;
	}

}
