package com.shangpin.iog.util;


import com.alibaba.fastjson.JSON;

/**
 * <p>Title:JsonUtil.java </p>
 * <p>Description: json序列化和反序列化工具类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月16日 上午11:16:11
 */
public final class JsonUtil {
	
	/**
	 * 将一个对象序列化为一个json字符串
	 * @param object 需要序列化的对象
	 * @return 序列化之后的json字符串
	 */
	public static String serialize(Object object){
		return JSON.toJSONString(object);
	}
	/**
	 * @param json 需要反序列化的字符串
	 * @param clazz 反序列化的java类型
	 * @return 对象
	 */
	public static <T> T deserialize(String json, Class<T> clazz){
		return JSON.parseObject(json, clazz);
	}
}
