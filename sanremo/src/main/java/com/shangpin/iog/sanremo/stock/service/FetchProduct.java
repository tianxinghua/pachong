package com.shangpin.iog.sanremo.stock.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.sanremo.stock.dto.*;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by loyalty on 15/6/8.
 */
@Component("sanremo")
public class FetchProduct {
	final Logger logger = Logger.getLogger(this.getClass());
    private static Logger loggerError = Logger.getLogger("error");
    //String supplierId = "2015081701439";
    private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
	}
    //String supplierId = "2015082801463";

    @Autowired
    private ProductFetchService pfs;
    @Autowired
   	ProductSearchService productSearchService;

    public void fetchProductAndSave() {
	    try{
	    	Date startDate,endDate= new Date();
			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
			//获取原有的SKU 仅仅包含价格和库存
			Map<String,SkuDTO> skuDTOMap = new HashedMap();
			try {
				skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
	
	        //首先获取季节码  http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetAllSeasonCode?DBContext=Default&key=8IZk2x5tVN
	
	        Gson gson = new Gson();
	
	        String[]  databaseArray = new String[] {"sanremo"}; //
	        for(String database:databaseArray){
	            String season_json = HttpUtil45.get("http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetAllSeasonCode?DBContext="+database+"&key=8IZk2x5tVN",new OutTimeConfig(1000*60*2,1000*60*2,1000*60*2),null);
	
	            SeasoncodeList season_list = gson.fromJson(season_json, new TypeToken<SeasoncodeList>() {
	            }.getType());
	
	            String producturl ="";
	
	            for (Seasoncode obj : season_list.getSeasonCode()){
	                int i = 1;
	                while (true){
	                    //然后根据季节码抓取sku  http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetProducts?DBContext=Default&CategoryId=&BrandId=&SeasonCode=[[seasoncode]]&StartIndex=[[startindex]]&EndIndex=[[endindex]]&key=8IZk2x5tVN
	                    producturl = "http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetProducts?DBContext="+database+"&CategoryId=&BrandId=&SeasonCode=[[seasoncode]]&StartIndex=[[startindex]]&EndIndex=[[endindex]]&key=8IZk2x5tVN";
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
	
	                        json = HttpUtil45.get(url,new OutTimeConfig(1000*60*2,1000*60*2,1000*60*2),null);
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
	                            String priceUrl;
	                            String itemID;
	                            String stock;
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
	                                spudto.setMaterial(spu.getProduct_detail());
//	                                spudto.setPicUrl(spu.getUrl());
	                                spudto.setSpuName(spu.getDescription());
	                                try {
	                                    pfs.saveSPU(spudto);
	                                } catch (ServiceException e) {
	                                	try{
	                                		pfs.updateMaterial(spudto);
	            		            	}catch(ServiceException ex){
	            		            		ex.printStackTrace();
	            		            	}
	                                    e.printStackTrace();
	                                }
	
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
	                                    priceUrl = "http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetPriceByItemID?DBContext="+database+"&ItemID="+itemID+"&key=8IZk2x5tVN";
	                                    try {
	                                        json = HttpUtil45.get(priceUrl, new OutTimeConfig(1000*60*2,1000*60*2,1000*60*2), null);
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
	                                    try {
	                                    	if(skuDTOMap.containsKey(skudto.getSkuId())){
	                							skuDTOMap.remove(skudto.getSkuId());
	                						}
	                                        pfs.saveSKU(skudto);
	                                        
	                                      //保存图片
	                    	                List<String> imgList = new ArrayList<String>();
	                    	                if (sku.getPictures() != null) {
	                    	                    for (String  imageUrl: sku.getPictures()) {
	                    	                        if (imageUrl != null ) {
	                    	                        	imgList.add(imageUrl);	                        	
	                    	                        }
	                    	                    }
	                    	                    pfs.savePicture(supplierId, null, skudto.getSkuId(), imgList);
	                    	                }
	                    	                
	//                                        for(String image : sku.getPictures()){
	//                                            ProductPictureDTO pic = new ProductPictureDTO();
	//                                            pic.setPicUrl(image);
	//                                            pic.setId(UUIDGenerator.getUUID());
	//                                            pic.setSkuId(sku.getItem_id());
	//                                            pic.setSupplierId(supplierId);
	//                                            try {
	//                                                pfs.savePictureForMongo(pic);
	//                                            } catch (ServiceException e) {
	//                                                e.printStackTrace();
	//                                            }
	//                                        }
	                                    } catch (ServiceException e) {
	                                        try {
	                                            if(e.getMessage().equals("数据插入失败键重复")){
	                                                pfs.updatePriceAndStock(skudto);
	                                            } else{
	                                                e.printStackTrace();
	                                            }
	
	                                        } catch (ServiceException e1) {
	                                            e1.printStackTrace();
	                                        }
	
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
	        
	        //更新网站不再给信息的老数据
			for(Iterator<Map.Entry<String,SkuDTO>> itor = skuDTOMap.entrySet().iterator();itor.hasNext(); ){
				 Map.Entry<String,SkuDTO> entry =  itor.next();
				if(!"0".equals(entry.getValue().getStock())){//更新不为0的数据 使其库存为0
					entry.getValue().setStock("0");
					try {
						pfs.updatePriceAndStock(entry.getValue());
					} catch (ServiceException e) {
						e.printStackTrace();
					}
				}
			}
	    } catch (Exception e) {
			loggerError.info(e.getMessage());
			e.printStackTrace();
	    }

    }

}
