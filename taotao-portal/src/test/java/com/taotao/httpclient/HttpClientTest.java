package com.taotao.httpclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HttpClientTest {

	@Test
	public void doGet() throws Exception {
		//创建HttpClient对象
		CloseableHttpClient httpClient=HttpClients.createDefault();
		//创建Get对象
		HttpGet get=new HttpGet("http://www.sogou.com");
		//执行请求
		CloseableHttpResponse resp = httpClient.execute(get);
		//获取响应结果
		int statusCode = resp.getStatusLine().getStatusCode();
		System.out.println(statusCode);
		HttpEntity entity = resp.getEntity();
		System.out.println(EntityUtils.toString(entity,"utf-8"));
		//关闭资源
		resp.close();
		httpClient.close();
	}
	
	@Test
	public void doGetWithParam() throws Exception {
		CloseableHttpClient httpClient=HttpClients.createDefault();
		URIBuilder uriBuilder=new URIBuilder("http://127.0.0.1:8080/content/category/list");
		uriBuilder.addParameter("parentId", "0");
		HttpGet get=new HttpGet(uriBuilder.build());
		CloseableHttpResponse resp = httpClient.execute(get);
		//获取响应结果
		int statusCode = resp.getStatusLine().getStatusCode();
		System.out.println(statusCode);
		HttpEntity entity = resp.getEntity();
		System.out.println(EntityUtils.toString(entity,"utf-8"));
		//关闭资源
		resp.close();
		httpClient.close();
	}
	
	@Test
	public void doPost() throws Exception {
		CloseableHttpClient httpClient=HttpClients.createDefault();
		/*
		URIBuilder uriBuilder=new URIBuilder("http://localhost:8082/httpclient/post.html");
		uriBuilder.addParameter("username", "iwlnner");
		uriBuilder.addParameter("password", "666666");
		HttpPost post=new HttpPost(uriBuilder.build());
		*/
		HttpPost post=new HttpPost("http://localhost:8082/httpclient/post.action");
		CloseableHttpResponse resp = httpClient.execute(post);
		HttpEntity entity = resp.getEntity();
		System.out.println(EntityUtils.toString(entity));
		resp.close();
		httpClient.close();
	}
	
	@Test
	public void doPostWithParam() throws Exception {
		CloseableHttpClient httpClient=HttpClients.createDefault();
		HttpPost post=new HttpPost("http://localhost:8082/httpclient/post.action");
		//创建Entity模拟一个表单
		List<NameValuePair> list=new ArrayList<>();
		list.add(new BasicNameValuePair("username", "孙培洋"));
		list.add(new BasicNameValuePair("password", "123456"));
		StringEntity entity=new UrlEncodedFormEntity(list,"utf-8");
		//设置请求的内容
		post.setEntity(entity);
		CloseableHttpResponse resp = httpClient.execute(post);
		System.out.println(EntityUtils.toString(resp.getEntity(),"utf-8"));
		resp.close();
		httpClient.close();
	}
}
