package com.shangpin.iog.monti.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.monti.dto.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.shangpin.product.AbsSaveProduct;

/**
 * Created by lubaijiang
 */
@Component("monti")
public class FetchProduct extends AbsSaveProduct{
    final Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    
    private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static int day;
	private static String seasonUri = "";
	private static String productUri = "";
	private static String picUri = "";
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		seasonUri = bdl.getString("seasonUri");
		productUri = bdl.getString("productUri");
		picUri = bdl.getString("picUri");
	}

    @Autowired
    ProductFetchService pfs;
    @Autowired
	ProductSearchService productSearchService;

    public Map<String, Object> fetchProductAndSave() {
	    
    	Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
    	
    	try{
	
	        //首先获取季节码  http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetAllSeasonCode?DBContext=Default&key=8IZk2x5tVN
	
	        Gson gson = new Gson();
	
	        String[]  databaseArray = new String[] {"Default"}; //
	        OutTimeConfig outTimeConfig  = new OutTimeConfig(1000*60*2,1000*60*2,1000*60*2);
	        for(String database:databaseArray){
	        	String seasonUrl = seasonUri.replace("@database@", database);
	            String season_json = HttpUtil45.get(seasonUrl,outTimeConfig,null);
	
	            logger.info("season_json=="+season_json); 
	            SeasoncodeList season_list = gson.fromJson(season_json, new TypeToken<SeasoncodeList>() {
	            }.getType());
	
	            String producturl ="";
	
	            for (Seasoncode obj : season_list.getSeasonCode()){
	                int i = 1;
	                while (true){
	                    //然后根据季节码抓取sku  http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetProducts?DBContext=Default&CategoryId=&BrandId=&SeasonCode=[[seasoncode]]&StartIndex=[[startindex]]&EndIndex=[[endindex]]&key=8IZk2x5tVN
	                    producturl = productUri.replace("@database@", database); 
	                    String url = null;
	                    try {
	                        url = producturl.replaceAll("\\[\\[seasoncode\\]\\]", URLEncoder.encode(obj.getSeasonCode(), "UTF-8"))
	                                .replaceAll("\\[\\[startindex\\]\\]", "" + i)
	                                .replaceAll("\\[\\[endindex\\]\\]", "" + (i + 100));
	                    } catch (UnsupportedEncodingException e) {
	                        e.printStackTrace();
	                    }
	
	                    String json = null;
	                    try {
	
	                    	logger.info(url);
	                    	System.out.println(url);
	                        json = HttpUtil45.get(url,outTimeConfig,null);
	                        logger.info("json==="+json);
	                    }catch (Exception e){
	                        e.printStackTrace();
	                    }
	                    if (json != null && !json.isEmpty()) {
	                        Products list = null;
	                        try {
	                            list = gson.fromJson(json, new TypeToken<Products>() {}.getType());
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                        }
	                        if (list != null && list.getProduct() != null) {
	                            String priceUrl = "";
	                            String itemID = "";
	                            String stock = "";
	                            for (Spu spu : list.getProduct()) {
	                                //spu入库
	                                SpuDTO spudto = new SpuDTO();
	                                spudto.setBrandName(spu.getProducer_id());
	                                spudto.setCategoryGender(spu.getType());
	                                spudto.setCategoryName(spu.getCategory());
	                                spudto.setCreateTime(new Date());
	                                spudto.setSeasonId(obj.getSeasonCode());
	                                spudto.setSupplierId(supplierId);
	                                spudto.setSpuId(spu.getProduct_id());
	                                spudto.setId(UUIDGenerator.getUUID());
	                                spudto.setMaterial(spu.getProduct_Material());
	                                spudto.setPicUrl(spu.getUrl());
	                                spudto.setSpuName(spu.getDescription());
	                                spudto.setProductOrigin(spu.getProduct_MadeIn());
	                                
	                                spuList.add(spudto);
	
	                                for (Sku sku : spu.getItems().getItem()) {
	                                    //sku入库操作
	                                    stock = sku.getStock();
	                                    if(StringUtils.isBlank(stock)){
	                                        continue;
	                                    }else{
	                                        if(Integer.valueOf(stock)<=0){
	                                            continue;
	                                        }
	                                    }
	                                    SkuDTO skudto = new SkuDTO();
	                                    skudto.setCreateTime(new Date());
	                                    skudto.setBarcode(sku.getBarcode());
	                                    skudto.setColor(sku.getColor());
	                                    skudto.setId(UUIDGenerator.getUUID());
	                                    skudto.setProductCode(spu.getProduct_name());
	                                    skudto.setProductDescription(spu.getProduct_detail());
	                                    skudto.setProductName(spu.getDescription());
	
	                                    if(sku.getItem_size().length()>4) {
	                                        skudto.setProductSize(sku.getItem_size().substring(0,sku.getItem_size().length()-4));
	                                    }else{
	                                        skudto.setProductSize(sku.getItem_size());
	                                    }
	                                    skudto.setSkuId(sku.getBarcode());
	                                    itemID = sku.getItem_id();
	                                    priceUrl = picUri.replace("@database@", database).replace("@itemID@", itemID);
	                                    try {
	                                        json = HttpUtil45.get(priceUrl, outTimeConfig, null);
	                                    }catch (IllegalArgumentException e){
	                                        e.printStackTrace();
	                                    }
	                                    if(json != null && !json.isEmpty()){
	                                        Price price = null;
	                                        price = gson.fromJson(json,new TypeToken<Price>() {}.getType());
	                                        if(price!=null&&price.getMarket_price()!=null||price.getSuply_price()!=null){
	                                            skudto.setMarketPrice(price.getMarket_price());
	                                            skudto.setSupplierPrice(price.getSuply_price().replace(",","."));
	                                        }
	                                    }
	
	                                    skudto.setSpuId(spu.getProduct_id());
	                                    skudto.setStock(sku.getStock());
	                                    skudto.setSupplierId(supplierId);
	                                    
	                                    skuList.add(skudto);
	                                    
	                                    try {
	                                    	
	                                      //保存图片
	                    	                List<String> imgList = new ArrayList<String>();
	                    	                if (sku.getPictures() != null) {
	                    	                    for (String  imageUrl: sku.getPictures()) {
	                    	                        if (imageUrl != null ) {
	                    	                        	imgList.add(imageUrl);	                        	
	                    	                        }
	                    	                    }
	                    	                    imageMap.put(skudto.getSkuId()+";"+skudto.getProductCode()+" "+skudto.getColor(), imgList);
                 	                    
	                    	                }
	                    	                
	                                    } catch (Exception e) {
	                                    	
	                                    }
	
	
	                                }
	                            }
	                        } else {
	                            break;
	                        }
	
	                        if (list != null && list.getProduct() != null && list.getProduct().length < 100) {
	                            break;
	                        }
	                    }
	                    i += 100;
	                }
	            }
	        }
	        
	    } catch (Exception e) {
			loggerError.info(e.getMessage());
			e.printStackTrace();
	    }
	    returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	    

  }

}
