package com.taotao.search.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

public interface ItemService {

	TaotaoResult importAllItems();
	TaotaoResult importItem(TbItem item,String categoryName,String desc);
}
