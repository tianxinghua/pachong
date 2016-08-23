package com.shangpin.iog.optical.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

public class Utils {
	public static void main(String[] args) throws Exception {
		
	}
	public static List<List<String>>  getListProduct(String str) {
		
		List<List<String>> allList = new ArrayList<List<String>>();
		List<Element> list = null;
		try {
			list = getList(str);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		int j=0;
		for(Element element:list){
			System.out.println(element.getName());
			List<Element> list1 = element.elements();
			if(j==0){
				j+=1;
				continue;
			}else{
				List<String> ll = new ArrayList<String>();
				for(Element element1:list1){
					if(element1.getName().equals("Cell")){
						if(element1.attribute("Index")!=null){
							int index = Integer.parseInt(element1.attribute("Index").getText())-1;
							int l = ll.size();
							for(int i=0;i<index-l;i++){
								ll.add("");
							}
						}
					}
					List<Element> list2 = element1.elements(); 
					for(Element list3:list2){
						if("Data".equals(list3.getName())){
							ll.add(list3.getText());
							System.out.println(list3.getText());
							break;
						}
					}
				}
				if(ll.size()==19){
					ll.add("");
				}
				allList.add(ll);
			}
		}
		return allList;
	}
	public static List<Element>  getList(String str) throws DocumentException{
		
		List<Element> allList = new ArrayList<Element>();
		SAXReader reader = new SAXReader();
		InputStream in_withcode =null;
		try {
			in_withcode = new   ByteArrayInputStream(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		Document document =reader.read(in_withcode);
		Element root = document.getRootElement();
	      //把解析路径切换到/beans下  
	    Node node=document.selectSingleNode("/Workbook");  
	    Map<String,String> nsMap=new HashMap<String,String>();  
	      nsMap.put("Workbook","urn:schemas-microsoft-com:office:spreadsheet");  
	      XPath xpath=document.createXPath("Workbook:Worksheet");  
	      xpath.setNamespaceURIs(nsMap);  
	      List<Element> list=xpath.selectNodes(node);  
		for(Element element:list){
			System.out.println(element.getName());
			List<Element> ss = element.elements();
			for(Element sa:ss){
				System.out.println(sa.getName());
				if("Table".equals(sa.getName())){
					List<Element> sss = sa.elements();
					for(Element ssss:sss){
						System.out.println(ssss.getName());
						if("Row".equals(ssss.getName())){
							allList.add(ssss);
						}
					}
				}
			}
		}
		 return allList;
	}
}