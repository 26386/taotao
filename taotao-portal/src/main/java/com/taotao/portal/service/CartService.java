package com.taotao.portal.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.portal.pojo.CartItem;

public interface CartService {

	TaotaoResult addCartItem(Long itemId,Integer num,HttpServletRequest request,HttpServletResponse response);
	List<CartItem> getCartItemList(HttpServletRequest request,HttpServletResponse response);
	TaotaoResult updateCartNum(HttpServletRequest request,HttpServletResponse response,Long itemId,Integer num);
}
