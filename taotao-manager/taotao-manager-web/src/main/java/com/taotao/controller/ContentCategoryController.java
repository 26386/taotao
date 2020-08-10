package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.service.ContentCatgoryService;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {

	@Autowired
	private ContentCatgoryService contentCatgoryService;
	
	@RequestMapping("/list")
	@ResponseBody
	public List<EUTreeNode> getCategoryList(@RequestParam(value="id",defaultValue="0")Long parentId){
		return contentCatgoryService.getCategoryList(parentId);
	}
	
	@RequestMapping("/create")
	@ResponseBody
	public TaotaoResult createContentCategory(Long parentId,String name){
		return contentCatgoryService.insertContentCategory(parentId, name);
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult deleteContentCategory(Long id){
		//System.out.println(parentId);
		return contentCatgoryService.deleteContentCategory(id);
	}
	
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult updateContentCategoryName(Long id,String name){
		return contentCatgoryService.updateContentCategoryName(id, name);
	}
	
}
