package com.taotao.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbContent;
import com.taotao.portal.service.ContentService;

@Service
public class ContentServiceImpl implements ContentService{

	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${REST_INDEX_AD_URL}")
	private String REST_INDEX_AD_URL;
	
	public String getContentList(){
		//调用服务层的服务
		String resp = HttpClientUtil.doGet(REST_BASE_URL+REST_INDEX_AD_URL);
		try{
		List<TbContent> list = (List<TbContent>)TaotaoResult.formatToList(resp, TbContent.class).getData();
		List<Map<String, Object>> ads=new ArrayList<>();
		for (TbContent tbContent : list) {
			Map<String, Object> map=new HashMap<>();
			ads.add(map);
			map.put("src", tbContent.getPic());
			map.put("height", 240);
			map.put("width", 670);
			map.put("srcB", tbContent.getPic2());
			map.put("heightB", 240);
			map.put("widthB", 550);
			map.put("href", tbContent.getUrl());
			map.put("alt", tbContent.getSubTitle());
		}
		return JsonUtils.objectToJson(ads);
		}catch(Exception e){
			e.printStackTrace();
			return ExceptionUtil.getStackTrace(e);
		}
	}
}
