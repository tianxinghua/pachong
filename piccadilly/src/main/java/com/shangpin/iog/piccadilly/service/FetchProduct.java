package com.shangpin.iog.piccadilly.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.piccadilly.dto.PiccadillySkuDto;
import com.shangpin.iog.piccadilly.dto.PiccadillySpuDto;
import com.shangpin.product.AbsSaveProduct;

@Component("piccadilly")
public class FetchProduct extends AbsSaveProduct {
	private static Logger logger = Logger.getLogger("info");
    private static String supplierId;
    private static String url = "";
    private static String userName = "";
    private static String password="";
    private static String filepath = "";
	public static int day;
    private static ResourceBundle bdl=null;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("url");
        day = Integer.valueOf(bdl.getString("day"));
        userName = bdl.getString("userName");
        password = bdl.getString("password");
        filepath = bdl.getString("filepath");
    }
    
    private static OutTimeConfig outTimeConfig = new OutTimeConfig(1000*60*60,1000*60*600,1000*60*600);
    private static Gson gson = new Gson();

    /**
     * 得到产品信息并储存
     */
    public Map<String, Object> fetchProductAndSave(){
    	//获取产品信息
        logger.info("get product starting....");
    	Map<String,String> priceMap = getPriceCollections();
    	Map<String,List<PiccadillySkuDto>> skuMap= getSkuCollections(priceMap);
    	Map<String,String> imgMap = getImageCollections();
        String spuData = HttpUtil45.postAuth(url+"GetAllItemsMarketplace", null, outTimeConfig, userName, password);
        save("spu.txt",spuData);
        logger.info("get product over");
        String data = "";
        //得到所有的spu信息
        String[] spuStrings = spuData.split("\\r\\n");
        String[] spuArr = null;
		for (int i = 1; i < spuStrings.length; i++) {
			try {
				if (StringUtils.isNotBlank(spuStrings[i])) {
					if (i==1) {
					  data =  spuStrings[i].split("\\n")[1];
					}else {
					  data = spuStrings[i];
					}
					spuArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
					PiccadillySpuDto spu = new PiccadillySpuDto();
				    spu.setId(UUIDGenerator.getUUID());
		            spu.setSupplierId(supplierId);
		            spu.setSpuId(spuArr[0]);
		            //TODO  品牌名更改
		            spu.setBrandName(spuArr[2]);
		            spu.setCategoryName(spuArr[8]);
		            spu.setSeasonId(spuArr[1]);
		            StringBuffer material = new StringBuffer() ;
		    	    material.append(spuArr[11]).append(";");
		     	    material.append(spuArr[39]).append(";");
		       	    material.append(spuArr[42]);
		            spu.setMaterial(material.toString());
		            spu.setCategoryGender(spuArr[5]);
		            spu.setProductOrigin(spuArr[40]);
		            spu.setProductModel(spuArr[3]+"-"+spuArr[4]);
		            spu.setColor(StringUtils.isBlank(spuArr[10])?spuArr[4]:spuArr[10]);
		            spu.setSpuName(spuArr[15]);
		            spu.setSkus(skuMap.get(spu.getSpuId())); 
		            String imgUrls = imgMap.get(spu.getSpuId());
		            if (StringUtils.isNotBlank(imgUrls)) {
						String[] imgUrlArr = imgUrls.split(",");
						spu.setPictures(Arrays.asList(imgUrlArr));
		            }
		            supp.setData(gson.toJson(spu));  
    				pushMessage(null);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e); 
			}
			
		}
        logger.info("===================over=====================");
        return null;
    } 
    /**
     * 获取价格
     * @return
     */
    private Map<String,String> getPriceCollections(){
    	Map<String,String> priceMap= new HashMap<String,String>();
    	try {
    		String priceData = HttpUtil45.postAuth(url+"GetAllPricelistMarketplace", null, outTimeConfig, userName, password);
            save("priceData.txt",priceData);
        	//价格信息
            String data = "";
            String[] priceStrings = priceData.split("\\r\\n");
            String[] priceArr = null;
            for (int i = 1; i < priceStrings.length; i++) {
            	if (StringUtils.isNotBlank(priceStrings[i])) {
    				if (i==1) {
    				  data =  priceStrings[i].split("\\n")[1];
    				}else {
    				  data = priceStrings[i];
    				}
            	}
    			priceArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
    			priceMap.put(priceArr[0], priceArr[3]);
            }
		} catch (Exception e) {
			logger.error(e.getMessage(),e); 
		}
        return priceMap;
    }
    /**
     * 获取sku集合
     * @param priceMap
     * @return
     */
    private Map<String,List<PiccadillySkuDto>> getSkuCollections(Map<String,String> priceMap){
    	Map<String,List<PiccadillySkuDto>> skuMap = new HashMap<>();
    	try {
    		String skuData = HttpUtil45.postAuth(url+"GetAllAvailabilityMarketplace", null, outTimeConfig, userName, password);
            save("sku.txt",skuData);
            String[] skuStrings = skuData.split("\\r\\n");
     		String[] skuArr = null;
     		String size = "";
     		String data = "";
     		for (int i = 1; i < skuStrings.length; i++) {
     			if (StringUtils.isNotBlank(skuStrings[i])) {
     				if (i==1) {
     				  data =  skuStrings[i].split("\\n")[1];
     				}else {
     				  data = skuStrings[i];
     				}
     				skuArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
     				PiccadillySkuDto sku = new PiccadillySkuDto();
     				sku.setId(UUIDGenerator.getUUID());
         			sku.setSupplierId(supplierId);
         			sku.setSpuId(skuArr[0]);
         			size = skuArr[1];
         			if (size.indexOf("½")>0) {
     					size=size.replace("½", "+");
     				}
         			sku.setProductSize(size);
         			if (StringUtils.isNotBlank(priceMap.get(skuArr[0]))) {
         				sku.setMarketPrice(priceMap.get(skuArr[0]).replace(",", ""));
     				}else{
     					logger.info(skuArr[0]+"++++++++++++++++++++++++++++++++++"+"没有价格");
     				}
         			sku.setSaleCurrency("EURO");
         			String stock = skuArr[2];
         			String barCode = skuArr[5];
         			sku.setStock(stock);
         			sku.setSkuId(skuArr[0]+"-"+barCode);
         			sku.setBarcode(barCode);
         			if(skuMap.containsKey(sku.getSpuId())){
         				skuMap.get(sku.getSpuId()).add(sku);
         			}else{
         				List<PiccadillySkuDto> lists = new ArrayList<>();
         				lists.add(sku);
         				skuMap.put(sku.getSkuId(), lists);
         			}
         			logger.info("sku找到对应的spu,itemId="+skuArr[0]+"尺寸："+skuArr[1]+"barcode="+barCode+"库存为："+stock);
     			}
     		}
		} catch (Exception e) {
			logger.error(e.getMessage(),e); 
		}
 		return skuMap;
    }
    /**
     * 获取图片集合
     * @return
     */
    private Map<String,String> getImageCollections(){
    	Map<String,String> imgMap= new HashMap<String,String>();
    	try {
    		String imageData = HttpUtil45.postAuth(url+"GetAllImageMarketplace", null, outTimeConfig, userName, password);
            save("imageData.txt",imageData);
            String[] imageStrings = imageData.split("\\r\\n");
     		String[] imageArr = null;
     		String data = "";
     		for (int j = 2; j < imageStrings.length; j++) {
     			if (StringUtils.isNotBlank(imageStrings[j])) {
     				data = imageStrings[j];
     				imageArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
     				if (!imgMap.containsKey(imageArr[0])) {
     					imgMap.put(imageArr[0], imageArr[1]);
     				}else {
     					imgMap.put(imageArr[0],imgMap.get(imageArr[0])+","+imageArr[1]);
     				}
     			}
     		}
		} catch (Exception e) {
			logger.error(e.getMessage(),e); 
		}
 		return imgMap;
    }
    
    private void save(String name,String data){
    	File file = new File(filepath+name);
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				logger.error(e.getMessage(),e); 
			}
		}
		FileWriter fwriter = null;
		try {
			fwriter = new FileWriter(filepath+name);
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
    }
    public static void main(String[] args) {
    	String aaa= "00200 BIANCO OTTICO";
    	String[] split = aaa.split(" ");
    	String color = "";
    	for (int i = 1; i < split.length; i++) {
			color+=split[i]+" ";
		}
    	System.out.println(color);
    }
}
