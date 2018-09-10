package com.shangpin.spider.entity.gather;

import java.io.Serializable;

/**
 * @author njt
 * @date 创建时间：2017年12月15日 上午10:26:18
 * @version 1.0
 * @parameter
 */

public class RedisCache implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 缓存uuid标识
	 */
	private String uuid;
	/**
	 * 网站名称
	 */
	private String webName;
	/**
	 * 缓存中未处理链接的数量
	 */
	private Integer taskCount;
	/**
	 * 缓存中已处理链接的数量
	 */
	private Integer remTaskCount;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getWebName() {
		return webName;
	}

	public void setWebName(String webName) {
		this.webName = webName;
	}

	public Integer getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(Integer taskCount) {
		this.taskCount = taskCount;
	}

	public Integer getRemTaskCount() {
		return remTaskCount;
	}

	public void setRemTaskCount(Integer remTaskCount) {
		this.remTaskCount = remTaskCount;
	}

}
