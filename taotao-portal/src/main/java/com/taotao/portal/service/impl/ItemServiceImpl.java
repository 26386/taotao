package com.taotao.portal.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.portal.pojo.ItemInfo;
import com.taotao.portal.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${ITEM_INFO_URL}")
	private String ITEM_INFO_URL;
	@Value("${ITEM_DESC_URL}")
	private String ITEM_DESC_URL;
	@Value("${ITEM_PARAM_URL}")
	private String ITEM_PARAM_URL;

	// 获取商品的基本信息
	public ItemInfo getItemById(Long itemId) {
		String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_INFO_URL + itemId);
		if (StringUtils.isNotBlank(json)) {
			TaotaoResult result = TaotaoResult.formatToPojo(json, ItemInfo.class);
			if (result.getStatus() == 200) {
				return (ItemInfo) result.getData();
			}
		}
		return null;
	}

	// 获取商品的详细信息
	public String getItemDescById(Long itemId) {
		String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_DESC_URL + itemId);
		if (StringUtils.isNoneBlank(json)) {
			TaotaoResult result = TaotaoResult.formatToPojo(json, TbItemDesc.class);
			if (result.getStatus() == 200) {
				TbItemDesc itemDesc = (TbItemDesc) result.getData();
				return itemDesc.getItemDesc();
			}
		}
		return null;
	}

	// 获取商品规格参数
	public String getItemParam(Long itemId) {
		String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_PARAM_URL + itemId);
		if (StringUtils.isNoneBlank(json)) {
			TaotaoResult result = TaotaoResult.formatToPojo(json, TbItemParamItem.class);
			if (result.getStatus() == 200) {
				TbItemParamItem itemParamItem = (TbItemParamItem) result.getData();
				List<Map> jsonList = JsonUtils.jsonToList(itemParamItem.getParamData(), Map.class);
				StringBuffer sb = new StringBuffer();
				sb.append("<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"1\" class=\"Ptable\">\n");
				sb.append("    <tbody>\n");
				for (Map m1 : jsonList) {
					sb.append("        <tr>\n");
					sb.append("            <th class=\"tdTitle\" colspan=\"2\">" + m1.get("group") + "</th>\n");
					sb.append("        </tr>\n");
					List<Map> list2 = (List<Map>) m1.get("params");
					for (Map m2 : list2) {
						sb.append("        <tr>\n");
						sb.append("            <td class=\"tdTitle\">" + m2.get("k") + "</td>\n");
						sb.append("            <td>" + m2.get("v") + "</td>\n");
						sb.append("        </tr>\n");
					}
				}
				sb.append("    </tbody>\n");
				sb.append("</table>");
				return sb.toString();
			}
		}
		return "";
	}
	
	@Test
	public void testName() throws Exception {
		Integer[] nums=new Integer[]{100,80,50,70,85,34,60};
		System.out.println(Arrays.toString(nums));
		//第一轮
		for(int j=1;j<nums.length;j++){
			for(int i=1;i<=nums.length-j;i++){
				if(nums[i]<nums[i-1]){
					int temp=nums[i];
					nums[i]=nums[i-1];
					nums[i-1]=temp;
				}
			}
		}
		System.out.println(Arrays.toString(nums));
	}
}
