package com.taotao.portal.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.portal.pojo.SearchResult;
import com.taotao.portal.service.SearchService;

/**
 * taotao门户查询商品controller
 * @author wlnner
 *
 */
@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@RequestMapping("/search")
	public String search(@RequestParam("q")String queryCondition,@RequestParam(defaultValue="1")Integer page,Model model){
		try {
			queryCondition=new String(queryCondition.getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		SearchResult result = searchService.search(queryCondition, page);
		model.addAttribute("query", queryCondition);
		model.addAttribute("itemList", result.getItemList());
		model.addAttribute("totalPages", result.getTotalPage());
		model.addAttribute("page", page);
		return "search";
	}
}
