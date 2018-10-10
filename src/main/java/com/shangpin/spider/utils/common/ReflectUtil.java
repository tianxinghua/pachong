package com.shangpin.spider.utils.common;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.spider.common.Constants;
import com.shangpin.spider.config.FieldNotes;
import com.shangpin.spider.config.FilterNotes;
import com.shangpin.spider.entity.gather.SpiderRules;

/**
 * 
 * @author njt
 * @date 2018年9月25日
 * @desc 反射获取类信息的工具类 ReflectUtil
 */
public class ReflectUtil {
	/**
	 * 字段规则的标识
	 */
	private static final Integer FIELD_RULES_FLAG = 2;
	
	/**
	 * 根据过滤注解获取类型需过滤的字段
	 * 
	 * @return
	 */
	public static Set<String> getAllFilter(String className) {
		Set<String> set = new HashSet<String>();
		try {
			Class<?> class1 = Class.forName(className);
			Field[] fields = class1.getDeclaredFields();
			for (Field field : fields) {
				if (!field.isAnnotationPresent(FilterNotes.class)) {
					continue;
				}
				String fieldName = field.getName();
				set.add(fieldName);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return set;
	}
	
	/**
	 * 根据注解获取类型所有字段的信息
	 * 
	 * @return
	 */
	public static JSONArray getAllField(String className) {
		JSONArray array = new JSONArray();
		try {
			Class<?> class1 = Class.forName(className);
			Field[] fields = class1.getDeclaredFields();
			for (Field field : fields) {
				if (!field.isAnnotationPresent(FieldNotes.class)) {
					continue;
				}
				String fieldNoteName = field.getAnnotation(FieldNotes.class).name();
				if ("".equals(fieldNoteName)) {
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

	/**
	 * 获取需要点击的字段规则
	 * 
	 * @param needClickFieldAry
	 * @param spiderRuleInfo
	 * @return
	 */
	public static Map<String, Map<String, String>> getClickFieldMap(SpiderRules spiderRuleInfo) {
		try {
			Class<?> ruleClass = Class.forName(SpiderRules.class.getName());
			Field[] ruleFields = ruleClass.getDeclaredFields();
			Map<String, Map<String, String>> clickFieldRulesMap = new ConcurrentHashMap<String, Map<String, String>>();
			String[] needClickFieldAry = spiderRuleInfo.getNeedClickFieldAry();
			for (String clickfield : needClickFieldAry) {
				int i = 0;
				for (Field ruleField : ruleFields) {
					if(i==FIELD_RULES_FLAG) {
						break;
					}
					ruleField.setAccessible(true);
					String ruleFieldName = ruleField.getName();
					if (ruleFieldName.contains(clickfield) && ruleFieldName.contains(Constants.FIELD_RULES_SUFFIX)) {
						String rulesStr = (String) ruleField.get(spiderRuleInfo);
						if (clickFieldRulesMap.containsKey(clickfield)) {
							Map<String, String> map = clickFieldRulesMap.get(clickfield);
							map.put("rulesStr", rulesStr);
							clickFieldRulesMap.put(clickfield, map);
						} else {
							Map<String, String> map = new ConcurrentHashMap<String, String>();
							map.put("rulesStr", rulesStr);
							clickFieldRulesMap.put(clickfield, map);
						}
						i++;
					}
					if (ruleFieldName.contains(clickfield) && ruleFieldName.contains(Constants.FIELD_STRATEGY_SUFFIX)) {
						String strategyStr = (String) ruleField.get(spiderRuleInfo);
						if (clickFieldRulesMap.containsKey(clickfield)) {
							Map<String, String> map = clickFieldRulesMap.get(clickfield);
							map.put("strategyStr", strategyStr);
							clickFieldRulesMap.put(clickfield, map);
						} else {
							Map<String, String> map = new ConcurrentHashMap<String, String>();
							map.put("strategyStr", strategyStr);
							clickFieldRulesMap.put(clickfield, map);
						}
						i++;
					}
				}
			}
			return clickFieldRulesMap;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}
