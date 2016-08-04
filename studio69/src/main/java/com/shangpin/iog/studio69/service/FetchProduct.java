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
import com.shangpin.iog.studio69.util.DataTransUtil;

/**
 * Created by houkun on 2016/01/25.
 */
@Component("studio69")
public class FetchProduct {
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
	@Autowired
	ProductSearchService productSearchService;
	@Autowired
	private ProductFetchService productFetchService;
    public void fetchProductAndSave(){
    	
    	
        	Date startDate,endDate= new Date();
			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
			//获取原有的SKU 仅仅包含价格和库存
			Map<String,SkuDTO> skuDTOMap = new HashedMap();
			try {
				skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			//处理------------------------------
    	
    		String season = "";
    		String brandName = "";
    		String category = "";
    		String productName = "";
    		String madein = "";
    		String material = "";
    		String productCode = "";
    	
    		
			Map<String, String> brand = DataTransUtil.getBrand();
			Map<String, Category> goodsCategory = DataTransUtil.getGoodsCategory();
			List<Good> goodsList = DataTransUtil.getGoodsList();			
			for (Good good : goodsList) {
				try {
					SpuDTO spu = new SpuDTO();
					spu.setId(UUIDGenerator.getUUID());
					spu.setSupplierId(supplierId);
					spu.setSpuId(good.getID());
					season = good.getSeason();
					spu.setSeasonName(StringUtils.isBlank(season)?"":season);				
					brandName = brand.get(good.getBrandID());
					spu.setBrandName(StringUtils.isBlank(brandName)?"":brandName);					
					try {
						category = goodsCategory.get(good.getCategoryID()).getName();
					} catch (Exception e) {
						e.printStackTrace();
					}
					spu.setCategoryId(good.getCategoryID()); 
					spu.setCategoryName(StringUtils.isBlank(category)?"":category);					
					spu.setCategoryGender(good.getTypeID().equals("2")?"man":"women");					
					//sku
					GoodsDetail goodsDetail = DataTransUtil.getGoodsDetailByGoodsID(good.getID());
					if(null != goodsDetail){
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
									sku.setColor(goodDetail.getColor());
									sku.setProductSize(size);
									sku.setStock(item.getQty());									
									productName = good.getGoodsName();
									sku.setProductName(StringUtils.isBlank(productName)?"":productName);
									//TODO 暂时设置为市场价
									sku.setMarketPrice(good.getPrice());
									//TODO 暂时设置为euro
									sku.setSaleCurrency("EURO");
									try {
				        				
				        				if(skuDTOMap.containsKey(sku.getSkuId())){
				    						skuDTOMap.remove(sku.getSkuId());
				    					}        				
										productFetchService.saveSKU(sku);
				        			} catch (ServiceException e) {
										try {
					        				if (e.getMessage().equals("数据插入失败键重复")) {
					        					//更新价格和库存
					        					productFetchService.updatePriceAndStock(sku);
					        				} else {
					        					e.printStackTrace();
					        				}
					        			} catch (ServiceException e1) {
					        				e1.printStackTrace();
					        			}
									}
								
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
								productFetchService.savePicture(supplierId, spu.getSpuId(), null,pic);
							} catch (Exception e) {
								e.printStackTrace();
							}							
						}						
					}
					spu.setMaterial(material);
					spu.setProductOrigin(madein);					
					try {
						productFetchService.saveSPU(spu);
					} catch (ServiceException e) {
					   try {
							productFetchService.updateMaterial(spu);
						} catch (ServiceException e1) {
							e1.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
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
    }
    public static void main(String[] args) {
    	new FetchProduct().fetchProductAndSave();
    }
}
