package com.shangpin.framework.page;


/**
 * 一些线程变量
 * 分页变量:page 1开始,pageSize非负数从1开始
 * @description 
 * @author 陈小峰
 * <br/>2015年2月4日
 */
public class SystemContext {

	private static ThreadLocal<Integer> pageSize = new ThreadLocal<Integer>();
	private static ThreadLocal<Integer> page = new ThreadLocal<Integer>();
	/**
	 * 设置页码大小
	 * @param _pagesize
	 */
	public static void setPageSize(int _pagesize){
		pageSize.set(_pagesize);
	}
	/**
	 * 从1开始
	 * @return
	 */
	public static int getPageSize(){
		Integer ps = (Integer)pageSize.get();
		if(ps == null || ps<1){
			return Integer.MAX_VALUE;
		}
		return ps;
	}
	
	public static void removePageSize(){
		pageSize.remove();
	}
	/**
	 * 从1开始
	 * @return
	 */
	public static int getPage() {
		Integer pg = (page.get()==null||page.get()<1)?1:page.get();
		page.set(pg);
		return pg;
	}
	/**
	 * 设置分页从1开始
	 * @param _page
	 */
	public static void setPage(int _page) {
		page.set(_page);
	}
	
}
