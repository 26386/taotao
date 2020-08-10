package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;

public interface ContentCatgoryService {

	List<EUTreeNode> getCategoryList(Long parentId);
	
	TaotaoResult insertContentCategory(Long parentId,String name);

	TaotaoResult deleteContentCategory(Long id);
	
	TaotaoResult updateContentCategoryName(Long id,String name);
}
