package com.shangpin.iog.rosi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	 
	

}
