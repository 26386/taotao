package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;

public interface ItemParamService {
	
	EUDataGridResult getItemParamList(Integer page,Integer rows);
	
	TaotaoResult getItemParamByCid(Long cid);
	
	TaotaoResult insertItemParam(TbItemParam itemParam);
}
