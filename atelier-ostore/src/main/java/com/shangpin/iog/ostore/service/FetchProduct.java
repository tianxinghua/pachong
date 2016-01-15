package com.shangpin.iog.ostore.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.ostore.dto.Item;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

/**
 * Created by monkey on 2015/12/25.
 */
@Component("atelierostore")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static String supplierId;
    private static String url;
    private static String oldurl;
	public static int day;
    private static ResourceBundle bdl=null;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("url");
        oldurl = bdl.getString("oldurl");
        day = Integer.valueOf(bdl.getString("day"));
    }
    @Autowired
    private ProductFetchService productFetchService;
    @Autowired
	ProductSearchService productSearchService;

    /**
     * 得到产品信息并储存
     */
    public void fetchProductAndSave(){
    	List<String> dataList = getDataList();
    	Map<String, Map> dataMap = getDataMap(dataList);
    	Map<String,SkuDTO> skuMap = dataMap.get("sku");
    	Map<String,SpuDTO> spuMap = dataMap.get("spu");
    	Map<String,String> imgMap = dataMap.get("img");
    	Map<String,Item> itemMap= new HashMap<String,Item>();
    	Map<String,String> priceMap= new HashMap<String,String>();
        //获取产品信息
        logger.info("get product starting....");
    	String spuData = HttpUtil45.post(url+"GetAllItemsMarketplace",
    										new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30));
    	String skuData = HttpUtil45.post(url+"GetAllAvailabilityMarketplace",
    										new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30));
    	String imageData = HttpUtil45.post(url+"GetAllImageMarketplace",
    										new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30));
    	String priceData = HttpUtil45.post(url+"GetAllPricelistMarketplace",
    										new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30));

    	Date startDate,endDate= new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
		
		
		//获取原有的SKU 仅仅包含价格和库存
		Map<String,SkuDTO> skuDTOMap = new HashMap<String,SkuDTO>();
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    	
        logger.info("get product over");
        //映射数据并保存
        logger.info("save product into DB begin");
        String data = "";
        
        //价格信息
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
        
        
        //得到所有的spu信息
        String[] spuStrings = spuData.split("\\r\\n");
        String[] spuArr = null;
		for (int i = 1; i < spuStrings.length; i++) {
			if (StringUtils.isNotBlank(spuStrings[i])) {
				if (i==1) {
				  data =  spuStrings[i].split("\\n")[1];
				}else {
				  data = spuStrings[i];
			}
				spuArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
				SpuDTO spu = new SpuDTO();
				Item item = new Item();
			   item.setColor(StringUtils.isBlank(spuArr[10])?spuArr[4]:spuArr[10]);
			   item.setSupplierPrice(spuArr[16]);
			   item.setDescription(spuArr[15]);
			   item.setSpuId(spuArr[0]);
			   
			   item.setStyleCode(spuArr[3]);
			   item.setColorCode(spuArr[4]);
			   
			   itemMap.put(spuArr[0], item);

			   spu.setId(UUIDGenerator.getUUID());
               spu.setSupplierId(supplierId);
               spu.setSpuId(spuArr[0]);
               spu.setBrandName(spuArr[2]);
               spu.setCategoryName(spuArr[8]);
               spu.setSeasonId(spuArr[1]);
               StringBuffer material = new StringBuffer() ;
               if (StringUtils.isNotBlank(spuArr[11])) {
            	   material.append(spuArr[11]).append(";");
               }else if(StringUtils.isNotBlank(spuArr[15])){
            	   material.append(spuArr[15]).append(";");
               }else if (StringUtils.isNotBlank(spuArr[42])) {
            	   material.append(spuArr[42]);
               }
               spu.setMaterial(material.toString());
               spu.setCategoryGender(spuArr[5]);
               spu.setProductOrigin(spuArr[40]);
               //=====================================================================================
               spuMap.put(spu.getSpuId(), spu);
			}
		}
		
		//============================保存spu===================================
		for (Entry<String, SpuDTO> entry: spuMap.entrySet()) {
			 try {
				productFetchService.saveSPU(entry.getValue());
			} catch (ServiceException e) {
			   try {
					productFetchService.updateMaterial(entry.getValue());
				} catch (ServiceException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		//处理sku信息
		//处理图片信息
		String[] imageStrings = imageData.split("\\r\\n");
		String[] imageArr = null;
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
		String[] skuStrings = skuData.split("\\r\\n");
		String[] skuArr = null;
		String size = "";
		for (int i = 1; i < skuStrings.length; i++) {
			if (StringUtils.isNotBlank(skuStrings[i])) {
				if (i==1) {
				  data =  skuStrings[i].split("\\n")[1];
				}else {
				  data = skuStrings[i];
				}
				skuArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
				if (itemMap.containsKey(skuArr[0])) {
					Item item = itemMap.get(skuArr[0]);
					SkuDTO sku = new SkuDTO();
					sku.setId(UUIDGenerator.getUUID());
        			sku.setSupplierId(supplierId);
        			sku.setSpuId(skuArr[0]);
        			
        			size = skuArr[1];
        			if (size.indexOf("½")>0) {
						size=size.replace("½", "+");
					}
        			sku.setProductSize(size);
        			sku.setMarketPrice(priceMap.get(item.getSpuId()).replace(",", ""));
        			sku.setColor(item.getColor());
        			sku.setProductDescription(item.getDescription());
        			sku.setSaleCurrency("EURO");
        			String stock = skuArr[2];
        			String barCode = skuArr[5];
        			sku.setStock(stock);
        			//skuid+barcode
        			sku.setSkuId(skuArr[0]+"-"+barCode);
        			sku.setBarcode(barCode);
        			sku.setProductCode(item.getStyleCode()+"-"+item.getColorCode());
        			//====================================================================================
        			skuMap.put(sku.getSkuId(), sku);
				}
			}
		}
		
		//============================保存sku和图片==================================
		for (Entry<String, SkuDTO> entry : skuMap.entrySet()) {
			if(skuDTOMap.containsKey(entry.getValue().getSkuId())){
				skuDTOMap.remove(entry.getValue().getSkuId());
			}
			try {
				productFetchService.saveSKU(entry.getValue());
			} catch (ServiceException e) {
				try {
    				if (e.getMessage().equals("数据插入失败键重复")) {
    					//更新价格和库存
    					productFetchService.updatePriceAndStock(entry.getValue());
    				} else {
    					e.printStackTrace();
    				}
    			} catch (ServiceException e1) {
    				e1.printStackTrace();
    			}
			}
		}
		//=======================处理图片==========================
		for (Entry<String, String> entry : imgMap.entrySet()) {
			if (entry.getKey().contains("dota1")) {
				String imgUrls = entry.getValue();
				if (StringUtils.isNotBlank(imgUrls)) {
					String[] imgUrlArr = imgUrls.split(",");
					productFetchService.savePicture(supplierId, null, entry.getKey().split("|")[0], Arrays.asList(imgUrlArr));
				}
			}else{
				String imgUrls = entry.getValue();
				if (StringUtils.isNotBlank(imgUrls)) {
					String[] imgUrlArr = imgUrls.split(",");
					productFetchService.savePicture(supplierId, entry.getKey(), null, Arrays.asList(imgUrlArr));
				}
			}
		}
		
		//更新网站不再给信息的老数据
		for(Iterator<Map.Entry<String,SkuDTO>> itor = skuDTOMap.entrySet().iterator();itor.hasNext(); ){
			 Map.Entry<String,SkuDTO> entry =  itor.next();
			if(!"0".equals(entry.getValue().getStock())){//更新不为0的数据 使其库存为0
				entry.getValue().setStock("0");
				try {
					productFetchService.updatePriceAndStock(entry.getValue());
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
        logger.info("save product into DB success");
    }
    public List<String> getDataList(){
        OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
        timeConfig.confRequestOutTime(10*60*1000);
        timeConfig.confConnectOutTime(10*60*1000);
        timeConfig.confSocketOutTime(10*60*1000);
        List<String> resultList = HttpUtil45.getContentListByInputSteam(oldurl, timeConfig, null, null, null);
        HttpUtil45.closePool();
    	return resultList;
    }
    public Map<String,Map> getDataMap(List<String> resultList){
    	
    	Map<String,Map> returnMap= new HashMap<String,Map>();
      	Map<String,SkuDTO> skuMap= new HashMap<String,SkuDTO>();
      	Map<String,SpuDTO> spuMap= new HashMap<String,SpuDTO>();
    	Map<String,String> imgMap= new HashMap<String,String>();
        int i=0;
        String stock="",size ="";
        String skuId = "";
        for(String content:resultList){
            if(i==0){
                i++;
                continue;
            }
            i++;
            //SKU;Brand;ModelName;Color;ColorFilter;Description;Materials;Sex;Category;Season;Price;Discount;Images;SizesFormat;Sizes
            // 0 ;  1   ;  2      ;3    ;   4       ;    5      ;6        ;7  ;  8     ;  9   ;10   ; 11     ;  12  ;  13       ; 14
            String[] contentArray = content.split(";");
            if(null==contentArray||contentArray.length<15) continue;
                SpuDTO spu = new SpuDTO();
                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId(supplierId);
                spu.setSpuId(contentArray[0]);
                spu.setBrandName(contentArray[1]);
                spu.setCategoryName(contentArray[8]);
                spu.setSpuName(contentArray[2]);
                if (contentArray[9].equals("P16")) {
					continue;
				}
                spu.setSeasonId(contentArray[9]);
                spu.setMaterial(contentArray[6]);
                spu.setCategoryGender(contentArray[7]);
                System.out.println(spu.getCategoryGender());
                spuMap.put(spu.getSpuId(), spu);

                String[] sizeArray = contentArray[14].split(",");

                for(String sizeAndStock:sizeArray){
                    if(sizeAndStock.contains("(")&&sizeAndStock.length()>1) {
                        size = sizeAndStock.substring(0, sizeAndStock.indexOf("("));
                        stock = sizeAndStock.substring(sizeAndStock.indexOf("(")+1, sizeAndStock.length() - 1);
                        //System.out.println("库存"+stock);
                    }
                    SkuDTO sku  = new SkuDTO();
                        sku.setId(UUIDGenerator.getUUID());
                        sku.setSupplierId(supplierId);

                        sku.setSpuId(contentArray[0]);
                        skuId = contentArray[0] + "-"+size;
                        if(skuId.indexOf("½")>0){
                            skuId = skuId.replace("½","+");
                        }
                        sku.setSkuId(skuId);
                        sku.setProductSize(size.replace("½","+"));
                        sku.setMarketPrice(contentArray[10]);
                        sku.setColor(contentArray[3]);
                        sku.setProductDescription(contentArray[5]);
                        sku.setStock(stock);
                        skuMap.put(sku.getSkuId(), sku);
                    imgMap.put(sku.getSkuId()+"|"+"dota1", contentArray[12]);
                }
        }
        returnMap.put("sku", skuMap);
        returnMap.put("spu", spuMap);
        returnMap.put("img", imgMap);
    	return returnMap;
    }
   
    
    
    
    public static void main(String[] args){
        new FetchProduct().fetchProductAndSave();
    }
}
