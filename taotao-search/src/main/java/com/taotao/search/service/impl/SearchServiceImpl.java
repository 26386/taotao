package com.taotao.search.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.pojo.SearchResult;
import com.taotao.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;

	public TaotaoResult search(String queryCondition, Integer pageNum, Integer pageSize) {
		if (StringUtils.isBlank(queryCondition)) {
			return TaotaoResult.build(400, "查询条件不能为空");
		}
		SearchResult searchResult=null;
		try {
			queryCondition=new String(queryCondition.getBytes("ISO8859-1"), "UTF-8");
			SolrQuery query = new SolrQuery(queryCondition);
			query.setQuery(queryCondition);
			/*query.setQuery("item_sell_point:"+queryCondition);
			query.setQuery("item_category_name:"+queryCondition);*/
			// 设置分页
			query.setStart((pageNum - 1) * pageSize);//这里需要特别注意
			query.setRows(pageSize);
			// 设置默认搜索域
			query.set("df", "item_keywords");
			// 设置高亮显示
			query.setHighlight(true);
			query.addHighlightField("item_title");
			query.setHighlightSimplePre("<em style=\"color:red\">");
			query.setHighlightSimplePost("</em>");
			searchResult = searchDao.search(query);
			// 计算查询结果总页数
			Long total = searchResult.getTotal();
			Long totalPage = total % pageSize > 0 ? total / pageSize + 1 : total / pageSize;
			searchResult.setCurrentPage(Long.valueOf(pageNum));
			searchResult.setTotalPage(totalPage);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		return TaotaoResult.ok(searchResult);
	}
}
