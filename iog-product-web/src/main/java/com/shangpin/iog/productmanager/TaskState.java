package com.shangpin.iog.productmanager;
/**
 * 任务状态枚举类.
 * 0、未执行
 * 1、锁定或处理中LOCK 
 * 2、完成COMPLETE
 * 3、异常
 */
public enum TaskState {
	STOP(0),
	START(1);
	private Integer value;

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
	TaskState(Integer value){
		this.value=value;
	}
	
}
