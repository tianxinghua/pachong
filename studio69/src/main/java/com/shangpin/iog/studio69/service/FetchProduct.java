package com.shangpin.iog.studio69.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.studio69.dto.Category;
import com.shangpin.iog.studio69.dto.Good;
import com.shangpin.iog.studio69.dto.GoodDetail;
import com.shangpin.iog.studio69.dto.GoodsCategory;
import com.shangpin.iog.studio69.dto.GoodsDetail;
import com.shangpin.iog.studio69.dto.Item;
import com.shangpin.iog.studio69.dto.Picture;
import com.shangpin.iog.studio69.dto.SecondCategory;
import com.shangpin.iog.studio69.util.DataTransUtil;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by houkun on 2016/01/25.
 */
@Component("studio69")
public class FetchProduct extends AbsSaveProduct{
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");		
	}
	@Autowired
	ProductSearchService productSearchService;
	@Autowired
	private ProductFetchService productFetchService;
    public  Map<String, Object> fetchProductAndSave(){
    	Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
	
		//处理------------------------------	
		String season = "";
		String brandName = "";
		String category = "";
		String productName = "";
		String madein = "";
		String material = "";
		String productCode = "";	
		
		Map<String, String> brand = DataTransUtil.getBrand();
		Map<String, SecondCategory> goodsCategory = DataTransUtil.getSecondCategory();
		List<Good> goodsList = DataTransUtil.getGoodsList();			
		for (Good good : goodsList) {
			try {
				SpuDTO spu = new SpuDTO();
				spu.setId(UUIDGenerator.getUUID());
				spu.setSupplierId(supplierId);
				spu.setSpuId(good.getID());
				season = good.getSeason();
				spu.setSeasonName(guolv(season));				
				brandName = brand.get(good.getBrandID());
				spu.setBrandName(guolv(brandName));					
				try {
					category = goodsCategory.get(good.getCategoryID()).getName();
				} catch (Exception e) {
					e.printStackTrace();
				}
				spu.setCategoryId(good.getCategoryID()); 
				spu.setCategoryName(guolv(category));
				String gender = "";
				if(good.getTypeID().equals("2")){
					gender = "man";
				}else if(good.getTypeID().equals("1")){
					gender = "woman";
				}else if(good.getTypeID().equals("21617687")){
					gender = "boy";
				}else if(good.getTypeID().equals("21617686")){
					gender = "girl";
				}
				spu.setCategoryGender(gender);					
				//sku
				GoodsDetail goodsDetail = DataTransUtil.getGoodsDetailByGoodsID(good.getID());
				if(null != goodsDetail){
					try {
						for(GoodDetail goodDetail : goodsDetail.getGoodDetials()){	
							for(Item item : goodDetail.getStock().getItemlist()){
								try {
									SkuDTO sku = new SkuDTO();
									sku.setId(UUIDGenerator.getUUID());
									sku.setSupplierId(supplierId);
									sku.setSpuId(spu.getSpuId());
									String size = item.getSize();
									if(size.indexOf("½")>0){
										size=size.replace("½","+");
									}
									sku.setSkuId(spu.getSpuId()+"-"+size);									
									productCode = good.getCode();
									sku.setProductCode(StringUtils.isBlank(productCode)?"":productCode);										
									sku.setColor(guolv(goodDetail.getColor()));
									sku.setProductSize(guolv(size));
									sku.setStock(guolv(item.getQty()));									
									productName = good.getGoodsName();
									sku.setProductName(guolv(productName));
									//TODO 暂时设置为市场价
									sku.setMarketPrice(good.getPrice());
									//TODO 暂时设置为euro
									sku.setSaleCurrency("EURO");
									logger.info(sku.getSkuId()+" save good------------"); 
									skuList.add(sku);								
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							madein = goodDetail.getMadeIn();
							material = goodDetail.getComposition();
							
							//保存图片
							try {
								List<Picture> asList = goodDetail.getPictures().getPicturelist();
								List<String> pic = new ArrayList<String>();
								for (Picture picture : asList) {
									pic.add(picture.getPictureUrl());
								}
								imageMap.put(spu.getSpuId()+";"+good.getCode(), pic);
							} catch (Exception e) {
								e.printStackTrace();
							}							
						}	
					} catch (Exception e) {
						e.printStackTrace(); 
					}			
				}
				spu.setMaterial(material);
				spu.setProductOrigin(madein);					
				
				spuList.add(spu);
				
			} catch (Exception e) {
				e.printStackTrace();
			}			
			
		}
		
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;		
		
    }
    
    /**
     * 过滤字\r \n 多余空格
     * @param origin
     * @return
     */
    private String guolv(String origin){
    	if(StringUtils.isNotBlank(origin)){
    		return origin.replaceAll("\r","").replaceAll("\n","").trim(); 
    	}else{
    		return "";
    	}
    	
    }
    
    public static void main(String[] args) {
    	new FetchProduct().fetchProductAndSave();
    }
}
