package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.service.ContentService;

@Service
public class ContentServiceImpl implements ContentService{

	@Autowired
	private TbContentMapper contentMapper;
	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${REST_CONTENT_SYNC_URL}")
	private String REST_CONTENT_SYNC_URL;
	
	public EUDataGridResult listContent(Integer pageNum,Integer pageSize,Long categoryId){
		PageHelper.startPage(pageNum, pageSize);
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list=contentMapper.selectByExample(example);
		EUDataGridResult result=new EUDataGridResult();
		PageInfo<TbContent> pageInfo=new PageInfo<>(list);
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}
	
	public TaotaoResult insertContent(TbContent content){
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		
		//添加缓存同步逻辑
		try {
			HttpClientUtil.doGet(REST_BASE_URL+REST_CONTENT_SYNC_URL+content.getCategoryId());			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok();
	}
	
	public TaotaoResult updateContent(TbContent content){
		content.setUpdated(new Date());
		contentMapper.updateByPrimaryKeySelective(content);
		HttpClientUtil.doGet(REST_BASE_URL+REST_CONTENT_SYNC_URL+content.getCategoryId());
		return TaotaoResult.ok();
	}
}
