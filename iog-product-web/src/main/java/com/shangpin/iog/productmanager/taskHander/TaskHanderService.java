package com.shangpin.iog.productmanager.taskHander;

import com.shangpin.iog.productmanager.Task;





/**
 * 任务接口 
 * @author lizhongren
 *
 */
public interface TaskHanderService {
	
	/**
	 * 执行任务
	 */
	public void executeTask(Task task);

}
