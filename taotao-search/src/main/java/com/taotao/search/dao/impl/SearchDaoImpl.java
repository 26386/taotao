package com.taotao.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.search.dao.SearchDao;
import com.taotao.search.pojo.Item;
import com.taotao.search.pojo.SearchResult;

@Repository
public class SearchDaoImpl implements SearchDao{
	
	@Autowired
	private SolrServer solrServer;
	
	public SearchResult search(SolrQuery query) throws Exception{
		List<Item> items=new ArrayList<>();
		SearchResult result=new SearchResult();
		//根据查询条件查询索引库
		QueryResponse resp = solrServer.query(query);
		//获取查询到的结果
		SolrDocumentList solrDocumentList = resp.getResults();
		//获取并设置查询结果总数
		result.setTotal(solrDocumentList.getNumFound());
		//取高亮显示:第一个Map的键是文档的ID，第二个Map的键是高亮显示的字段名  
		Map<String, Map<String, List<String>>> highlighting = resp.getHighlighting();
		//获取商品列表
		for (SolrDocument solrDocument : solrDocumentList) {
			Item item=new Item();
			item.setId((String)solrDocument.get("id"));
			//取高亮显示的结果
			List<String> hlTitles = highlighting.get(solrDocument.get("id")).get("item_title");
			String title="";
			if(hlTitles!=null && hlTitles.size()>0){
				title=hlTitles.get(0);
			}else {
				title=(String)solrDocument.get("item_title");
			}
			item.setTitle(title);
			item.setImage((String)solrDocument.get("item_image"));
			item.setPrice((Long)solrDocument.get("item_price"));
			item.setSell_point((String)solrDocument.get("item_sell_point"));
			item.setCategory_name((String)solrDocument.get("item_category_name"));
			items.add(item);
		}
		result.setItemList(items);
		solrServer.commit();
		return result;
	}
	
}
