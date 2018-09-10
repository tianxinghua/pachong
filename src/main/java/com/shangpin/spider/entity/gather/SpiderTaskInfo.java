package com.shangpin.spider.entity.gather;

import java.io.Serializable;
import java.util.Date;

/** 
 * @author  njt 
 * @date 创建时间：2017年12月15日 下午4:16:34 
 * @version 1.0 
 * @parameter  
 */

public class SpiderTaskInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * uuid标识
	 */
	private String id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 处理数量
	 */
	private Long count;
	/**
	 * 状态序数
	 */
	private Integer ordinal;
	/**
	 * 状态
	 */
	private String state;
	/**
	 * 开始时间
	 */
	private Date startTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Integer getOrdinal() {
		return ordinal;
	}
	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}
	
}
