package com.shangpin.spider.common;

public class SymbolConstants {
	
	/**
	 * 链接截取符
	 */
	public static final String REFER_MARK = "-";
	
	/**
	 * 问号
	 */
	public static final String QUESTION_MARK = "?";
	/**
	 * 双斜杠
	 */
	public static final String DOUBLE_SLASH = "//";
	/**
	 * 单斜杠
	 */
	public static final String SINGLE_SLASH = "/";
	/**
	 * href属性
	 */
	public static final String HREF = "href";
	/**
	 * href属性，绝对路径
	 */
	public static final String ABS_HREF = "abs:href";
	/**
	 * src属性
	 */
	public static final String SRC = "src";
	/**
	 * src属性,绝对路径
	 */
	public static final String ABS_SRC = "abs:src";
	/**
	 * 策略C中规则属性的标识
	 */
	public static final String ATTR_FLAG = "@|";
	/**
	 * 抓取最终值的间隔符(多种策略或的间隔符)
	 */
	public static final String SPLIT_FLAG= "|";
	/**
	 * 多种策略与的间隔符
	 */
	public static final String AND_FLAG= "&";
	/**
	 * 多种规则间的间隔符
	 */
	public static final String RULE_SPLIT_FLAG= "@,";
	/**
	 * 左括号
	 */
	public static final String LEFT_PART = "{";
	/**
	 * 右括号
	 */
	public static final String RIGHT_PART = "}";
	/**
	 * 截取规则间的分隔符
	 */
	public static final String SUB_SPLIT_FLAG = "@;";
	/**
	 * 截取规则间的前缀
	 */
	public static final String SUB_SUFIX = "#T";
	/**
	 * 截取规则第一个的多种匹配标识
	 */
	public static final String SUB_FIRSTOR = "#OR";
	/**
	 * 截取规则第二个的默认匹配标识
	 */
	public static final String SUB_LENGTH = "length";
	/**
	 * QTY规则中的标识符
	 */
	public static final String QTY_FLAG = "@F";
	/**
	 * 非的标识符
	 */
	public static final String FALSE_MARK = "!";
	
}
