package com.shangpin.iog.fusco.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.fusco.dao.Item;
import com.shangpin.iog.fusco.util.FtpUtil;
import com.shangpin.iog.fusco.util.ReconciliationFtpUtil;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

import java.util.*;

/**
 * Created by loyalty on 15/9/22.
 */
@Component("fusco")
public class FetchProduct {
	private static Logger logger = Logger.getLogger("info");
	public static int day;
	@Autowired
	ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;

	private static ResourceBundle bdl = ResourceBundle.getBundle("conf");

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		day = Integer.valueOf(bdl.getString("day"));
	}
    @Autowired
    com.shangpin.iog.service.OrderService productOrderService;
	public void fetchProductAndSave() {

		String supplierId = bdl.getString("supplierId");
		
		List<String> realPaths=FtpUtil.download();
		System.out.println(realPaths.size());
		List<Item> list = FtpUtil.readLocalCSV(Item.class,realPaths.get(0));
		

		Date startDate, endDate = new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate, day
				* -1, "D");
		Map<String, SkuDTO> skuDTOMap = new HashMap<String, SkuDTO>();
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(
					supplierId, startDate, endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		for (Item item : list) {
			SkuDTO sku = new SkuDTO();
			try {
				
				sku.setId(UUIDGenerator.getUUID());
				sku.setSupplierId(supplierId);
				sku.setSpuId(item.getCODICE().substring(1, item.getCODICE().length()-1));
				
				if(item.getTAGLIA().substring(1, item.getTAGLIA().length()-1).endsWith("�")){
					sku.setProductSize(item.getTAGLIA().substring(1, item.getTAGLIA().length()-1).replace("�", ".5"));
					sku.setSkuId(item.getCODICE().substring(1, item.getCODICE().length()-1)+"|"+item.getTAGLIA().substring(1, item.getTAGLIA().length()-1).replace("�", ".5"));
				}else{
					sku.setSkuId(item.getCODICE().substring(1, item.getCODICE().length()-1)+"|"+item.getTAGLIA().substring(1, item.getTAGLIA().length()-1));
					sku.setProductSize(item.getTAGLIA().substring(1, item.getTAGLIA().length()-1));
				}
					
					sku.setColor(item.getCOLORE().substring(1, item.getCOLORE().length()-1));
//					sku.setMeasurement(array[0].trim());
//				}
				sku.setMarketPrice(item.getPREZZO().substring(1, item.getPREZZO().length()-1) );
				sku.setProductDescription(item.getDESCRIZIONE().substring(1, item.getDESCRIZIONE().length()-1) );
				sku.setStock(item.getDISPO().substring(1, item.getDISPO().length()-1) );
//				sku.setProductName(item.getItemName());
				sku.setProductCode(item.getARTICOLO().substring(1, item.getARTICOLO().length()-1) );
//				sku.setSaleCurrency("USD");
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
				}
			}


			SpuDTO spu = new SpuDTO();
			try {
				spu.setId(UUIDGenerator.getUUID());
				spu.setSupplierId(supplierId);
				spu.setSpuId(item.getCODICE().substring(1, item.getCODICE().length()-1) );
				spu.setBrandName(item.getMARCA_NOME().substring(1, item.getMARCA_NOME().length()-1) );
				spu.setCategoryName(item.getTIPOLOGIA_NOME().substring(1, item.getTIPOLOGIA_NOME().length()-1) );
//				spu.setSpuName(item.getItemName());
				spu.setSeasonId(item.getSTAGIONE().substring(1, item.getSTAGIONE().length()-1) );
				
				if(!"".equals(item.getMATERIALE())){
					spu.setMaterial(item.getMATERIALE().substring(1, item.getMATERIALE().length()-1) );	
				}
				                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
				
				spu.setCategoryGender(item.getGENERE_NOME().substring(1, item.getGENERE_NOME().length()-1) );
				if(!"".equals(item.getORIGINE())){
					spu.setProductOrigin(item.getORIGINE().substring(1, item.getORIGINE().length()-1) );	
				}
				
				productFetchService.saveSPU(spu);
			} catch (ServiceException e) {
				try {
					productFetchService.updateMaterial(spu);
				} catch (ServiceException e1) {
					e1.printStackTrace();
				}
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

}
