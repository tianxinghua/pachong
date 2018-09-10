package com.shangpin.spider.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author njt
 * @date 创建时间：2017年11月14日 下午3:18:24
 * @version 1.0
 * @parameter
 */

public class Constants {

	/**
	 * 角色map
	 */
	public static Map<String, String> map = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("super_admin", "超级管理员");
			put("admin", "管理员");
			put("vip", "普通会员");
		}
	};

	/**
	 * 无权限，金额显示$
	 */
	public static final String MONEY = "$$$$$$$$";
	
	/**
	 * 时间，年
	 */
	public static final String YEAR = "yyyy";
	/**
	 * 抓取任务完成删除后储存的键
	 */
	public static final String REMTASKUUID = "REMTASKUUID";

	/**
	 * 抓取链接储存的键
	 */
	public static final String TASKUUID = "TASKUUID";

	/**
	 * 抓取网页无图片的前台标识
	 */
	public static final String NONEIMG = "无";

	/**
	 * 状态描述
	 */
	public static final String SUCCESS = "success";

	public static final String FAIL = "fail";

	public static final String EXIST = "exist";

	/**
	 * quartz的相关信息
	 */
	public static final String QUARTZ_JOB_GROUP_NAME = "webpage-SPspider-job";

	public static final String QUARTZ_TRIGGER_GROUP_NAME = "webpage-SPspider-trigger";

	public static final String QUARTZ_TRIGGER_NAME_SUFFIX = "-cron";

	/**
	 * quartz运行的状态
	 */
	public static Map<String, String> stateMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("NONE", "<span class='btn btn-info'>无</span>");
			put("NORMAL", "<span class='btn btn-primary'>正常运行</span>");
			put("PAUSED", "<span class='btn btn-default'>暂停</span>");
			put("COMPLETE", "<span class='btn btn-default'>结束</span>");
			put("ERROR", "<span class='btn btn-danger'>错误</span>");
			put("BLOCKED", "<span class='btn btn-danger'>抓取中</span>");
		}
	};

	/**
	 * 爬虫task运行的状态
	 */
	public static Map<Integer, String> taskStateMap = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(0, "<span class='btn btn-info'>初始化</span>");
			put(1, "<span class='btn btn-primary'>正常运行</span>");
			put(2, "<span class='btn btn-default'>结束</span>");
		}
	};

	/**
	 * 抓取结果map
	 */
	public static final String RESULTMAP = "resultMap";

	/**
	 * 编辑按钮参数
	 */
	public static final String ADD = "add";
	public static final String EDIT = "edit";
	public static final String DEL = "del";
	
	/**
	 * phantomjs进程名称
	 */
	public static final String PHANTOMJS = "phantomjs.exe";
	
	/**
	 * 状态码
	 */
	public static final String SUCCESSCODE = "200";
	public static final String ERRORCODE = "500";
	public static final String NOCODE = "404";
}
