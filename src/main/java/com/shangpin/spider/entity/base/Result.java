package com.shangpin.spider.entity.base;

import java.io.Serializable;
import java.util.List;

/** 
 * @author  njt 
 * @date 创建时间：2017年12月15日 下午2:36:02 
 * @version 1.0 
 * @parameter  
 */

public class Result<T> extends StatusEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 总条数
	 */
	private Long totalCount;
	/**
	 * 数据集合
	 */
	private List<T> dataList;
	/**
	 * 总页数
	 */
    private Long totalPages;
	/**
	 * 当前页码
	 */
	private Integer currentPage;

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Long totalPages) {
		this.totalPages = totalPages;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

}
