package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;

@Controller
@RequestMapping("/content")
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/query/list")
	@ResponseBody
	public EUDataGridResult listContent(@RequestParam(defaultValue="1")Integer page
			,@RequestParam(defaultValue="10")Integer rows,Long categoryId){
		return contentService.listContent(page, rows, categoryId);
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public TaotaoResult insertContent(TbContent content){
		return contentService.insertContent(content);
	}
	
	@RequestMapping("/edit")
	@ResponseBody
	public TaotaoResult updateContent(TbContent content){
		return contentService.updateContent(content);	
	}
}
