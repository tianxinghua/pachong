package com.shangpin.iog.mario;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.shangpin.iog.mario.dto.Item;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("mario")
public class FetchProduct {

	private static Logger logInfo = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000 * 5*60,
			1000 * 60 * 5, 1000 * 60 * 5);

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
			
			List<Item> items = CVSUtil.readCSV(uri, Item.class, ';');
			if(items.size()>0){
				logInfo.info("------------------一共有"+items.size()+"条数据----------------"); 
				for(Item item :items){
					String des = item.getDescrizione_lunga().replaceAll(",", " "); 
					String[] desArr = des.split("\\s+");
					List<String> list = Arrays.asList(desArr);
					String material = "";
					String color = "";
					try{
						
						material = list.get(list.indexOf("IN")+1);
						color = list.get(list.indexOf("IN")+2);
						if(color.equals("DI") || color.equals("GG")){
							color = list.get(list.indexOf("IN")+4);
						}
						
					}catch(Exception ex){
						logError.error("IN======="+ex);
					}
					//保存sku					
					SkuDTO sku = new SkuDTO();
					sku.setId(UUIDGenerator.getUUID());
					sku.setSupplierId(supplierId);
					sku.setSkuId(item.getCodice_Prodotto()+"-"+item.getNumero());
					sku.setSpuId(item.getCodice_Prodotto());
					sku.setProductName(item.getNome_del_prodotto());
					sku.setMarketPrice(item.getMarket_price());
					sku.setSupplierPrice(item.getPrezzo());
					sku.setProductCode(item.getCodice_Prodotto());
					sku.setColor(color);
					sku.setProductDescription(des); 
					sku.setSaleCurrency(item.getValuta());
					sku.setProductSize(item.getSize_Tipe()+" "+item.getSize());
					sku.setStock(item.getStock());
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
					pics.add(item.getImage_URL_1());
					pics.add(item.getImage_URL_2());
					pics.add(item.getImage_URL_3());
					productFetchService.savePicture(supplierId, null, sku.getSkuId(), pics);
					//spu ru ku
					SpuDTO spu = new SpuDTO();
					spu.setId(UUIDGenerator.getUUID());
					spu.setSupplierId(supplierId);
					spu.setSpuId(item.getCodice_Prodotto());
					spu.setCategoryGender(item.getGender());
					spu.setCategoryName(item.getCategoria());
					spu.setSeasonId(item.getStagione());
					spu.setBrandName(item.getMarca());
					spu.setMaterial(material);
					//spu.setProductOrigin(item.);
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
