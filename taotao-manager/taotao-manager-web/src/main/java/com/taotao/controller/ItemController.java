package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	//RESTful编码风格
	@RequestMapping("/getItemById.do/{id}")
	@ResponseBody
	public TbItem getItemById(@PathVariable("id")Long itemId){
		return itemService.getItemById(itemId);
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public EUDataGridResult getItemList(Integer page,Integer rows){
		return itemService.getItemList(page, rows);
	}
	
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createItem(TbItem item,String desc,String itemParams) throws Exception{
		return itemService.createItem(item,desc,itemParams);
	}
}
