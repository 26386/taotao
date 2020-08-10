package com.taotao.rest.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.pojo.TbItemParamItemExample.Criteria;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_ITEM_KEY}")
	private String REDIS_ITEM_KEY;
	@Value("${REDIS_ITEM_EXPIRE}")
	private Integer REDIS_ITEM_EXPIRE;

	// 获取商品基本信息
	public TaotaoResult getItemBaseInfo(Long itemId) {
		String key = REDIS_ITEM_KEY + ":" + itemId + ":base";
		// 从缓存中获取商品信息
		try {
			String json = jedisClient.get(key);
			if (StringUtils.isNotBlank(json)) {
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				return TaotaoResult.ok(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		try {
			// 把商品基本信息写入缓存
			jedisClient.set(key, JsonUtils.objectToJson(item));
			// 设置key的有效期
			jedisClient.expire(key, REDIS_ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok(item);
	}

	// 获取商品详情
	public TaotaoResult getItemDesc(Long itemId) {
		String key = REDIS_ITEM_KEY + ":" + itemId + ":desc";
		// 从缓存中获取商品信息
		try {
			String json = jedisClient.get(key);
			if (StringUtils.isNotBlank(json)) {
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return TaotaoResult.ok(itemDesc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		try {
			// 把商品基本信息写入缓存
			jedisClient.set(key, JsonUtils.objectToJson(itemDesc));
			// 设置key的有效期
			jedisClient.expire(key, REDIS_ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok(itemDesc);
	}
	
	//获取商品的规格参数
	public TaotaoResult getItemParam(Long itemId){
		String key = REDIS_ITEM_KEY + ":" + itemId + ":param";
		// 从缓存中获取商品信息
		try {
			String json = jedisClient.get(key);
			if (StringUtils.isNotBlank(json)) {
				TbItemParamItem itemParamItem = JsonUtils.jsonToPojo(json, TbItemParamItem.class);
				return TaotaoResult.ok(itemParamItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemParamItemExample example=new TbItemParamItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);
		if(list!=null &list.size()>0){
			TbItemParamItem itemParamItem=list.get(0);
			try {
				// 把商品基本信息写入缓存
				jedisClient.set(key, JsonUtils.objectToJson(itemParamItem));
				// 设置key的有效期
				jedisClient.expire(key, REDIS_ITEM_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return TaotaoResult.ok(itemParamItem);
		}
		return TaotaoResult.build(400, "无规格参数");
	}
}
