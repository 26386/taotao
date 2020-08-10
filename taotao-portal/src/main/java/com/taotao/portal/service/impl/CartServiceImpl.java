package com.taotao.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;

/**
 * 购物车业务逻辑
 * @author wlnner
 *
 */
@Service
public class CartServiceImpl implements CartService{

	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${ITEM_INFO_URL}")
	private String ITEM_INFO_URL;
	
	//添加商品到购物车
	public TaotaoResult addCartItem(Long itemId,Integer num,HttpServletRequest request,HttpServletResponse response){
		//查询购物车中是否含有此商品
		List<CartItem> cartItems = this.getCartItemList(request);
		for (CartItem cartItem : cartItems) {
			//注意:由于itemId是包装类型,所以必须用equals作比较
			if(cartItem.getId().equals(itemId)){
				cartItem.setNum(cartItem.getNum()+num);
				CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartItems), true);
				return TaotaoResult.ok();
			}
		}
		CartItem cartItem=new CartItem();
		String json = HttpClientUtil.doGet(REST_BASE_URL+ITEM_INFO_URL+itemId);
		TaotaoResult result = TaotaoResult.formatToPojo(json,TbItem.class);
		if(result.getStatus()==200){
			TbItem item=(TbItem)result.getData();
			cartItem.setId(item.getId());
			cartItem.setTitle(item.getTitle());
			cartItem.setPrice(item.getPrice());
			cartItem.setImage(item.getImage()==null?"":item.getImage().split(",")[0]);
			cartItem.setNum(num);
		}
		cartItems.add(cartItem);
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartItems), true);
		return TaotaoResult.ok();
	}
	
	/*public List<CartItem> updateCartNum(HttpServletRequest request,HttpServletResponse response,Long itemId,Integer num){
		List<CartItem> cartItems = this.getCartItemList(request);
		for (CartItem cartItem : cartItems) {
			//注意:由于itemId是包装类型,所以必须用equals作比较
			if(cartItem.getId().equals(itemId)){
				cartItem.setNum(num);
				CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartItems), true);
			}
		}
		return TaotaoResult.build(400, "未找到商品");
	}*/
	
	private List<CartItem> getCartItemList(HttpServletRequest request){
		//从cookie中获取商品列表
		String json = CookieUtils.getCookieValue(request, "TT_CART", true);
		//当用户第一次向购物车中添加商品的时候,直接返回空集合
		if(StringUtils.isBlank(json)){
			return new ArrayList<>();
		}
		List<CartItem> list = JsonUtils.jsonToList(json, CartItem.class);
		return list;
	}
	
	public List<CartItem> getCartItemList(HttpServletRequest request,HttpServletResponse response){
		return this.getCartItemList(request);
	}

	@Override
	public TaotaoResult updateCartNum(HttpServletRequest request, HttpServletResponse response, Long itemId,
			Integer num) {
		// TODO Auto-generated method stub
		return null;
	}
}
