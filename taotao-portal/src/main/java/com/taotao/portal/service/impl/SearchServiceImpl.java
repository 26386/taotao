package com.taotao.portal.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.portal.pojo.SearchResult;
import com.taotao.portal.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

	@Value("${SEARCH_BASE_URL}")
	private String SEARCH_BASE_URL;

	public SearchResult search(String queryCondition, Integer page) {
		Map<String, String> map = new HashMap<>();
		map.put("q", queryCondition);
		map.put("page", page + "");
		try {
			String respJson = HttpClientUtil.doGet(SEARCH_BASE_URL, map);
			TaotaoResult taotaoRet = TaotaoResult.formatToPojo(respJson, SearchResult.class);
			if (taotaoRet.getStatus() == 200) {
				
				SearchResult data = (SearchResult) taotaoRet.getData();
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
