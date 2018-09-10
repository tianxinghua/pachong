package com.shangpin.spider.entity.quartz;

import java.io.Serializable;

/** 
 * @author  njt 
 * @date 创建时间：2017年12月15日 下午3:53:42 
 * @version 1.0 
 * @parameter  
 */

public class QuartzInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 任务名称
	 */
	private String name;
	/**
	 * 上次启动时间
	 */
	private String previousTime;
	/**
	 * 下次启动时间
	 */
	private String nextTime;
	/**
	 * 开始时间
	 */
	private String startTime;
	/**
	 * 状态
	 */
	private String state;
	/**
	 * cron表达式
	 */
	private String cronExpression;
	/**
	 * 时区
	 */
	private String timeZone;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPreviousTime() {
		return previousTime;
	}
	public void setPreviousTime(String previousTime) {
		this.previousTime = previousTime;
	}
	public String getNextTime() {
		return nextTime;
	}
	public void setNextTime(String nextTime) {
		this.nextTime = nextTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	
}
