package com.shangpin.iog.tizianafausti.utils.queue;
/**
 * 抽象任务类
 * @author wangjiyue
 *
 */
public abstract interface ITask {
	/**
	 * 统一任务开始入口
	 */
	public abstract void doTask(long startTime);
}
