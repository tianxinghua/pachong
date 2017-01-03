package com.shangpin.iog.ostore.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    final Logger logger = Logger.getLogger("info");
    private static Logger errorLogger = Logger.getLogger("error");
    private static String supplierId;
    private static String url;
    private static String oldurl;
	public static int day;
    private static ResourceBundle bdl=null;
    private static String savePath = null;
    private static String user = "";
    private static String password = "";
    
//    private static String spuData = "";
//    private static String skuData = "";
//    private static String imageData = "";
//    private static String priceData = "";
    
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("url");
        oldurl = bdl.getString("oldurl");
        day = Integer.valueOf(bdl.getString("day"));
        savePath = bdl.getString("savePath");
        user = bdl.getString("user");
        password = bdl.getString("password");
    }
    @Autowired
    private ProductFetchService productFetchService;
    @Autowired
	ProductSearchService productSearchService;

    /**
     * 得到产品信息并储存
     */
    public void fetchProductAndSave(){
    	
    	//全部改用atelier
/*    	List<String> dataList = getDataList();
    	Map<String, Map> dataMap = getDataMap(dataList);
    	Map<String,SkuDTO> skuMap = dataMap.get("sku");
    	Map<String,SpuDTO> spuMap = dataMap.get("spu");
    	Map<String,String> imgMap = dataMap.get("img");*/
    	
    	
    	Map<String,SkuDTO> skuMap= new HashMap<String,SkuDTO>();
      	Map<String,SpuDTO> spuMap= new HashMap<String,SpuDTO>();
    	Map<String,String> imgMap= new HashMap<String,String>();
    	
    	Map<String,Item> itemMap= new HashMap<String,Item>();
    	Map<String,String> priceMap= new HashMap<String,String>();
    	Map<String,String> supplierPriceMap = new HashMap<String,String>();
        //获取产品信息
    	logger.info("get product starting....");
    	System.out.println("get product starting...."); 
		System.out.println("++++++++++++++开始spu++++++++++++++++++++++");
		logger.info("++++++++++++++开始spu++++++++++++++++++++++");
		String spuData = HttpUtil45.postAuth(url+"GetAllItemsMarketplace",null,
				new OutTimeConfig(1000*60*60*24,1000*60*60*24,1000*60*60*24),user,password);
		
		int ii=0;
        while((StringUtils.isBlank(spuData) || HttpUtil45.errorResult.equals(spuData)) && ii<50){ 
        	try {
        		Thread.sleep(1000*3);
        		spuData = HttpUtil45.postAuth(url+"GetAllItemsMarketplace",null,
						new OutTimeConfig(1000*60*60*24,1000*60*60*24,1000*60*60*24),user,password);
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				ii++;
			}		        	
        }
        logger.info("拉取spu用了=="+ii+"次"); 
		save("spuData.txt",spuData);
		
		System.out.println("++++++++++++++开始sku++++++++++++++++++++++");
		logger.info("++++++++++++++开始sku++++++++++++++++++++++");
		int jj = 0;
		String skuData = HttpUtil45.postAuth(url+"GetAllAvailabilityMarketplace",null,
				new OutTimeConfig(1000*60*60*24,1000*60*60*24,1000*60*60*24),user,password);
		while((StringUtils.isBlank(skuData) || HttpUtil45.errorResult.equals(skuData)) && jj<50){
			try {
				Thread.sleep(1000*3);
				skuData = HttpUtil45.postAuth(url+"GetAllAvailabilityMarketplace",null,
						new OutTimeConfig(1000*60*60*24,1000*60*60*24,1000*60*60*24),user,password);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				jj++;
			}
		}
		logger.info("拉取sku用了=="+jj+"次"); 
		save("skuData.txt",skuData);
		
		System.out.println("++++++++++++++开始image++++++++++++++++++++++");
		logger.info("++++++++++++++开始image++++++++++++++++++++++");
		int kk = 0;
		String imageData = HttpUtil45.postAuth(url+"GetAllImageMarketplace",null,
				new OutTimeConfig(1000*60*120,1000*60*120,1000*60*120),user,password);
		while((StringUtils.isBlank(imageData) || HttpUtil45.errorResult.equals(imageData)) && kk<50){
			try {
				Thread.sleep(1000*3);
				imageData = HttpUtil45.postAuth(url+"GetAllImageMarketplace",null,
						new OutTimeConfig(1000*60*120,1000*60*120,1000*60*120),user,password);
			} catch (Exception e) { 
				e.printStackTrace();
			}finally{
				kk++;
			}
		}
		logger.info("拉取图片用了=="+kk+"次"); 
		save("imageData.txt",imageData);
		
		System.out.println("++++++++++++++开始price++++++++++++++++++++++");
		logger.info("++++++++++++++开始price++++++++++++++++++++++"); 
		int ll = 0;		
		String priceData = HttpUtil45.postAuth(url+"GetAllPricelistMarketplace",null,
				new OutTimeConfig(1000*60*120,1000*60*120,1000*60*120),user,password);
		while((StringUtils.isBlank(priceData) || HttpUtil45.errorResult.equals(priceData)) && ll<50){
			try {
				Thread.sleep(1000*3);
				priceData = HttpUtil45.postAuth(url+"GetAllPricelistMarketplace",null,
						new OutTimeConfig(1000*60*120,1000*60*120,1000*60*120),user,password);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				ll++;
			}
		}
		logger.info("拉取价格用了=="+ll+"次");  
		save("priceData.txt",priceData);	
    	
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
        Map<String,String> spuId_seasonM = new HashMap<String,String>();
        
      //得到所有的spu信息
        String[] spuStrings = spuData.split("\\r\\n");
        String[] spuArr = null;
        logger.info("spu的总数是======="+spuStrings.length); 
		for (int i = 1; i < spuStrings.length; i++) {
			try {				
			
				if (StringUtils.isNotBlank(spuStrings[i])) {
					if (i==1) {
					  data =  spuStrings[i].split("\\n")[1];
					}else {
					  data = spuStrings[i];
				}
					spuArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
					
					spuId_seasonM.put(spuArr[0], spuArr[1]);
					
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
				
			} catch (Exception e) {
				e.printStackTrace();
				errorLogger.error(spuStrings[i]); 
				errorLogger.error(i+" "+e);
			}
		}
        
        
        
        //价格信息
        String[] priceStrings = priceData.split("\\r\\n");
        String[] priceArr = null;
        for (int i = 1; i < priceStrings.length; i++) {
        	try {
				
        		if (StringUtils.isNotBlank(priceStrings[i])) {
    				if (i==1) {
    				  data =  priceStrings[i].split("\\n")[1];
    				}else {
    				  data = priceStrings[i];
    				}
            	}
    			priceArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
    			if("A16".equals(spuId_seasonM.get(priceArr[0]))){
    				priceMap.put(priceArr[0], priceArr[4]);
    			}else{
    				priceMap.put(priceArr[0], priceArr[3]);
    			}
    			
    			supplierPriceMap.put(priceArr[0], priceArr[2]);
        		
			} catch (Exception e) {
				e.printStackTrace();
				errorLogger.error(e);
			}
        	
        }
        
        
        
		
		//============================保存spu===================================
		logger.info("开始保存spu，spuMap的大小是============"+spuMap.size()); 
		for (Entry<String, SpuDTO> entry: spuMap.entrySet()) {
			 try {
				productFetchService.saveSPU(entry.getValue());
				logger.info(entry.getKey()+"已保存");				
			} catch (ServiceException e) {
			   try {
					productFetchService.updateMaterial(entry.getValue());
					logger.info(entry.getKey()+"已存在");
				} catch (ServiceException e1) {
					e1.printStackTrace();
					errorLogger.error(entry.getKey()+"+++++++++++++"+e);
				}
			}
		}
		
		//处理sku信息
		//处理图片信息
		String[] imageStrings = imageData.split("\\r\\n");
		String[] imageArr = null;
		for (int j = 2; j < imageStrings.length; j++) {
			try {
				if (StringUtils.isNotBlank(imageStrings[j])) {
					data = imageStrings[j];
					imageArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
					if (!imgMap.containsKey(imageArr[0])) {
						imgMap.put(imageArr[0], imageArr[1]);
					}else {
						imgMap.put(imageArr[0],imgMap.get(imageArr[0])+","+imageArr[1]);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorLogger.error(e);
			}
			
		}
		String[] skuStrings = skuData.split("\\r\\n");
		String[] skuArr = null;
		String size = "";
		int qqq=0;
		int has=0;
		int hasnot=0;
		int stockis0=0;
		logger.info("sku的总数有============"+skuStrings.length);
		for (int i = 1; i < skuStrings.length; i++) {
			
			try {				
			
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
	        			
	        			if (StringUtils.isBlank(priceMap.get(item.getSpuId()))) {
	        				logger.info(item.getSpuId()+"++++++++++++++++++++++++++++++++++"+"没有价格");
							System.err.println(item.getSpuId()+"++++++++++++++++++++++++++++++++++"+"没有价格");
//							continue;
							priceMap.put(item.getSpuId(), "0");
						}
	        			sku.setMarketPrice(priceMap.get(item.getSpuId()).replace(",", "."));
	        			try {
	        				String supplierPrice = supplierPriceMap.get(item.getSpuId()).replaceAll(",", ".");
		        			double suPrice = new BigDecimal(Double.parseDouble(supplierPrice)).setScale(2, RoundingMode.HALF_UP).doubleValue();
		        			sku.setSupplierPrice(String.valueOf(suPrice));  
						} catch (Exception e) {
							e.printStackTrace();
						}	        			
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
	        			has++;
	        			logger.info("sku找到对应的spu,itemId="+skuArr[0]+"尺寸："+skuArr[1]+"barcode="+barCode+"库存为："+stock);
	        			if (stock.equals("0")) {
	        				stockis0++;
						}
					}else{
						qqq++;
						if (skuArr[2].equals("0")) {
							hasnot++;
						}
						logger.info("此sku找不到对应的spu,itemId="+skuArr[0]+"尺寸："+skuArr[1]+"库存为"+skuArr[2]+" barcode="+skuArr[5]);
					}
				}
			
			} catch (Exception e) {
				e.printStackTrace();
				errorLogger.error(skuStrings[i]); 
				errorLogger.error(i+" "+e);
			}
		}
		logger.info("找不到对应关系总数为："+qqq);
		logger.info("找不到对应关系数据中库存为0的有："+hasnot);
		logger.info("有对应sku，spu的数据的总数为："+has);
		logger.info("有对应sku，spu的数据中库存为0的总数为："+stockis0);
		
		//============================保存sku和图片==================================
		logger.info("开始保存sku，skuMap的大小是============"+skuMap.size()); 
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
    					errorLogger.error(entry.getKey()+" "+e); 
    				}
    			} catch (ServiceException e1) {
    				e1.printStackTrace();
    				errorLogger.error(entry.getKey()+" "+e1);
    			}
			}
		}
		//=======================处理图片==========================
		for (Entry<String, String> entry : imgMap.entrySet()) {
			try {
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
			} catch (Exception e) {
				e.printStackTrace();
				errorLogger.error(e);
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
        //HttpUtil45.closePool();
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
   
    public void save(String name,String data){
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
			errorLogger.error(e);
		}
    	
    }
    
    
    public static void main(String[] args){
        new FetchProduct().fetchProductAndSave();
    }
}