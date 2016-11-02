package com.shangpin.iog.MengottiOnline.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.MengottiOnline.dto.Item;
import com.shangpin.iog.MengottiOnline.utils.CVSUtil;
import com.shangpin.iog.MengottiOnline.utils.DownLoad;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
//import com.shangpin.iog.MengottiOnline.util.MengottiOnlineUtil;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("MengottiOnline")
public class FetchProduct {

	private static Logger logger = Logger.getLogger("info");
	private static Logger error = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	public static int day;
	private static String url = null;
	private static String local = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		url = bdl.getString("url");
		local = bdl.getString("local");
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
			DownLoad.downFromNet(url, local);
			File file = new File(local);
			List<Item> items = CVSUtil.readCSV(file, Item.class, ';');
			if(null != items && items.size()>0){
				for(Item item:items){
					try{
						String spu_id = item.getProductModel();
						String sku_id = item.getSupplierSkuNo();
//						if(sku_id.endsWith("+")){
//							spu_id = sku_id.substring(0, sku_id.length()-2);
//						}else{
//							spu_id = sku_id.substring(0, sku_id.length()-1);
//						}
						SkuDTO sku = new SkuDTO();
						sku.setId(UUIDGenerator.getUUID());
						sku.setSupplierId(supplierId);
						sku.setSkuId(sku_id);
						sku.setSpuId(spu_id);
						sku.setProductName(item.getSopProductName()); 
						sku.setBarcode(item.getBarCode()); 
						sku.setNewMarketPrice(item.getNewMarketPrice());
						sku.setNewSalePrice(item.getNewSallPrice());
						sku.setNewSupplierPrice(item.getNewSupplierPrice());
						sku.setMarketPrice(item.getMarkerPrice());
						sku.setSalePrice(item.getSallPrice());
						sku.setSupplierPrice(item.getSupplierPrice());
						sku.setProductCode(spu_id);
						sku.setColor(item.getProductColor());
						sku.setProductDescription(item.getPcDesc());
						sku.setProductSize(item.getProductSize());
						sku.setStock(item.getStock());
						sku.setSaleCurrency(item.getCurrency());
						
						// sku入库
						try {
							if (skuDTOMap.containsKey(sku.getSkuId())) {
								skuDTOMap.remove(sku.getSkuId());
							}
							productFetchService.saveSKU(sku);

						} catch (ServiceException e) {
							try {
								if (e.getMessage().equals("数据插入失败键重复")) {
									// 更新价格和库存
									productFetchService.updatePriceAndStock(sku);
								} else {
									e.printStackTrace();
								}
							} catch (ServiceException e1) {
								e1.printStackTrace();
							}
						}
						List<String> pics = new ArrayList<String>();
						if(StringUtils.isNotBlank(item.getProductUrl1())){
							pics.add(item.getProductUrl1());
						}
						if(StringUtils.isNotBlank(item.getProductUrl2())){
							pics.add(item.getProductUrl2());
						}
						if(StringUtils.isNotBlank(item.getProductUrl3())){
							pics.add(item.getProductUrl3());
						}
						if(StringUtils.isNotBlank(item.getProductUrl4())){
							pics.add(item.getProductUrl4());
						}
						if(StringUtils.isNotBlank(item.getProductUrl5())){
							pics.add(item.getProductUrl5());
						}
						if(StringUtils.isNotBlank(item.getProductUrl6())){
							pics.add(item.getProductUrl6());
						}
						if(StringUtils.isNotBlank(item.getProductUrl7())){
							pics.add(item.getProductUrl7());
						}
						if(StringUtils.isNotBlank(item.getProductUrl8())){
							pics.add(item.getProductUrl8());
						}
						try{
							productFetchService.savePicture(supplierId,
									null, sku.getSkuId(), pics);
						}catch(Exception e){
							e.printStackTrace();
						}
						
						SpuDTO spu = new SpuDTO();
						spu.setId(UUIDGenerator.getUUID());
						spu.setSpuId(spu_id);
						spu.setSupplierId(supplierId);
						spu.setCategoryGender(item.getGender());
						spu.setCategoryName(item.getCategoryName());
						spu.setBrandName(item.getBrandName());					
						spu.setMaterial(item.getMaterial());
						spu.setSeasonName(item.getSeason());
						spu.setProductOrigin(item.getProductOrigin());					
						// spu 入库
						try {
							productFetchService.saveSPU(spu);
						} catch (ServiceException e) {
							try {
								productFetchService.updateMaterial(spu);
							} catch (ServiceException ex) {
								ex.printStackTrace();
							}

							e.printStackTrace();
						}
						
					}catch(Exception e){
						e.printStackTrace();
					}					
				}
			}
		} catch (IOException e1) {
			error.error("下载文件失败:"+e1); 
			e1.printStackTrace();
		} catch (Exception e) { 
			error.error("解析出错:"+e); 
			e.printStackTrace();
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

