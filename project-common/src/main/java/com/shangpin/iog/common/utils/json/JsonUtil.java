package com.shangpin.iog.common.utils.json;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {
	/**
	 * 
	 * 将java对象转换成json字符串
	 * 
	 * @param javaObj
	 * 
	 * @return
	 */

	public static String getJsonString4JavaPOJO(Object javaObj) {

		JSONObject json;

		json = JSONObject.fromObject(javaObj);

		return json.toString();

	}

	/**
	 * 
	 * 将java对象转换成json字符串，并设定日期格式
	 * 
	 * @param javaObj
	 * 
	 * @param dataFormat
	 * 
	 * @return
	 */

	public static String getJsonString4JavaPOJO(Object javaObj,
			String dataFormat) {

		JSONObject json;

		JsonConfig jsonConfig = configJson(dataFormat);

		json = JSONObject.fromObject(javaObj, jsonConfig);

		return json.toString();

	}

	/**
	 * 
	 * 将java对象转换成json字符串，并设定日期格式
	 * 
	 * @param javaObj
	 * 
	 * @param dataFormat
	 * 
	 * @return
	 */

	public static String getJsonArrayString4JavaPOJO(Object javaObj,
			String dataFormat) {

		JSONArray json;

		JsonConfig jsonConfig = configJson("yyyy-MM-dd HH:mm:ss");

		json = JSONArray.fromObject(javaObj, jsonConfig);

		return json.toString();

	}

	public static JSONObject objectcollect2json(List<?> list, int total) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows", list);
		return JSONObject.fromObject(map);
	}

	/**
	 * 
	 * @param args
	 */

	public static void main(String args) {

		System.out.println("JsonUtil="
				+ JsonUtil.getJsonArrayString4JavaPOJO("2010-01-01 11:11:11",
						"yyyy-mm-dd"));
		System.out.println("JsonUtil="
				+ JsonUtil.getJsonArrayString4JavaPOJO("2010-01-01 11:11:11",
						"yyyy-mm-dd hh:mm-ss"));

	}

	/**
	 * 
	 * JSON 时间解析器具
	 * 
	 * @param datePattern
	 * 
	 * @return
	 */

	public static JsonConfig configJson(String datePattern) {

		JsonConfig jsonConfig = new JsonConfig();

		jsonConfig.setExcludes(new String[] { "" });

		jsonConfig.setIgnoreDefaultExcludes(false);

		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor(datePattern));

		return jsonConfig;

	}

	public static String getJsonArrayString4JavaPOJOSimpleFormat(
			Object javaObj, String dataFormat) {

		JSONArray json;

		JsonConfig jsonConfig = configJson("yyyy-MM-dd");

		json = JSONArray.fromObject(javaObj, jsonConfig);

		return json.toString();

	}

	/**
	 * 
	 * 
	 * 
	 * @param excludes
	 * 
	 * @param datePattern
	 * 
	 * @return
	 */

	public static JsonConfig configJson(String[] excludes,

	String datePattern) {

		JsonConfig jsonConfig = new JsonConfig();

		jsonConfig.setExcludes(excludes);

		jsonConfig.setIgnoreDefaultExcludes(false);

		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

		jsonConfig.registerJsonValueProcessor(Date.class,

		new DateJsonValueProcessor(datePattern));

		return jsonConfig;

	}

	/**
	 * 
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description: 从一个JSON 对象字符格式中得到一个java对象
	 * </p>
	 * 
	 * @author Robin
	 * @param jsonString
	 * @param pojoCalss
	 * @return
	 */
	public static Object getObject4JsonString(String jsonString, Class<?> pojoCalss) {
		Object pojo = null;
		try {
			JSONObject jsonObject = JSONObject.fromObject(jsonString);
			pojo = JSONObject.toBean(jsonObject, pojoCalss);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pojo;
	}
}
