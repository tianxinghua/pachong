package com.shangpin.asynchronous.task.consumer.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * <p>Title: ReflectBeanUtils</p>
 * <p>Description: 对象和Map之间转换工具 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年3月23日 上午10:50:38
 *
 */
public class ReflectBeanUtils {

	/**
	 * Map转换为Object
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	public static Object mapToObject(Map<String, String> map, Class<?> beanClass) throws Exception {
		if (map == null || map.size() <= 0)
			return null;
		Object obj = beanClass.newInstance();
		// 获取关联的所有类，本类以及所有父类
		boolean ret = true;
		Class<?> oo = obj.getClass();
		List<Class<?>> clazzs = new ArrayList<Class<?>>();
		while (ret) {
			clazzs.add(oo);
			oo = oo.getSuperclass();
			if (oo == null || oo == Object.class)
				break;
		}

		for (int i = 0; i < clazzs.size(); i++) {
			Field[] fields = clazzs.get(i).getDeclaredFields();
			for (Field field : fields) {
				int mod = field.getModifiers();
				if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
					continue;
				}
				// 由字符串转换回对象对应的类型
				if (field != null) {
					field.setAccessible(true);
					field.set(obj, map.get(field.getName()));
				}
			}
		}
		return obj;
	}

	/**
	 * Object转换Map
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> objectToMap(Object obj) throws Exception {
		if (obj == null) {
			return null;
		}
		// 获取关联的所有类，本类以及所有父类
		boolean ret = true;
		Class<?> oo = obj.getClass();
		List<Class<?>> clazzs = new ArrayList<Class<?>>();
		while (ret) {
			clazzs.add(oo);
			oo = oo.getSuperclass();
			if (oo == null || oo == Object.class)
				break;
		}

		Map<String, String> map = new HashMap<String, String>();

		for (int i = 0; i < clazzs.size(); i++) {
			Field[] declaredFields = clazzs.get(i).getDeclaredFields();
			for (Field field : declaredFields) {
				int mod = field.getModifiers();
				// 过滤 static 和 final 类型
				if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
					continue;
				}
				field.setAccessible(true);
				map.put(field.getName(), field.get(obj).toString());
			}
		}

		return map;
	}
}
