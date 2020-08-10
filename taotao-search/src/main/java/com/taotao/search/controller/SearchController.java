package com.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchService;

@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@RequestMapping(value="/query",method=RequestMethod.GET)
	@ResponseBody
	public TaotaoResult search(@RequestParam("q")String queryCondition
			, @RequestParam(value="page",defaultValue="1")Integer pageNum
			, @RequestParam(value="rows",defaultValue="60")Integer pageSize){
		return searchService.search(queryCondition, pageNum, pageSize);
	}
}
