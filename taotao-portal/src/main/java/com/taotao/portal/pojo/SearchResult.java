package com.taotao.portal.pojo;

import java.util.List;

public class SearchResult {

	//商品列表
	private List<Item> itemList;
	//查询到的总记录数
	private Long total;
	//总页数
	private Long totalPage;
	//当前页
	private Long currentPage;
	public List<Item> getItemList() {
		return itemList;
	}
	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Long getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Long totalPage) {
		this.totalPage = totalPage;
	}
	public Long getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Long currentPage) {
		this.currentPage = currentPage;
	}
	
}
