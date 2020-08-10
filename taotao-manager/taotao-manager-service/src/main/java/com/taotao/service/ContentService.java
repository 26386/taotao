package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {

	EUDataGridResult listContent(Integer pageNum,Integer pageSize,Long categoryId);
	
	TaotaoResult insertContent(TbContent content);
	
	TaotaoResult updateContent(TbContent content);
}
