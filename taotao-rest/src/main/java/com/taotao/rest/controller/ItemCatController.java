package com.taotao.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.utils.JsonUtils;
import com.taotao.rest.service.ItemCatService;

@Controller
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;
	
	/*@RequestMapping(value="/itemcat/list",produces=MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
	@ResponseBody
	public String getItemCatList(String callback){
		String json = JsonUtils.objectToJson(itemCatService.getItemCatList());
		String ret=callback+"("+json+");";
		return ret;
	}*/
	
	@RequestMapping("/itemcat/list")
	@ResponseBody
	public Object getItemCatList(String callback){
		MappingJacksonValue jacksonValue=new MappingJacksonValue(itemCatService.getItemCatList());
		jacksonValue.setJsonpFunction(callback);
		return jacksonValue;
	}
}
