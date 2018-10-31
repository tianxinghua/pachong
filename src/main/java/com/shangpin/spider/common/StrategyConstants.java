package com.shangpin.spider.common;

/**
 * 策略常量
 * 
 * @author njt
 * @date 2018年9月27日 上午11:17:12
 * @desc StrategyConstants
 */
public class StrategyConstants {
	
	/**
	 * 一层点击的策略标识
	 */
	public static final String ONE_CLICK = "ONE-CLICK";
	/**
	 * 两层点击的策略标识
	 */
	public static final String TWO_CLICK = "TWO-CLICK";
	/**
	 * 三层点击的策略标识
	 */
	public static final String THREE_CLICK = "THREE-CLICK";
	
	/**
	 * 对重复XPATH策略的处理(出发点针对图片路径)
	 */
	public static final String DE_X = "DE-X";
	
	/**
	 * 针对颜色和尺寸的处理
	 */
	public static final String DE_C = "DE-C";
	
	/**
	 * 对填写默认值策略的处理
	 */
	public static final String DE_S = "DE-S";
	
	/**
	 * 对库存量的标识
	 */
	public static final String SP_QTY_C = "SP-QTY-C";
	
	/**
	 * 对库存量的标识
	 */
	public static final String X = "X";
	
	/**
	 * 对库存量的标识
	 */
	public static final String C = "C";
	
	/**
	 * 对库存量的标识
	 */
	public static final String SUB = "SUB";
	
	/**
	 * 对库存量的标识
	 */
	public static final String RG = "RG";
	
	/**
	 * driver对img处理的标识
	 */
	public static final String SP_C = "SP-C";
	
	/**
	 * 有字符需拼接的标识
	 */
	public static final String UNION = "UNION";
	
	/**
	 * 下拉条的标识
	 */
	public static final String SCROLL = "scroll";
	
	/**
	 * 点击下一页
	 */
	public static final String NEXT = "next";
	
	/**
	 * 查看更多
	 */
	public static final String MORE = "more";
	
	/**
	 * 破解反爬虫的策略，关闭遮罩
	 */
	public static final String CLICK_X = "CLICK-X";
	
}