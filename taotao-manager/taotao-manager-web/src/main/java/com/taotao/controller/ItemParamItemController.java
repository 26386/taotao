package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.service.ItemParamItemService;

/**
 * 展示商品规格参数
 * @author wlnner
 *
 */
@Controller
public class ItemParamItemController {

	@Autowired
	private ItemParamItemService itemParamItemService;
	
	@RequestMapping("/showItem/{itemId}")
	public String showItemParamItem(@PathVariable Long itemId,Model model){
		String viewStr = itemParamItemService.getItemParamItemByItemId(itemId);
		model.addAttribute("vs", viewStr);
		return "item";
	}
}
