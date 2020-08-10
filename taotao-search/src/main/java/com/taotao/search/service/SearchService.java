package com.taotao.search.service;

import com.taotao.common.pojo.TaotaoResult;

public interface SearchService {

	TaotaoResult search(String queryCondition, Integer pageNum, Integer pageSize);
}
