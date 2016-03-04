package com.shangpin.iog.ctsiLogistics.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.ctsiLogistics.dao.Item;
import com.shangpin.iog.ctsiLogistics.util.ReconciliationFtpUtil;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
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
@Component("ctsilog")
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

	public void fetchProductAndSave() {

		String supplierId = bdl.getString("supplierId");
		List<Item> list = null;
		try {
			list = ReconciliationFtpUtil.readLocalCSV(Item.class);;
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

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

			String[] array = item.getDescription().split("\\|");
			if(array.length!=3){
				array = item.getDescription().split("-");
			}
			SkuDTO sku = new SkuDTO();
			try {
				sku.setId(UUIDGenerator.getUUID());
				sku.setSupplierId(supplierId);
				sku.setSpuId(item.getSku());
				sku.setSkuId(item.getSku());
				sku.setProductSize(array[2].trim());
				sku.setMarketPrice(item.getPrice());
				sku.setProductDescription(item.getDescription());
				sku.setStock(item.getQty());
				sku.setMeasurement(array[0].trim());
				sku.setProductName(item.getItemName());
				sku.setColor(array[1].trim());
				sku.setSaleCurrency("USD");
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

			if (StringUtils.isNotBlank(item.getPhoto())) {
				String [] picArray = item.getPhoto().split("\\|");
				productFetchService.savePicture(supplierId, null,
						item.getSku(), Arrays.asList(picArray));
			}

			SpuDTO spu = new SpuDTO();
			try {
				spu.setId(UUIDGenerator.getUUID());
				spu.setSupplierId(supplierId);
				spu.setSpuId(item.getSku());
				spu.setBrandName(item.getBrand());
				spu.setCategoryName(array[0].trim());
				spu.setSpuName(item.getItemName());
				spu.setSeasonId(item.getSeason());
				spu.setMaterial(item.getMaterial());
				spu.setCategoryGender(item.getGender());
				spu.setProductOrigin(item.getProductOrigin());
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
