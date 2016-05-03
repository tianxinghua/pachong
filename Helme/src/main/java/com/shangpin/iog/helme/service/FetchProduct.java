package com.shangpin.iog.helme.service;

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
import com.shangpin.iog.helme.dto.Item;
import com.shangpin.iog.helme.excel.ReadExcel;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("Helme")
public class FetchProduct {

	private static Logger logger = Logger.getLogger("info");
	private static Logger error = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	public static int day;
	private static String url = "";
	private static String path = "";
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		url = bdl.getString("url");
		path = bdl.getString("path");
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
		
		try{
			
			//ReadExcel.downLoadFile(url, path);
			List<Item> allProducts = ReadExcel.readExcel(Item.class,path);
			
			for (Item item : allProducts) {
				SpuDTO spu = new SpuDTO();
				spu.setId(UUIDGenerator.getUUID());
				spu.setSupplierId(supplierId);
				spu.setSpuId(item.getGruppo_Marchio());
				spu.setBrandName(item.getGruppo_Marchio());	
				String cat = item.getGruppo_Marchio();
				String gender = "";
				if(cat.startsWith("WOMEN")){
					gender = "WOMEN";
				}else if(cat.startsWith("MEN")){
					gender = "MEN";
				}else{
					gender = cat;
				}
				spu.setCategoryGender(gender);
				spu.setCategoryName(cat);
				spu.setMaterial(item.getMateriale());
				spu.setSeasonName(item.getStagione());
				spu.setProductOrigin("");
				
				//============================保存spu===================================
				 try {
					productFetchService.saveSPU(spu);
				} catch (ServiceException e) {
				   try {
						productFetchService.updateMaterial(spu);
					} catch (ServiceException e1) {
						e1.printStackTrace();
					}
				} 
				String size = item.getTaglia();
				String[] sizes = size.split(" ");
				if(sizes.length>0){
//					for (int i = 0; i < sizes.length; i++) {
						try{
							
//							String[] stockSize = sizes[i].split("/");
//							String stock = stockSize[0];
//							String productSize = stockSize[1];
							//============================保存sku===================================
							SkuDTO sku = new SkuDTO();
							sku.setId(UUIDGenerator.getUUID());
							sku.setSupplierId(supplierId);
							sku.setSpuId(spu.getSpuId());
							sku.setSkuId(item.getModello());
							sku.setProductCode(item.getModello());
							sku.setBarcode(item.getBarcode());
							sku.setColor(item.getColore());
							sku.setSalePrice(item.getPrezzo_ven());
							sku.setProductName(item.getGenere());
							sku.setProductSize(item.getTaglia());
							sku.setStock(item.getGiacenza());
							sku.setSaleCurrency("Euro");

							if (skuDTOMap.containsKey(sku.getSkuId())) {
								skuDTOMap.remove(sku.getSkuId());
							}
							try {
								productFetchService.saveSKU(sku);
							} catch (Exception e) {
								try {
//									if (e.getMessage().equals("数据插入失败键重复")) {
										// 更新价格和库存
										productFetchService.updatePriceAndStock(sku);
//									} else {
//										e.printStackTrace();
//									}
										e.printStackTrace();
										
								} catch (ServiceException e1) {
									e1.printStackTrace();
								}
							}
							
						}catch(Exception ex){
							ex.printStackTrace();
							error.error(ex);
							error.error(spu.getSpuId()); 
						}					
//					}
				}
						
				//productFetchService.savePicture(supplierId, spu.getSpuId(), null, imageList);
			}

		}catch(Exception ex){
			error.error(ex);
			ex.printStackTrace();
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
		
		logger.info("抓取结束");
	}
	
	
}

