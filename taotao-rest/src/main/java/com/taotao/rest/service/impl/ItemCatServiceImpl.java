package com.taotao.rest.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;

/**
 * 商品分类服务
 * @author wlnner
 *
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${INDEX_CATEGORY_REDIS_KEY}")
	private String INDEX_CATEGORY_REDIS_KEY;
	
	public CatResult getItemCatList(){
		CatResult result=new CatResult();
		result.setData(getCatList(0L));
		return result;
	}
	
	//使用递归查询分类列表
	private List<?> getCatList(Long parentId){
		try {
			String jsonData = jedisClient.get(INDEX_CATEGORY_REDIS_KEY);
			if(StringUtils.isNotBlank(jsonData)){
				List<Object> list = JsonUtils.jsonToList(jsonData, Object.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemCatExample example=new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		List<Object> resultList = this.getResultList(list, parentId);
		try {
			jedisClient.set(INDEX_CATEGORY_REDIS_KEY, JsonUtils.objectToJson(resultList));			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	private List<Object> getResultList(List<TbItemCat> list,Long parentId){
		List<Object> resultList=new ArrayList<>();
		int count=0;
		for (TbItemCat tbItemCat : list) {
			if(tbItemCat.getIsParent()){
				CatNode catNode=new CatNode();
				if(parentId==0){
					count++;
					if(count>=15){
						break;
					}
					catNode.setName("<a href='/products/"+tbItemCat.getId()+".html'>"+tbItemCat.getName()+"</a>");
				}else {
					catNode.setName(tbItemCat.getName());
				}
				catNode.setUrl("/products/"+tbItemCat.getId()+".html");
				catNode.setItem(getCatList(tbItemCat.getId()));
				resultList.add(catNode);
			}else {
				resultList.add("/products/"+tbItemCat.getId()+".html|"+tbItemCat.getName());
			}
		}
		return resultList;
	}
	
	
	
}
