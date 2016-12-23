package com.shangpin.ephub.client.data.mysql.enumeration;

/**
 * 任务表状态
 * @author zhaogenchun
 * @date 2016年12月23日 下午8:01:08
 */
public enum TaskState {

	/**
	 * 待处理
	 */
	NO_HANDLE(0,"NO_HANDLE"),
	/**
	 * 正在处理
	 */
	HANDLEING(1,"HANDLEING"),
	/**
	 * 部分成功
	 */
	SOME_SUCCESS(2,"SOME_SUCCESS"),
	/**
	 * 全部成功
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
    
    TaskState(Integer index,String description){
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
