package com.shangpin.iog.studio69.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.studio69.dto.Good;
import com.shangpin.iog.studio69.dto.GoodDetail;
import com.shangpin.iog.studio69.dto.GoodsDetail;
import com.shangpin.iog.studio69.dto.Item;
import com.shangpin.iog.studio69.dto.Picture;
import com.shangpin.iog.studio69.dto.SecondCategory;
import com.shangpin.iog.studio69.dto.StudioSkuDto;
import com.shangpin.iog.studio69.dto.StudioSpuDto;
import com.shangpin.iog.studio69.util.DataTransUtil;
import com.shangpin.product.AbsSaveProduct;
/**
 * <p>Title: FetchProduct</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年2月14日 下午3:06:59
 *
 */
@Component("studio69")
public class FetchProduct extends AbsSaveProduct{
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	public static int day;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		
	}
	public  Map<String, Object> fetchProductAndSave(){
		
		Gson gson = new Gson();
		//处理------------------------------	
		String season = "";
		String brandName = "";
		String category = "";
		String madein = "";
		String material = "";
		String color = "";
		
		Map<String, String> brand = DataTransUtil.getBrand();
		Map<String, SecondCategory> goodsCategory = DataTransUtil.getSecondCategory();
		List<Good> goodsList = DataTransUtil.getGoodsList();			
		for (Good good : goodsList) {
			try {
				StudioSpuDto spu = new StudioSpuDto();
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
					List<StudioSkuDto> skuList = new ArrayList<StudioSkuDto>();
					List<String> pic = new ArrayList<String>();
					try {
						for(GoodDetail goodDetail : goodsDetail.getGoodDetials()){	
							for(Item item : goodDetail.getStock().getItemlist()){
								try {
									StudioSkuDto sku = new StudioSkuDto();
									sku.setId(UUIDGenerator.getUUID());
									sku.setSupplierId(supplierId);
									sku.setSpuId(spu.getSpuId());
									String size = item.getSize();
									if(size.indexOf("½")>0){
										size=size.replace("½","+");
									}
									sku.setSkuId(spu.getSpuId()+"-"+size);	
									sku.setProductSize(guolv(size));
									sku.setStock(guolv(item.getQty()));									
									//TODO 暂时设置为市场价
									sku.setSupplierPrice(good.getPrice());
									sku.setMarketPrice(good.getReferencePrice()); 
									//TODO 暂时设置为euro
									sku.setSaleCurrency("EURO");
									logger.info(sku.getSkuId()+" save good------------"); 
									skuList.add(sku);								
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							color = guolv(goodDetail.getColor());
							madein = goodDetail.getMadeIn();
							material = goodDetail.getComposition();
							try {
								List<Picture> asList = goodDetail.getPictures().getPicturelist();
								for (Picture picture : asList) {
									pic.add(picture.getPictureUrl());
								}
							} catch (Exception e) {
								e.printStackTrace();
							}							
						}	
					} catch (Exception e) {
						logger.error("转换sku时异常："+e.getMessage(),e); 
					}
					spu.setSkus(skuList);
					spu.setPictures(pic);
				}
				spu.setSpuName(guolv(good.getGoodsName()));
				spu.setProductModel(good.getCode());
				spu.setColor(color); 
				spu.setMaterial(material);
				spu.setProductOrigin(madein);	
				supp.setData(gson.toJson(spu));  
				pushMessage(null);
				
			} catch (Exception e) {
				logger.error("处理spu时异常："+e.getMessage(),e); 
			}			
		}
		return null;
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
