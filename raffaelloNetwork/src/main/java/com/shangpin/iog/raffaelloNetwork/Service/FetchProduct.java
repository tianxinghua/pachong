package com.shangpin.iog.raffaelloNetwork.Service;

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
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.raffaelloNetwork.dto.Product;
import com.shangpin.iog.raffaelloNetwork.utils.Util;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("raffaelloNetwork")
public class FetchProduct {

	private static Logger logInfo = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000 * 30*60,
			1000 * 60 * 30, 1000 * 60 * 30);

	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static int day;
	private static String uri;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		uri = bdl.getString("uri");
	}
	
	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;
	
	public void fetchAndSave(){
		
		Date startDate, endDate = new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate, day
				* -1, "D");
		// 获取原有的SKU 仅仅包含价格和库存
		Map<String, SkuDTO> skuDTOMap = new HashedMap();
		try {
			skuDTOMap = productSearchService
					.findStockAndPriceOfSkuObjectMap(supplierId, startDate,
							endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		try{
			
			List<Product> items = Util.readCSV(uri, Product.class);
//			List<Product> items = Test.readTxt(uri, Product.class, '\t');
			
			if(items.size()>0){
				logInfo.info("------------------一共有"+items.size()+"条数据----------------"); 
				for(Product item :items){
					int beginIndex=item.getSize().indexOf(",");
					//保存sku	
					if(beginIndex!=-1){
					String size[] = item.getSize().split(",");
					String stock[] = item.getQuantity().split(",");
						if(size.length==stock.length){
							for (int i = 0; i < size.length; i++) {
								SkuDTO sku = new SkuDTO();
								sku.setId(UUIDGenerator.getUUID());
								sku.setSupplierId(supplierId);
								sku.setSpuId(item.getId());
								sku.setProductName(item.getTitle());
								sku.setMarketPrice(item.getProductcost());
								sku.setSupplierPrice(item.getProductcost());
								sku.setProductCode(item.getMpn());
								sku.setColor(item.getColor());
								sku.setProductDescription(item.getDescription()); 
								sku.setSaleCurrency("Euro");
								sku.setProductSize(size[i]);
								sku.setStock(stock[i]);
								sku.setSkuId(item.getMpn()+"_"+size[i]);
								sku.setBarcode(item.getId()+"_"+size[i]);
								// sku入库
								try {
									if (skuDTOMap.containsKey(sku.getSkuId())) {
										skuDTOMap.remove(sku.getSkuId());
									}
									productFetchService.saveSKU(sku);						

								} catch (ServiceException e) {
									logInfo.info("入库失败========="+sku.getSkuId());
									logInfo.info("失败原因========="+e.getMessage());
									try {
										if (e.getMessage().equals("数据插入失败键重复")) {
											// 更新价格和库存
											productFetchService.updatePriceAndStock(sku);
										} else {
											e.printStackTrace();
										}
									} catch (ServiceException e1) {
										logError.error(e1.getMessage());
										e1.printStackTrace();
									}
								}
								
								//图片
								List<String> pics = new ArrayList<String>();
								int pic1Index=item.getImage_link().indexOf(",");
								int pic2Index=item.getAdditional_image_link().indexOf(",");
								if(pic1Index!=-1){
									String img1 [] = item.getImage_link().split(",");
									for (int j = 0; j < img1.length; j++) {
										pics.add(img1 [j]);
									}
								}else{
									pics.add(item.getImage_link());
								}
								if(pic2Index!=-1){
									String img2 [] = item.getAdditional_image_link().split(",");
									for (int j = 0; j < img2.length; j++) {
										pics.add(img2[j]);
									}
								}else{
									pics.add(item.getAdditional_image_link());
								}
								productFetchService.savePicture(supplierId, null, sku.getSkuId(), pics);
							}
						}else{
							System.out.println("id为:"+item.getId());
							continue;
						}
					
					}else{
						SkuDTO sku = new SkuDTO();
						sku.setId(UUIDGenerator.getUUID());
						sku.setSupplierId(supplierId);
						sku.setSkuId(item.getMpn()+"_"+item.getSize());
						sku.setSpuId(item.getId());
						sku.setProductName(item.getTitle());
						sku.setMarketPrice(item.getProductcost());
						sku.setSupplierPrice(item.getProductcost());
						sku.setProductCode(item.getMpn());
						sku.setColor(item.getColor());
						sku.setProductDescription(item.getDescription());
						sku.setSaleCurrency("Euro");
						sku.setBarcode(item.getId()+"_"+item.getSize());
						sku.setProductSize(item.getSize());
						sku.setStock(item.getQuantity());
						
						// sku入库
						try {
							if (skuDTOMap.containsKey(sku.getSkuId())) {
								skuDTOMap.remove(sku.getSkuId());
							}
							productFetchService.saveSKU(sku);						

						} catch (ServiceException e) {
							logInfo.info("入库失败========="+sku.getSkuId());
							logInfo.info("失败原因========="+e.getMessage());
							try {
								if (e.getMessage().equals("数据插入失败键重复")) {
									// 更新价格和库存
									productFetchService.updatePriceAndStock(sku);
								} else {
									e.printStackTrace();
								}
							} catch (ServiceException e1) {
								logError.error(e1.getMessage());
								e1.printStackTrace();
							}
						}
						
						//图片
						List<String> pics = new ArrayList<String>();
						int pic1Index=item.getImage_link().indexOf(",");
						int pic2Index=item.getAdditional_image_link().indexOf(",");
						if(pic1Index!=-1){
							String img1 [] = item.getImage_link().split(",");
							for (int i = 0; i < img1.length; i++) {
								pics.add(img1 [i]);
							}
						}else{
							pics.add(item.getImage_link());
						}
						if(pic2Index!=-1){
							String img2 [] = item.getAdditional_image_link().split(",");
							for (int i = 0; i < img2.length; i++) {
								pics.add(img2[i]);
							}
						}else{
							pics.add(item.getAdditional_image_link());
						}
						productFetchService.savePicture(supplierId, null, sku.getSkuId(), pics);
						
					}
					
					//保存SPU
					SpuDTO spu = new SpuDTO();
					spu.setId(UUIDGenerator.getUUID());
					spu.setSupplierId(supplierId);
					spu.setSpuId(item.getId());
					spu.setCategoryGender(item.getGender());
					spu.setCategoryName(item.getProduct_type());
					spu.setBrandName(item.getBrand());
					spu.setMaterial(item.getMaterial());
					spu.setProductOrigin(item.getMade_in_italy());
					spu.setSeasonName(item.getCustom_label_0());
					spu.setSeasonId(item.getCustom_label_0());
					try {
						productFetchService.saveSPU(spu);
					} catch (ServiceException e) {
						logError.error(e.getMessage());
						try {
							productFetchService.updateMaterial(spu);
						} catch (ServiceException ex) {
							logError.error(ex.getMessage());
							ex.printStackTrace();
						}

						e.printStackTrace();
					}
				}
			}
			
			// 更新网站不再给信息的老数据
			for (Iterator<Map.Entry<String, SkuDTO>> itor = skuDTOMap
					.entrySet().iterator(); itor.hasNext();) {
				Map.Entry<String, SkuDTO> entry = itor.next();
				if (!"0".equals(entry.getValue().getStock())) {// 更新不为0的数据
																// 使其库存为0
					entry.getValue().setStock("0");
					try {
						productFetchService.updatePriceAndStock(entry
								.getValue());
					} catch (ServiceException e) {
						e.printStackTrace();
					}
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			logError.error(ex);
		}
		
	}
	
}
