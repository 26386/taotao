package com.taotao.search.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.search.service.ItemService;

/**
 * 索引库维护
 * 
 * @author wlnner
 *
 */
@Controller
@RequestMapping("/manage")
public class ItemController {

	@Autowired
	private ItemService itemService;

	/**
	 * 导入商品数据到索引库
	 * 
	 * @return
	 */
	@RequestMapping("/importall")
	@ResponseBody
	public TaotaoResult importAllItems() {
		return itemService.importAllItems();
	}

	@RequestMapping(value = "/import")
	@ResponseBody
	public TaotaoResult importItem(@RequestParam("item") String itemJson, String categoryName, String desc) {
		itemJson = toCharsetUTF(itemJson);
		categoryName = toCharsetUTF(categoryName);
		desc = toCharsetUTF(desc);
		TbItem item = JsonUtils.jsonToPojo(itemJson, TbItem.class);
		return itemService.importItem(item, categoryName, desc);
	}

	private String toCharsetUTF(String str) {
		try {
			return new String(str.getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
}
