package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;
import com.taotao.service.ItemParamService;

/**
 * 商品规格参数管理
 * @author wlnner
 *
 */
@Controller
@RequestMapping("/item/param")
public class ItemParamController {

	@Autowired
	private ItemParamService itemParamService;
	
	@RequestMapping("/list")
	@ResponseBody
	public EUDataGridResult getItemParamList(@RequestParam(defaultValue="1")Integer page,@RequestParam(defaultValue="30")Integer rows){
		return itemParamService.getItemParamList(page, rows);
	}
	
	@RequestMapping("/query/itemcatid/{itemCatId}")
	@ResponseBody
	public TaotaoResult getItemParamByCid(@PathVariable("itemCatId")Long cid){
		return itemParamService.getItemParamByCid(cid);
	}
	
	@RequestMapping("/save/{cid}")
	@ResponseBody
	public TaotaoResult insertItemParam(/*使用RESTful格式必须有此注解*/@PathVariable Long cid,String paramData){
		TbItemParam itemParam=new TbItemParam();
		itemParam.setItemCatId(cid);
		itemParam.setParamData(paramData);
		return itemParamService.insertItemParam(itemParam);
	}
}
