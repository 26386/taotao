package com.taotao.rest.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.rest.dao.JedisClient;

import redis.clients.jedis.JedisCluster;

//jedis集群版功能
public class JedisClientCluster implements JedisClient {

	@Autowired
	private JedisCluster jedisCluster;
	
	@Override
	public String get(String key) {
		return jedisCluster.get(key);
	}

	@Override
	public String set(String key, String value) {
		return jedisCluster.set(key, value);
	}

	@Override
	public String hget(String hkey, String key) {
		return jedisCluster.hget(hkey, key);
	}

	@Override
	public Long hset(String hkey, String key, String value) {
		return jedisCluster.hsetnx(hkey, key, value);
	}

	@Override
	public Long incr(String key) {
		return jedisCluster.incr(key);
	}

	@Override
	public Long expire(String key, Integer seconds) {
		return jedisCluster.expire(key, seconds);
	}

	@Override
	public Long ttl(String key) {
		return jedisCluster.ttl(key);
	}

	@Override
	public Long del(String key) {
		return jedisCluster.del(key);
	}

	@Override
	public Long hdel(String hkey, String key) {
		return jedisCluster.hdel(hkey, key);
	}

}
