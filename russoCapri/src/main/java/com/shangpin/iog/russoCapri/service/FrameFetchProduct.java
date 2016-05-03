package com.shangpin.iog.russoCapri.service;
	
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.russoCapri.dto.Item;
import com.shangpin.product.AbsSaveProduct;
@Component("russocapriframe")
public class FrameFetchProduct extends AbsSaveProduct{
	  final Logger logger = Logger.getLogger(this.getClass());
	    private static String supplierId;
	    private static String url;
	    private static String picpath;
		public static int day;
	    private static ResourceBundle bdl=null;
	    static {
	        if(null==bdl)
	            bdl=ResourceBundle.getBundle("conf");
	        supplierId = bdl.getString("supplierId");
	        url = bdl.getString("url");
	        picpath = bdl.getString("picpath");
	        day = Integer.valueOf(bdl.getString("day"));
	    }
	    private static ApplicationContext factory;
	    private static void loadSpringContext()
	    {
	        factory = new AnnotationConfigApplicationContext(AppContext.class);
	    }
	@Override
//	sku:List(skuDTO) spu:List(spuDTO) image: Map(id;picName,List) 
	public Map<String, Object> fetchProductAndSave() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, List<String>> picMap = new HashMap<String, List<String>>();
		
		Map<String,Item> spuMap= new HashMap<String,Item>();
    	Map<String,String> imgMap= new HashMap<String,String>();
    	Map<String,String> priceMap= new HashMap<String,String>();
	       //获取产品信息
        logger.info("get product starting....");
        System.out.println("开始获取产品信息");
    	String spuData = HttpUtil45.post(url+"GetAllItemsMarketplace",
    										new OutTimeConfig(1000*60*60,1000*60*600,1000*60*600));
    	String skuData = HttpUtil45.post(url+"GetAllAvailabilityMarketplace",
    										new OutTimeConfig(1000*60*60,1000*60*600,1000*60*600));
    	String imageData = HttpUtil45.post(url+"GetAllImageMarketplace",
    										new OutTimeConfig(1000*60*60,1000*60*600,1000*60*600));
    	String priceData = HttpUtil45.post(url+"GetAllPricelistMarketplace",
    										new OutTimeConfig(1000*60*60,1000*60*600,1000*60*600));
    	System.out.println("获取产品信息结束");
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
        List<SpuDTO> spuList = new ArrayList<SpuDTO>();
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
			   spuMap.put(spuArr[0], item);
			   spu.setId(UUIDGenerator.getUUID());
               spu.setSupplierId(supplierId);
               spu.setSpuId(spuArr[0]);
               spu.setBrandName(spuArr[2]);
               spu.setCategoryName(spuArr[8]);
               spu.setSeasonId(spuArr[1]);
               StringBuffer material = new StringBuffer() ;
               material.append(spuArr[11]).append(";");
               material.append(spuArr[15]).append(";");
               material.append(spuArr[42]);
               spu.setMaterial(material.toString());
               spu.setCategoryGender(spuArr[5]);
               spu.setProductOrigin(spuArr[40]);
               spuList.add(spu);
			}
		}
		//处理sku信息
		//处理图片信息
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
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
		String size="";
		String[] skuStrings = skuData.split("\\r\\n");
		String[] skuArr = null;
		for (int i = 1; i < skuStrings.length; i++) {
			if (StringUtils.isNotBlank(skuStrings[i])) {
				if (i==1) {
				  data =  skuStrings[i].split("\\n")[1];
				}else {
				  data = skuStrings[i];
				}
				skuArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
				if (spuMap.containsKey(skuArr[0])) {
					Item item = spuMap.get(skuArr[0]);
					SkuDTO sku = new SkuDTO();
					sku.setId(UUIDGenerator.getUUID());
        			sku.setSupplierId(supplierId);
        			sku.setSpuId(skuArr[0]);
        			size = skuArr[1];
        			if(size.indexOf("½")>0){
        				size=size.replace("½","+");
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
        			skuList.add(sku);
        			
        			
        			//处理图片
					String imgUrls = imgMap.get(skuArr[0]);
					if (StringUtils.isNotBlank(imgUrls)) {
						String[] imgUrlArr = imgUrls.split(",");
						picMap.put(sku.getSkuId()+";"+sku.getProductCode(), Arrays.asList(imgUrlArr));
					}
				}
			}
		}
		returnMap.put("sku",skuList);
		returnMap.put("spu",spuList );
		returnMap.put("image",picMap);
		
		return returnMap;
	}
	
	public static void main(String[] args) {
	  	//加载spring
        loadSpringContext();
        FrameFetchProduct stockImp =(FrameFetchProduct)factory.getBean("russocapriframe");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		stockImp.handleData("sku", supplierId, day, picpath);
	}
	
}
