package com.shangpin.iog.studio69.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.studio69.dto.Brand;
import com.shangpin.iog.studio69.dto.BrandList;
import com.shangpin.iog.studio69.dto.Category;
import com.shangpin.iog.studio69.dto.Good;
import com.shangpin.iog.studio69.dto.GoodDetail;
import com.shangpin.iog.studio69.dto.Goods;
import com.shangpin.iog.studio69.dto.GoodsCategory;
import com.shangpin.iog.studio69.dto.GoodsDetail;

public class DataTransUtil {
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	private static String username;
	private static String password;
	private static String savePath = null;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
		username = bdl.getString("username");
		password = bdl.getString("password");
		savePath = bdl.getString("savePath");
	}
	public static Map<String,Category> getGoodsCategory(){
		//处理------------------------------
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		logger.info("获取所有品类");
		Map<String,Category> returnMap = new HashMap<String, Category>();
		String goodsCategorys = HttpUtil45.postAuth(url+"GetGoodsCatg", null, outTimeConf, username, password);
		save("GetGoodsCatg.txt",goodsCategorys);
		GoodsCategory goodsCategory = null;
		try {
			goodsCategory = ObjectXMLUtil.xml2Obj(GoodsCategory.class, goodsCategorys);
		} catch (JAXBException e1) {
			e1.printStackTrace();
			logger.info("品类数据转换失败");
			System.exit(0);
		}
		for(Category cate : goodsCategory.getCategory()){
			returnMap.put(cate.getCID(), cate);
		}
		return returnMap;
	}
	public static Map<String,String> getBrand(){
		//处理------------------------------
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		logger.info("获取所有品类");
		Map<String,String> returnMap = new HashMap<String, String>();
		String data = HttpUtil45.postAuth(url+"DictBrand", null, outTimeConf, username, password);
		
		save("DictBrand.txt",data);
		
		BrandList brandlist = null;
		try {
			brandlist = ObjectXMLUtil.xml2Obj(BrandList.class, data);
		} catch (JAXBException e1) {
			e1.printStackTrace();
			logger.info("品类数据转换失败");
			System.exit(0);
		}
		for(Brand brand : brandlist.getBrand()){
			returnMap.put(brand.getID(), brand.getName());
		}
		return returnMap;
	}
	public static List<Good> getGoodsList(){
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		logger.info("获取所有spu");
		String data = HttpUtil45.postAuth(url+"GetGoodsList", null, outTimeConf, username, password);
		
		save("GetGoodsList.txt",data);
		
		Goods goods = null;
		try {
			goods = ObjectXMLUtil.xml2Obj(Goods.class, data);
		} catch (JAXBException e1) {
			e1.printStackTrace();
			logger.info("品类数据转换失败");
			System.exit(0);
		}
		return goods.getGood();
	}
	public static Map<String,GoodDetail> getGoodsDetailList(){
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		Map<String,GoodDetail> returnMap = new HashMap<String, GoodDetail>();
		logger.info("获取所有spu");
		String data = HttpUtil45.postAuth(url+"GetGoodsDetailList", null, outTimeConf, username, password);
		
		save("GetGoodsDetailList.txt",data);
		
		GoodsDetail goodsDetail = null;
		try {
			goodsDetail = ObjectXMLUtil.xml2Obj(GoodsDetail.class, data);
		} catch (JAXBException e1) {
			e1.printStackTrace();
			logger.info("品类数据转换失败");
			System.exit(0);
		}
		for(GoodDetail goodDetail :goodsDetail.getGoodDetials()){
			returnMap.put(goodDetail.getID(), goodDetail);
		}
		return returnMap;
	}
	
	public static void save(String name,String data){
    	try {
    		File file = new File(savePath+File.separator+name);
//        	File file = new File("E://"+name);
    		if (!file.exists()) {
    			try {
    				file.getParentFile().mkdirs();
    				file.createNewFile();
    				
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    		FileWriter fwriter = null;
    		try {
    			fwriter = new FileWriter(savePath+File.separator+name);
    			fwriter.write(data);
    		} catch (IOException ex) {
    			ex.printStackTrace();
    		} finally {
    			try {
    				fwriter.flush();
    				fwriter.close();
    			} catch (IOException ex) {
    				ex.printStackTrace();
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
    	
    }
}
