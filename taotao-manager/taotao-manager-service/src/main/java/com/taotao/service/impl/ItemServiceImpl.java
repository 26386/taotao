package com.taotao.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;

//商品管理
@Service
public class ItemServiceImpl implements ItemService{

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;
	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Value("${SEARCH_BASE_URL}")
	private String SEARCH_BASE_URL;
	
	@Override
	public TbItem getItemById(Long itemId) {
		TbItemExample example=new TbItemExample();
		//创建查询条件对象
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> items = itemMapper.selectByExample(example);
		return items!=null&&items.size()>0?items.get(0):null;
	}
	
	public List<TbItem> listItem(){
		return itemMapper.listItem();
	}

	@Override
	public EUDataGridResult getItemList(int page, int rows) {
		TbItemExample example=new TbItemExample();
		PageHelper.startPage(page, rows);
		List<TbItem> list = itemMapper.selectByExample(example);
		EUDataGridResult ret=new EUDataGridResult();
		PageInfo<TbItem> pageInfo=new PageInfo<>(list);
		ret.setTotal(pageInfo.getTotal());
		ret.setRows(list);
		return ret;
	}

	@Override
	public TaotaoResult createItem(TbItem item,String desc,String paramData) throws Exception {
		Long itemId=IDUtils.getItemId();
		item.setId(itemId);
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//保证这两个插入操作属于同一个事务
		itemMapper.insert(item);
		TaotaoResult result = this.insertItemDesc(itemId, desc);
		//控制事务回滚
		if(result.getStatus()!=200){
			throw new Exception();
		}
		result=insertItemParamItem(itemId, paramData);
		if(result.getStatus()!=200){
			throw new Exception();
		}
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(item.getCid());
		Map<String, String> map=new HashMap<>();
		map.put("item", JsonUtils.objectToJson(item));
		map.put("desc", desc);
		map.put("categoryName", itemCat.getName());
		String respJson = HttpClientUtil.doGet(SEARCH_BASE_URL,map);
		result = TaotaoResult.format(respJson);
		if(result.getStatus()!=200){
			throw new Exception();
		}
		return TaotaoResult.ok();
	}
	
	private TaotaoResult insertItemDesc(Long itemId,String desc){
		TbItemDesc itemDesc=new TbItemDesc(); 
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDescMapper.insert(itemDesc);
		return TaotaoResult.ok();
	}
	
	private TaotaoResult insertItemParamItem(Long itemId,String paramData){
		TbItemParamItem itemParamItem=new TbItemParamItem();
		itemParamItem.setItemId(itemId);
		itemParamItem.setParamData(paramData);
		itemParamItem.setCreated(new Date());
		itemParamItem.setUpdated(new Date());
		itemParamItemMapper.insert(itemParamItem);
		return TaotaoResult.ok();
	}

}
