package com.shangpin.iog.theclutcher.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.gson.JsonSyntaxException;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

public class XMLUtil {
	private static LoggerUtil error = LoggerUtil.getLogger("error");
	private static Logger logger = Logger.getLogger("info");
	
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
		 XmlParserCreator parserCreator = new XmlParserCreator(){
             @Override
             public XmlPullParser createParser() {
                 try {
                     return XmlPullParserFactory.newInstance().newPullParser();
                 } catch (Exception e) {
                	 error.error(e);
                	 return null;
                 }
             }
         };
         
         logger.info("创建parserCreator成功===="+parserCreator); 
    	 try {
			
    		 GsonXml gsonXml = new GsonXmlBuilder()
             .setSameNameLists(true)
             .setXmlParserCreator(parserCreator)
             .create();
    		 logger.info("创建gsonXml成功===="+gsonXml); 
        	 return gsonXml.fromXml(xml, clazz); 
    		 
		} catch (Exception e) {
			error.error("解析xml出错======="+e.getMessage()); 
		}
    	 
    	return null;
        
	 }


}
