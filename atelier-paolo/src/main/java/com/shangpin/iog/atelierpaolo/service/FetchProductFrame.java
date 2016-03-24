package com.shangpin.iog.atelierpaolo.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.atelierpaolo.dto.Item;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.product.AbsSaveProduct;

@Component("paoloframe")
public class FetchProductFrame extends AbsSaveProduct{
	
	 private static ApplicationContext factory;
	    private static void loadSpringContext()
	    {
	        factory = new AnnotationConfigApplicationContext(AppContext.class);
	    }
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
	@Override
	public Map<String, Object> fetchProductAndSave() {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		
	    OutTimeConfig outTimeConfig = new OutTimeConfig(1000*60*60,1000*60*600,1000*60*600);
	    String skuData = HttpUtil45.postAuth(url+"GetAllAvailabilityMarketplace", null, outTimeConfig, "shangpin", "fiorillo1003");
	    String imageData = HttpUtil45.postAuth(url+"GetAllImageMarketplace", null, outTimeConfig, "shangpin", "fiorillo1003");
	    String priceData = HttpUtil45.postAuth(url+"GetAllPricelistMarketplace", null, outTimeConfig, "shangpin", "fiorillo1003");
	    String spuData = HttpUtil45.postAuth(url+"GetAllItemsMarketplace", null, outTimeConfig, "shangpin", "fiorillo1003");
    	Map<String,Item> itemMap= new HashMap<String,Item>();
    	Map<String,String> priceMap= new HashMap<String,String>();
    	
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
        	   material.append(spuArr[11]).append(";");
        	   material.append(spuArr[39]).append(";");
               material.append(spuArr[42]);
               spu.setMaterial(material.toString());
               spu.setCategoryGender(spuArr[5]);
               spu.setProductOrigin(spuArr[40]);
               //=====================================================================================
               spuList.add(spu);
			}
		}
	    
		//处理sku信息
		
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
        			
        			if (StringUtils.isBlank(priceMap.get(item.getSpuId()))) {
        				logger.info(item.getSpuId()+"++++++++++++++++++++++++++++++++++"+"没有价格");
						System.err.println(item.getSpuId()+"++++++++++++++++++++++++++++++++++"+"没有价格");
						continue;
					}
        			sku.setMarketPrice(priceMap.get(item.getSpuId()).replace(",", ""));
        			sku.setSalePrice("");
        			sku.setSupplierPrice("");
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
        			skuList.add(sku);
        			logger.info("sku找到对应的spu,itemId="+skuArr[0]+"尺寸："+skuArr[1]+"barcode="+barCode+"库存为："+stock);
				}else{
					logger.info("此sku找不到对应的spu,itemId="+skuArr[0]+"尺寸："+skuArr[1]+"库存为"+skuArr[2]+" barcode="+skuArr[5]);
				}
			}
		}
		
		//处理图片信息
		String[] imageStrings = imageData.split("\\r\\n");
		String[] imageArr = null;
		List<String> value = null;
		for (int j = 2; j < imageStrings.length; j++) {
			if (StringUtils.isNotBlank(imageStrings[j])) {
				data = imageStrings[j];
				imageArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
				String key = imageArr[0]+";"+imageArr[0];
				if (!imageMap.containsKey(key)) {
					value = new ArrayList<String>();
					value.add(imageArr[1]);
				}else {
					value = imageMap.get(key);
					value.add(imageArr[1]);
				}
				imageMap.put(key,value);
			}
		}
		returnMap.put("sku", skuList);returnMap.put("spu", spuList);returnMap.put("image", imageMap);
		return returnMap;
	}
	public static void main(String[] args) throws Exception {
	  	//加载spring
        loadSpringContext();
        System.out.println("asdasdasdasd");
        FetchProductFrame stockImp =(FetchProductFrame)factory.getBean("paoloframe");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		stockImp.handleData("spu", supplierId, day, "E://atelirePaoloFrame//");
//		stockImp.handleData("spu", supplierId, day, "");
	}
}
