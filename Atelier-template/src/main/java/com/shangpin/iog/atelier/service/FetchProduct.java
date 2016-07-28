package com.shangpin.iog.atelier.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.atelier.dto.AtelierPrice;
import com.shangpin.iog.atelier.dto.AtelierSku;
import com.shangpin.iog.atelier.dto.AtelierSpu;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by lubaijiang on 2015/9/14.
 */

@Component("Atelier-template")
public class FetchProduct extends AbsSaveProduct{

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	
	private static String api_url = "";
	private static String spu_interface = "";
	private static String sku_interface = "";
	private static String image_interface = "";
	private static String price_interface = "";
	private static String userName = "";
	private static String password = "";
	
	private static String savePath = "";
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");	
		
		api_url = bdl.getString("api_url");
		spu_interface = bdl.getString("spu_interface");
		sku_interface = bdl.getString("sku_interface");
		image_interface = bdl.getString("image_interface");
		price_interface = bdl.getString("price_interface");
		userName = bdl.getString("userName");
		password = bdl.getString("password");
		savePath = bdl.getString("savePath");
	}
	
	public Map<String, Object> fetchProductAndSave() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		
		try{
			//具体的业务
			Map<String,AtelierSpu> spuMap =  handleSpuData();
			List<AtelierSku> skuLists =  handleSkuData();
			Map<String,List<String>> imgMap = handleImagData();
			Map<String,AtelierPrice> priceMap = handlePriceData();
			//保存sku
			for(AtelierSku atelierSku : skuLists){
				try {
					SkuDTO sku = new SkuDTO();
					sku.setId(UUIDGenerator.getUUID());
        			sku.setSupplierId(supplierId);
        			sku.setSpuId(atelierSku.getSpuId());
        			sku.setSkuId(atelierSku.getSpuId()+"-"+atelierSku.getBarcode());
        			if(null !=spuMap && spuMap.containsKey(atelierSku.getSpuId()) && null != spuMap.get(atelierSku.getSpuId())){
        				AtelierSpu atelierSpu = spuMap.get(atelierSku.getSpuId());
        				sku.setProductName(atelierSpu.getCategoryName()+" "+atelierSpu.getBrandName());
        				sku.setMarketPrice(atelierSpu.getSupplierPrice());
        				sku.setProductCode(atelierSpu.getStyleCode()+"-"+atelierSpu.getColorCode());
        				sku.setColor(atelierSpu.getColorName());
        				sku.setProductDescription(atelierSpu.getDescription());        				
        			}
        			if(null != priceMap && priceMap.containsKey(atelierSku.getSpuId()) && null != priceMap.get(atelierSku.getSpuId())){
        				sku.setSupplierPrice(priceMap.get(atelierSku.getSpuId()).getPrice1().replaceAll(",", ".")); 
        			}
        			sku.setSalePrice("");
        			sku.setBarcode(atelierSku.getBarcode());
        			sku.setProductSize(atelierSku.getSize().replaceAll("½", "+")); 
        			sku.setStock(atelierSku.getStock()); 
        			skuList.add(sku);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//保存spu
			for(String spuId:spuMap.keySet()){
				try {
					AtelierSpu atelierSpu = spuMap.get(spuId);
					SpuDTO spu = new SpuDTO();
					spu.setId(UUIDGenerator.getUUID());
		            spu.setSupplierId(supplierId);
		            spu.setSpuId(spuId); 
		            spu.setCategoryGender(atelierSpu.getCategoryGender());
		            spu.setCategoryName(atelierSpu.getCategoryName());
		            spu.setBrandName(atelierSpu.getBrandName());
		            spu.setSeasonName(atelierSpu.getSeasonName());
		            spu.setMaterial(atelierSpu.getMaterial1()+" "+atelierSpu.getMaterial3());
		            spu.setProductOrigin(atelierSpu.getProductOrigin()); 
		            spuList.add(spu);
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}			
			//保存图片
			for(String spuId :imgMap.keySet()){
				String key = spuId;
				if(null !=spuMap && spuMap.containsKey(spuId) && null != spuMap.get(spuId)){
					AtelierSpu atelierSpu = spuMap.get(spuId);
					key = spuId +";"+atelierSpu.getStyleCode()+"-"+atelierSpu.getColorCode();
				}
				imageMap.put(key, imgMap.get(spuId)); 
			}
			
		}catch(Exception ex){
			loggerError.error(ex);
		}
		
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;		
	}

	/**
	 * 处理spu数据，返回spuId<->AtelierSpu map
	 * @return
	 */
	private Map<String,AtelierSpu> handleSpuData(){
		String spuData = getInterfaceData(spu_interface,new OutTimeConfig(1000*60*60,1000*60*5,1000*60*60));
		if(HttpUtil45.errorResult.equals(spuData)){
			return null;
		}else{
			Map<String,AtelierSpu> spuMap = new HashMap<String,AtelierSpu>();
			String[] spuStrings = spuData.split("\\r\\n"); 
			for (int i = 1; i < spuStrings.length; i++) {
				try {
					if (StringUtils.isNotBlank(spuStrings[i])) {						
						String data = "";
						if (i==1) {
						  data =  spuStrings[i].split("\\n")[1];
						}else {
						  data = spuStrings[i];
						}
						String[] spuArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
						AtelierSpu atelierSpu = new AtelierSpu();
						atelierSpu.setSpuId(spuArr[0]);
						atelierSpu.setSeasonName(spuArr[1]);
						atelierSpu.setBrandName(spuArr[2]);
						atelierSpu.setStyleCode(spuArr[3]);
						atelierSpu.setColorCode(spuArr[4]);
						atelierSpu.setCategoryGender(spuArr[5]);
						atelierSpu.setCategoryName(spuArr[8]);
						atelierSpu.setColorName(spuArr[10]);
						atelierSpu.setMaterial1(spuArr[11]);
						atelierSpu.setDescription(spuArr[15]); 
						atelierSpu.setSupplierPrice(spuArr[16]);
						atelierSpu.setMaterial3(spuArr[42]);
						atelierSpu.setProductOrigin(spuArr[40]); 
						spuMap.put(spuArr[0], atelierSpu);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			logger.info("spu的总数是======="+spuMap.size()); 
			return spuMap;
		}
	}
	
	/**
	 * 处理sku数据，返回AtelierSku集合
	 * @return
	 */
	private List<AtelierSku> handleSkuData(){
		String skuData = getInterfaceData(sku_interface,new OutTimeConfig(1000*60*60,1000*60*5,1000*60*60));
		if(HttpUtil45.errorResult.equals(skuData)){
			return null;
		}else{
			List<AtelierSku> skuLists = new ArrayList<AtelierSku>();
			String[] skuStrings = skuData.split("\\r\\n");
			for (int i = 1; i < skuStrings.length; i++) {
				try {
					if (StringUtils.isNotBlank(skuStrings[i])) {
						String data = "";
						if (i==1) {
						  data =  skuStrings[i].split("\\n")[1];
						}else {
						  data = skuStrings[i];
						}
						String[] skuArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
						AtelierSku atelierSku = new AtelierSku();
						atelierSku.setSpuId(skuArr[0]);
						atelierSku.setSize(skuArr[1]);
						atelierSku.setStock(skuArr[2]);
						atelierSku.setBarcode(skuArr[5]); 
						skuLists.add(atelierSku);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			logger.info("sku的总数是======="+skuLists.size()); 
			return skuLists;
		}
	}
	
	/**
	 * 处理图片数据，返回spuId<->图片集合 对照关系集合
	 * @return
	 */
	private Map<String,List<String>> handleImagData(){
		String imageData = getInterfaceData(image_interface,new OutTimeConfig(1000*60*60,1000*60*5,1000*60*60));
		if(HttpUtil45.errorResult.equals(imageData)){
			return null;
		}else{
			Map<String,List<String>> imgMap = new HashMap<String,List<String>>();
			String[] imageStrings = imageData.split("\\r\\n");
			for (int j = 2; j < imageStrings.length; j++) {
				try {
					if (StringUtils.isNotBlank(imageStrings[j])) {	
						String[] imageArr = imageStrings[j].replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
						if (!imgMap.containsKey(imageArr[0])) {
							List<String> list = new ArrayList<String>();
							list.add(imageArr[1]);
							imgMap.put(imageArr[0], list);
						}else {
							List<String> list = imgMap.get(imageArr[0]);
							list.add(imageArr[1]);
							imgMap.put(imageArr[0],list);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			return imgMap;
		}
		
	}
	
	/**
	 * 处理价格数据，返回spuId<->价格  对应关系集合
	 * @return
	 */
	private Map<String,AtelierPrice> handlePriceData(){
		String priceData = getInterfaceData(price_interface,new OutTimeConfig(1000*60*60,1000*60*5,1000*60*60));
		if(HttpUtil45.errorResult.equals(priceData)){
			return null;
		}else{
			Map<String,AtelierPrice> priceMap = new HashMap<String,AtelierPrice>();
			String[] priceStrings = priceData.split("\\r\\n");        
	        for (int i = 1; i < priceStrings.length; i++) {
	        	try {				
	        		if (StringUtils.isNotBlank(priceStrings[i])) {
	        			String data = "";
	    				if (i==1) {
	    				  data =  priceStrings[i].split("\\n")[1];
	    				}else {
	    				  data = priceStrings[i];
	    				}
	    				String[] priceArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
	    				AtelierPrice atelierPrice = new AtelierPrice();
	    				atelierPrice.setPrice1(priceArr[2]);
	    				atelierPrice.setPrice2(priceArr[3]);
	    				atelierPrice.setPrice3(priceArr[4]);
	    				atelierPrice.setPrice4(priceArr[5]);
	    				atelierPrice.setPrice5(priceArr[6]);
	    				atelierPrice.setPrice6(priceArr[7]);
	    				priceMap.put(priceArr[0], atelierPrice);
	            	}       		
	    			
				} catch (Exception e) {
					e.printStackTrace();				
				}
	        }
	        return priceMap;
		}
		
	}
	
	/**
	 * 下载接口文件并返回数据
	 * @param api_interface
	 * @param outTimeConfig
	 * @return
	 */
	private String getInterfaceData(String api_interface,OutTimeConfig outTimeConfig) {
		logger.info("开始拉取"+api_interface+"的数据......"); 
		String data = HttpUtil45.postAuth(api_url+api_interface,null,outTimeConfig,userName,password);		
		int i=0;
		while((StringUtils.isBlank(data) || HttpUtil45.errorResult.equals(data)) && i<10){ 
			try {
				Thread.sleep(1000*3);
				data = HttpUtil45.postAuth(api_url+api_interface,null,outTimeConfig,userName,password);				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				i++;
			}		        	
		}
		logger.info("拉取"+api_interface+"用了=="+i+"次"); 
		saveData(api_interface+".txt",data);
		return data;
	}	
	
	/**
	 * 保存数据
	 * @param name 文件名
	 * @param data 要保存的数据
	 */
	private void saveData(String name,String data){
    	try {
    		File file = new File(savePath+File.separator+name);
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
		}
    	
    }
	
	public static void main(String[] args) {
		FetchProduct fe = new FetchProduct();
		fe.fetchProductAndSave();
	}
	
}

