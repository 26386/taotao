package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemParamAdviceMapper;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.model.TbItemParamModel;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.pojo.TbItemParamExample.Criteria;
import com.taotao.service.ItemParamService;

@Service
public class ItemParamServiceImpl implements ItemParamService {

	@Autowired
	private TbItemParamMapper itemParamMapper;
	@Autowired
	private TbItemParamAdviceMapper itemParamAdviceMapper;
	
	@Override
	public EUDataGridResult getItemParamList(Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		List<TbItemParamModel> list = itemParamAdviceMapper.getAllItemParam();
		EUDataGridResult ret=new EUDataGridResult();
		PageInfo<TbItemParamModel> pageInfo=new PageInfo<>(list);
		ret.setRows(list);
		ret.setTotal(pageInfo.getTotal());
		return ret;
	}
	
	public TaotaoResult getItemParamByCid(Long cid){
		TbItemParamExample example=new TbItemParamExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemCatIdEqualTo(cid);
		List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);
		if(list!=null&&list.size()>0){
			return TaotaoResult.ok(list.get(0));		
		}
		return TaotaoResult.ok();
	}

	public TaotaoResult insertItemParam(TbItemParam itemParam){
		itemParam.setCreated(new Date());
		itemParam.setUpdated(new Date());
		itemParamMapper.insert(itemParam);
		return TaotaoResult.ok();
	}
}
