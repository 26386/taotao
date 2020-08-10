package com.taotao.rest.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.ContentService;

/**
 * 内容管理
 * @author wlnner
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${INDEX_CONTENT_REDIS_KEY}")
	private String INDEX_CONTENT_REDIS_KEY;
	
	public List<TbContent> getContentList(Long contentCid){
		//从缓存中取内容 
		try {
			String jsonList = jedisClient.hget(INDEX_CONTENT_REDIS_KEY, contentCid+"");
			if(!StringUtils.isBlank(jsonList)){
				List<TbContent> list = JsonUtils.jsonToList(jsonList, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(contentCid);
		List<TbContent> list = contentMapper.selectByExample(example);
		
		//向缓存中添加内容
		try {
			//把list转换成字符串
			String value = JsonUtils.objectToJson(list);
			jedisClient.hset(INDEX_CONTENT_REDIS_KEY,contentCid+"", value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
