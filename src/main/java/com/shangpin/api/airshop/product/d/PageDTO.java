package com.shangpin.api.airshop.product.d;

import java.util.List;

public class PageDTO {
	
	private List<ProductDTO> list;
	
	private int count;
	//每页个数
	private int pageSize;
	//当前页,1开始
	private int pageNow ;
	
	private String url;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<ProductDTO> getList() {
		return list;
	}
	public void setList(List<ProductDTO> list) {
		this.list = list;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNow() {
		return pageNow;
	}
	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
	}

}
