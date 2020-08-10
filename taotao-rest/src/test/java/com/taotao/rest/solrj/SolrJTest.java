package com.taotao.rest.solrj;

import static org.junit.Assert.*;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrJTest {

	//添加或修改索引
	@Test
	public void testAddDocument() throws Exception {
		//创建连接
		SolrServer solrServer=new HttpSolrServer("http://192.168.79.128:8080/solr");
		//创建文档对象
		SolrInputDocument document=new SolrInputDocument();
		document.addField("id", "test001");
		document.addField("item_title", "测试商品1");
		document.addField("item_price", 123456);
		//把文档对写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	
	@Test
	public void testDeleteDocument() throws Exception {
		SolrServer solrServer=new HttpSolrServer("http://192.168.79.128:8080/solr");
		solrServer.deleteById("test001");
		solrServer.commit();
	}
	
	//查询生成好的solr索引库中的数据
	@Test
	public void testQueryDocument() throws Exception {
		SolrServer solrServer=new HttpSolrServer("http://192.168.79.128:8080/solr");
		SolrQuery solrQuery=new SolrQuery();
		//设置查询条件
		solrQuery.setQuery("*:*");
		solrQuery.setStart(0);//solr的起始页是从0开始的
		solrQuery.setRows(50);
		QueryResponse resp = solrServer.query(solrQuery);
		SolrDocumentList solrDocumentList = resp.getResults();
		System.out.println("查询结果记录总数:"+solrDocumentList.getNumFound());
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println("-----------------------------------------");
		}
		solrServer.commit();
	}
}
