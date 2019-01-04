package com.shangpin.iog.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

public class XMLUtil {
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	 public static List<org.jdom2.Element> getProductElement(File file,String cname){
		 
		 	List<Element> retList = new ArrayList<Element>();
	        List<Element> allChildren = new ArrayList();
	        try {
	            SAXBuilder builder = new SAXBuilder();
	            org.jdom2.Document doc = builder.build(file);
	            org.jdom2.Element foo =doc.getRootElement();
	            allChildren = foo.getChildren();
	            for(Element element : allChildren){
//	            	if(element.getName().equals(cname)){
//	            		retList.add(element);
//	            	}
	            	retList.addAll(element.getChildren(cname));
	            }
	            } catch (Exception e) {
	            e.printStackTrace();
	            }
	        return retList;
	    }
	 
	 public static <T> T gsonXml2Obj(Class<T> clazz, String xml){
		 XmlParserCreator parserCreator = new XmlParserCreator() {
             @Override
             public XmlPullParser createParser() {
                 try {
                     return XmlPullParserFactory.newInstance().newPullParser();
                 } catch (Exception e) {
                     throw new RuntimeException(e);
                 }
             }
         };

         GsonXml gsonXml = new GsonXmlBuilder()
                 .setSameNameLists(true)
                 .setXmlParserCreator(parserCreator)
                 .create();

         return gsonXml.fromXml(xml, clazz); 
	 }
	 
	 public static Integer random(){
		 Random rand = new Random();
		 int i = rand.nextInt(); //int范围类的随机数
//		 i = rand.nextInt(1000); //生成0-1000以内的随机数

		 i = (int)(Math.random() * 1000); //0-1000以内的随机数，用Matn.random()方式
		 return i;
	 } 


}
