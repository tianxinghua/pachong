package com.shangpin.spider.quartz;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/** 
 * @author  njt 
 * @date 创建时间：2018年1月3日 上午10:26:12 
 * @version 1.0 
 * @parameter  
 */

public class SerializableUtil {
	/**
	 * 把序列化的对象转成对应类
	 * @return 
	 */
	public static Object objToClass(Object object, Object obj){
		byte[] bytes = toByteArray(object);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois;
		try {
		ois = new ObjectInputStream(bais);
		bais.close();
		obj = ois.readObject();
		} catch (Exception e) {
		e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 对象转数组
	 * @return 
	 */
	private static byte[] toByteArray(Object obj){
		byte[] bytes = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
			outputStream.writeObject(obj);
			outputStream.flush();
			bytes = byteArrayOutputStream.toByteArray();
			outputStream.close();
			byteArrayOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}
}
