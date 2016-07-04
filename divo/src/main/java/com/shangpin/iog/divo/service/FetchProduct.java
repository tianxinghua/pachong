package com.shangpin.iog.divo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.shangpin.iog.divo.dto.Item;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

/**
 * Created by monkey on 2015/12/25.
 */
@Component("atelierdivo")
public class FetchProduct {
	private static Logger logger = Logger.getLogger("info");
    private static String supplierId;
    private static String url;
	public static int day;
    private static ResourceBundle bdl=null;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("url");
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
    	Map<String,SkuDTO> skuMap= new HashMap<String,SkuDTO>();
      	Map<String,SpuDTO> spuMap= new HashMap<String,SpuDTO>();
    	Map<String,String> imgMap= new HashMap<String,String>();
    	
    	Map<String,Item> itemMap= new HashMap<String,Item>();
    	Map<String,String> priceMap= new HashMap<String,String>();
        //获取产品信息
    	
    	
        logger.info("get product starting....");
        System.out.println("get product starting....");
    	String spuData = HttpUtil45.post(url+"GetAllItemsMarketplace",
    										new OutTimeConfig(1000*60*60,1000*60*600,1000*60*600));
    	save("divoSPU.txt", spuData);
    	String skuData = HttpUtil45.post(url+"GetAllAvailabilityMarketplace",
    										new OutTimeConfig(1000*60*60,1000*60*600,1000*60*600));
    	save("divoSKU.txt", skuData);
    	String imageData = HttpUtil45.post(url+"GetAllImageMarketplace",
    										new OutTimeConfig(1000*60*60,1000*60*600,1000*60*600));
    	save("divoImage.txt", imageData);
    	String priceData = HttpUtil45.post(url+"GetAllPricelistMarketplace",
    										new OutTimeConfig(1000*60*60,1000*60*600,1000*60*600));
    	save("divoPrice.txt", priceData);
    	
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
        System.out.println("save product into DB begin");
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
//			   item.setColor(StringUtils.isBlank(spuArr[10])?spuArr[4]:spuArr[10]);
			   //设置成颜色码
				String color = "";
				String[] split = spuArr[4].split(" ");
		    	for (int j = 1; j < split.length; j++) {
					color+=split[j]+" ";
				}
			   color = StringUtils.isBlank(color)?"":color;
			   
			   item.setColor(color);
			   item.setSupplierPrice(spuArr[16]);
			   item.setDescription(spuArr[15]);
			   item.setSpuId(spuArr[0]);
			   
			   item.setStyleCode(spuArr[3]);
			   item.setColorCode(spuArr[4]);
			   
			   itemMap.put(spuArr[0], item);

			   spu.setId(UUIDGenerator.getUUID());
               spu.setSupplierId(supplierId);
               spu.setSpuId(spuArr[0]);
               //TODO  品牌名更改
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
		int qqq=0;
		int has=0;
		int hasnot=0;
		int stockis0=0;
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
        			
        			if (StringUtils.isBlank(priceMap.get(item.getSpuId()))) {
        				logger.info(item.getSpuId()+"++++++++++++++++++++++++++++++++++"+"没有价格");
						System.err.println(item.getSpuId()+"++++++++++++++++++++++++++++++++++"+"没有价格");
						continue;
					}
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
		}
		logger.info("找不到对应关系总数为："+qqq);
		logger.info("找不到对应关系数据中库存为0的有："+hasnot);
		logger.info("有对应sku，spu的数据的总数为："+has);
		logger.info("有对应sku，spu的数据中库存为0的总数为："+stockis0);
		
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
			String imgUrls = entry.getValue();
			if (StringUtils.isNotBlank(imgUrls)) {
				String[] imgUrlArr = imgUrls.split(",");
				productFetchService.savePicture(supplierId, entry.getKey(), null, Arrays.asList(imgUrlArr));
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
    
    public void save(String name,String data){
    	
    	File file = new File("/usr/local/app/"+name);
//    	File file = new File("E://"+name);
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
			fwriter = new FileWriter("/usr/local/app/"+name);
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
