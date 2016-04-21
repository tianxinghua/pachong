package com.shangpin.iog.pavinGroup.util.test;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.shangpin.iog.pavinGroup.util.HttpResponse;
import com.shangpin.iog.pavinGroup.util.HttpUtils;

public class Test {

	public static void main(String[] args) {
		getCategoryUrl();
	}
	
	
	private static List getUrlList() {
		
		List<String> list = new ArrayList();
		list.add("/collections/women");		
		list.add("/collections/shoes");	
		list.add("/collections/shoes?page=2");
		list.add("/collections/shoes?page=3");
		list.add("/collections/shoes?page=4");
		list.add("/collections/shoes?page=5");
		list.add("/collections/shoes?page=6");
		list.add("/collections/shoes?page=7");
		list.add("/collections/shoes?page=8");
		list.add("/collections/shoes?page=9");
		
		list.add("/collections/bags");	
		for(int i=2;i<=12;i++){
			list.add("/collections/bags?page="+i);
		}
		
		list.add("/collections/accessories");	
		list.add("/collections/accessories?page=2");
		list.add("/collections/accessories?page=3");
		list.add("/collections/accessories?page=4");
		
		
		list.add("/collections/men");	
		list.add("/collections/men?page=2");
		list.add("/collections/men?page=3");
		list.add("/collections/men?page=4");
		list.add("/collections/men?page=5");	
		
		
		
		list.add("/collections/lifestyle");
		list.add("/collections/lifestyle?page=2");
		list.add("/collections/lifestyle?page=3");
		list.add("/collections/lifestyle?page=4");
		
		
		List<String> picUrllist = new ArrayList();
		
		for(String url :list){
			try {
				HttpResponse response = HttpUtils.get("http://www.smets.lu"+url);
				if (response.getStatus()==200) {
					String htmlContent = response.getResponse();
					Document doc = Jsoup.parse(htmlContent);
					Elements categorys = doc.select(".table").select("li");
					for (Element category : categorys) {
						String picUrl = category.select("a").attr("href");
						String name = category.select("a").text();
						if("/collections/women/products/ruffle-knitwear".equals(picUrl)){
							System.out.println("s");
						}
						System.out.println(name+"|"+picUrl);
						picUrllist.add(picUrl);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return picUrllist;
	}
	
	

	private static void getCategoryUrl() {
		StringBuffer s = new StringBuffer();
		List<String> list = getUrlList();
		for(String url :list){
			try {
				HttpResponse response = HttpUtils.get("http://www.smets.lu"+url);
				if (response.getStatus()==200) {
					String htmlContent = response.getResponse();
					Document doc = Jsoup.parse(htmlContent);
					Elements categorys = doc.select("#rg-gallery").select(".es-carousel").get(0).select("li");
					for (Element category : categorys) {
						s.append("http:"+ category.select("img").attr("data-large")).append(";");
					}
				}
//				readLine(s.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		readLine(s.toString());
	}
	
	 private static void readLine(String content){
	    	File file = new File("C://picUrl.txt");
	    	FileWriter fwriter = null;
	    	   try {
	    	    fwriter = new FileWriter(file);
	    	    fwriter.write(content);
	    	   } catch (Exception ex) {
	    	    ex.printStackTrace();
	    	   } finally {
	    	    try {
	    	     fwriter.flush();
	    	     fwriter.close();
	    	    } catch (Exception ex) {
	    	     ex.printStackTrace();
	    	    }
	    	   }
	    }
	
}
