package com.taotao.rest.jedis;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {

	@Test
	public void testSingleJedis() throws Exception {
		Jedis jedis=new Jedis("192.168.79.128", 6379);
		jedis.set("key1", "value1");
		System.out.println(jedis.get("key1"));
		jedis.close();
	}
	
	//使用jedis连接池
	@Test
	public void testJedisPool() throws Exception {
		JedisPool pool=new JedisPool("192.168.79.128", 6379);
		Jedis jedis = pool.getResource();
		System.out.println(jedis.get("key1"));
		//每次用完关闭资源，jedis连接池才能回收，以供下次获取
		jedis.close();
		pool.close();
	}
	
	//测试redis集群
	@Test
	public void testJedisCluster() throws Exception {
		String host="192.168.79.128";
		Set<HostAndPort> nodes=new HashSet<>();
		nodes.add(new HostAndPort(host, 7001));
		nodes.add(new HostAndPort(host, 7002));
		nodes.add(new HostAndPort(host, 7003));
		nodes.add(new HostAndPort(host, 7004));
		nodes.add(new HostAndPort(host, 7005));
		nodes.add(new HostAndPort(host, 7006));
		JedisCluster cluster=new JedisCluster(nodes);
		System.out.println(cluster.get("a"));
		cluster.set("key2", "value2");
		System.out.println(cluster.get("key2"));
		cluster.close();
	}
	
	//spring管理单机版Jedis
	@Test
	public void testSpringJedisSingle() throws Exception {
		ApplicationContext apt=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-jedis.xml");
		JedisPool pool = apt.getBean("redisClient", JedisPool.class);
		Jedis jedis = pool.getResource();
		System.out.println(jedis.get("key1"));
		jedis.close();
		pool.close();
	}
	
	//spring管理集群版Jedis
	@Test
	public void testSpringJedisCluster() throws Exception {
		ApplicationContext apt=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-jedis.xml");
		JedisCluster cluster = apt.getBean("redisClient", JedisCluster.class);
		System.out.println(cluster.get("key1"));
		System.out.println(cluster.get("key2"));
		cluster.close();
	}
}
