package com.shangpin.iog.productweb.schedule;
/**
 * 任务状态枚举类.
 * 0、未执行
 * 1、锁定或处理中LOCK 
 * 2、完成COMPLETE
 * 3、异常
 */
public enum TaskState {
	INIT(0),
	LOCK(1),
	COMPLETE(2),
	EXCEPTION(3);
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
