package com.shangpin.iog.common.utils.httpclient;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public  class ObjectXMLUtil<T> {

	//private static final String DECLARATION = "<?xml version='1.0' encoding='utf-8'?>";  
	private static final String ENCODING = "GB18030"; 
	
	/**
	 * 将xml输入流转成object
	 * @param clazz object类型
	 * @param xml xml输入流
	 * @return clazz对应的实例
	 * @throws JAXBException
	 */
    @SuppressWarnings("unchecked")
	public static <T> T xml2Obj(Class<T> clazz, InputStream xml) throws JAXBException {
        Unmarshaller un = createUnMarshaller(clazz);
        return (T) un.unmarshal(xml);
    }

    /**
     *
     * 根据xml文件内容转成object
	 * @param clazz object类型
	 * @param file xml文件
	 * @return clazz对应的实例
     * @throws JAXBException
     * @throws FileNotFoundException
     */
    public static <T> T xml2Obj(Class<T> clazz, File file) throws JAXBException, FileNotFoundException {
        return xml2Obj(clazz,new FileInputStream(file));
    }
    /**
	 * 将xml字符串转成object
	 * @param clazz object类型
	 * @param xml xml字符串
	 * @return clazz对应的实例
	 * @throws JAXBException
	 */
    @SuppressWarnings("unchecked")
	public static <T> T xml2Obj(Class<T> clazz, String xml) throws JAXBException {
    	Unmarshaller un = createUnMarshaller(clazz);
    	return (T) un.unmarshal(new StringReader(xml));
    }
    /**
     * 将object转为xml，并将信息保存到os流
     * @param obj 待转为xml的对象
     * @param os 转后输出对象
     * @throws JAXBException
     */
    public static void obj2Xml(Object obj,OutputStream os) throws JAXBException{
        Marshaller m = createMarshaller(obj);
        m.marshal(obj, os);
    }
    /**
     * 将object转为xml，并将信息保存到文件中
     * @param obj 待转为xml的对象
     * @param output 转后输出文件
     * @throws FileNotFoundException
     * @throws JAXBException
     */
    public static void obj2Xml(Object obj, File output) throws FileNotFoundException, JAXBException{
    	obj2Xml(obj,new FileOutputStream(output));
    }
    /**
     * 将object转为xml，并返回xml字符串
     * @param obj 待转为xml的对象
     * @return xml字符串
     * @throws FileNotFoundException
     * @throws JAXBException
     */
    public static String obj2Xml(Object obj) {
    	Marshaller m;
    	String output = "";
		try {
			m = createMarshaller(obj);
			Writer w = new StringWriter();
			m.marshal(obj, w);
			output = w.toString();
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}
        return output;
    }
	/**
	 * object 2 xml
	 * @param obj
	 * @return
	 * @throws JAXBException
	 */
    private static Marshaller createMarshaller(Object obj) throws JAXBException{
    	JAXBContext jc = JAXBContext.newInstance(obj.getClass());
    	Marshaller m = jc.createMarshaller();
    	//设置编码方式
 		m.setProperty(Marshaller.JAXB_ENCODING, ENCODING);
 		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
    	return  m;
    }
    /**
     * xml 2 object
     * @param clazz
     * @return
     * @throws JAXBException
     */
    private static Unmarshaller createUnMarshaller(@SuppressWarnings("rawtypes") Class clazz) throws JAXBException {
    	JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller un = context.createUnmarshaller();
		//设置编码方式 
		//un.setProperty(Marshaller.JAXB_ENCODING, "utf-8"); 
		//un.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); 
		return un;
	}  
}
