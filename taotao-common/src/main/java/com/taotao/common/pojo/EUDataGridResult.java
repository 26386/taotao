package com.taotao.common.pojo;

import java.util.List;

//EasyUI datagrid 能够接收的数据格式
public class EUDataGridResult {

	//查询到的数据总条数
	private long total;
	//当前页面经过pagehelper处理后的集合:Page{pageNum=2, pageSize=30, startRow=30, endRow=60, total=3098, pages=104}
	private List<?> rows;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
}
