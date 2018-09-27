package com.shangpin.spider.utils.common;

import java.lang.reflect.Field;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.spider.config.FieldNotes;

/**
 * 
 * @author njt
 * @date 2018年9月25日
 * @desc 反射获取类信息的工具类
 * ReflectUtil
 */
public class ReflectUtil {
	/**
	 * 获取类型所有字段的信息
	 * @return 
	 */
	public static JSONArray getAllField(String className) {
		JSONArray array = new JSONArray();
		try {
			Class<?> class1 = Class.forName(className);
			Field[] fields = class1.getDeclaredFields();
			for (Field field : fields) {
				if(!field.isAnnotationPresent(FieldNotes.class)) {
					continue;
				}
				String fieldNoteName = field.getAnnotation(FieldNotes.class).name();
				if("".equals(fieldNoteName)) {
					continue;
				}
				JSONObject obj = new JSONObject();
				String fieldName = field.getName();
				obj.put(fieldName, fieldNoteName);
				array.add(obj);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return array;
	}
	
	/*public static void main(String[] args) {
		JSONArray array = getAllField(CrawlResult.class.getName());
		System.out.println(array);
	}*/

}
