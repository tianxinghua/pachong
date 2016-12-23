package com.shangpin.ephub.product.business.ui.task.common.enumeration;

public enum TaskStatus {

	/**
	 * 图片状态
	 */
	NO_HANDLE(0,"NO_HANDLE"),
	/**
	 * 货号状态
	 */
	HANDLEING(1,"HANDLEING"),
	/**
	 * 品类状态
	 */
	SOME_SUCCESS(2,"SOME_SUCCESS"),
	/**
	 * 品牌状态
	 */
	ALL_SUCCESS(3,"ALL_SUCCESS");
	
	/**
     * 数字索引标识
     */
    private int index;
    /**
     * 描述信息
     */
    private String description;
    
    TaskStatus(Integer index,String description){
		this.index = index;
		this.description = description;
	}
    public int getIndex() {
        return index;
    }
    public void setIndex(Integer index) {
        this.index = index;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
