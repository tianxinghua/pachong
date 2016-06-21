package com.shangpin.iog.deliberti.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;



import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.deliberti.dto.Product;
import com.shangpin.iog.deliberti.util.MyUtil;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("deliberti")
public class FetchProduct {

	private static Logger logger = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	public static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("productUrl");
		day = Integer.valueOf(bdl.getString("day"));
	}
	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;
	public void fetchProductAndSave() {
		Date startDate,endDate= new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
		//获取原有的SKU 仅仅包含价格和库存
		Map<String,SkuDTO> skuDTOMap = new HashedMap();
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		
		try {
			List<Product> allProducts = MyUtil.readLocalCSV(url,Product.class);
		
			for (Product pro : allProducts) {
				System.out.println(pro.getSpuId());
				//保存spu
				String sizes [] = {pro.getSize35(),pro.getSize35x(),pro.getSize36(),pro.getSize36x(),pro.getSize37(),pro.getSize37x(),
						pro.getSize38(),pro.getSize38x(),pro.getSize39(),pro.getSize39x(),pro.getSize40(),pro.getSize40x(),
						pro.getSize41(),pro.getSize41x(),pro.getSize42(),pro.getSize42x(),pro.getSize43(),pro.getSize43x(),
						pro.getSize44(),pro.getSize44x(),pro.getSize45(),pro.getSize45x(),pro.getSize46(),pro.getSize46x(),
						pro.getSize47(),pro.getSize47x(),pro.getSize48(),pro.getSize48x(),pro.getSize49(),pro.getSize49x()};

				for (int i = 0; i < sizes.length; i++) {
					//保存sku
					SkuDTO sku = new SkuDTO();
					
					if(sizes[i]!=null){
						String si[] = sizes[i].split("~");
						if (si.length<1) {
							continue;
						}
						if(si[0].equals("")){
							continue;
						}else{
							if(si[0].indexOf("x")!=-1){
								String size = si[0].replace("x", ".5");
								sku.setProductSize(size);
							}else{
								sku.setProductSize(si[0]);
							}
						}
						try {
							if(si[1].equals("0")){
								continue;
							}else{
								sku.setStock(si[1]);
							}
						} catch (Exception e2) {
							e2.printStackTrace();
							continue;
						}
						sku.setId(UUIDGenerator.getUUID());
						sku.setSupplierId(supplierId);
						sku.setSpuId(pro.getSpuId());
						sku.setSkuId(pro.getSpuId()+"-"+sku.getProductSize());
						sku.setBarcode(pro.getBrandCode());
						sku.setProductCode(pro.getArticleCode());
						sku.setColor(pro.getColor());
						sku.setMarketPrice(pro.getRetailPrice());
						sku.setSalePrice(pro.getCurrentPrice());
						sku.setProductName(pro.getSubCategoryDesc());
						sku.setSaleCurrency("Euro");
						sku.setProductDescription(pro.getDescription());
						
						if(skuDTOMap.containsKey(sku.getSkuId())){
							skuDTOMap.remove(sku.getSkuId());
						}
						try {
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
					}else{
						continue;
					}
					
				}
				
				//保存SPU
				SpuDTO spu = new SpuDTO();
				spu.setId(UUIDGenerator.getUUID());
				spu.setSupplierId(supplierId);
				spu.setSpuId(pro.getSpuId());
				spu.setBrandName(pro.getBrandDesc());
				spu.setCategoryGender(pro.getGenderDesc());
				spu.setCategoryName(pro.getMainCategoryDesc());
				spu.setMaterial(pro.getMaterial());
				spu.setSeasonName(pro.getSeasonDesc());
				spu.setProductOrigin("");
				
				 try {
					productFetchService.saveSPU(spu);
				} catch (ServiceException e) {
				   try {
						productFetchService.updateMaterial(spu);
					} catch (ServiceException e1) {
						e1.printStackTrace();
					}
				}
			
				 
			//保存图片
			List<String> pics = new ArrayList<String>();
			if(pro.getImage()!=null){
				String picArrays[] = pro.getImage().split("~");
				for (int i = 0; i < picArrays.length; i++) {
					pics.add("deliberti.it/productsimage/"+picArrays[i]);
				}
				productFetchService.savePicture(supplierId, spu.getSpuId(), null, pics);
			}else{
				continue;
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
		logger.info("抓取结束");
		} catch (Exception e2) {
			e2.printStackTrace();
			logError.error(e2);
		}
	}
}

