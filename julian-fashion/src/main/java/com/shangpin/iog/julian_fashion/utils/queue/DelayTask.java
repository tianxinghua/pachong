package com.shangpin.iog.julian_fashion.utils.queue;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
/**
 * 延迟任务，用于存入延迟队列
 * @author wangjiyue
 *
 */
public class DelayTask implements Delayed {
	ITask task;
	Date at;

	public DelayTask(ITask task, Date at) {
		this.task = task;
		this.at = at;
	}
	/**
	 * 获得延时时间
	 */
	public long getDelay(TimeUnit unit) {
		long n = getAt().getTime() - System.currentTimeMillis();
		return unit.convert(n, TimeUnit.MILLISECONDS);
	}
	@Override
	public int compareTo(Delayed o) {
		if (getAt().getTime() < ((DelayTask) o).getAt().getTime()) {
			return -1;
		}
		if (getAt().getTime() > ((DelayTask) o).getAt().getTime()) {
			return 1;
		}
		return 0;
	}

	public Date getAt() {
		return this.at;
	}

	public void setAt(Date at) {
		this.at = at;
	}

	public ITask getTask() {
		return this.task;
	}

	public void setTask(ITask task) {
		this.task = task;
	}
	@Override
	public String toString() {
		return "Scheduled: " + getAt();
	}
}