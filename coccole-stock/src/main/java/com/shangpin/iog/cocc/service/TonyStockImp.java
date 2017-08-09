package com.shangpin.iog.cocc.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.ice.ice.UpdateProductSock;
import com.shangpin.iog.cocc.dto.Item;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuRelationDTO;
import com.shangpin.iog.dto.SupplierStockDTO;
import com.shangpin.iog.service.SkuRelationService;
import com.shangpin.iog.service.SupplierStockService;
import com.shangpin.iog.service.UpdateStockService;

/**
 * Created by wangyuzhi on 2015/9/14.
 */
@Service("tonyfetch")
public class TonyStockImp {
	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");

	OutTimeConfig outTimeConfig = new OutTimeConfig(1000 * 60, 1000 * 60,
			1000 * 60);

	public static ResourceBundle bundle = ResourceBundle.getBundle("conf");
	public static String SUPPLIER_ID = bundle.getString("supplierId");
	public static String url = bundle.getString("url");
	@Autowired
	SupplierStockService supplierStockService;
	@Autowired
	SkuRelationService skuRelationService;
	@Autowired
	UpdateStockService updateStockService;

	public void fetchStock() {

		int i = 0;
		Gson gson = new Gson();
		Map<String, Integer> stockMap = new HashMap<>();
		String json = HttpUtil45.get(url+"/tabella?nome=inventory_day", new OutTimeConfig(1000 * 60 * 2,
				1000 * 60 * 20, 1000 * 60 * 20), null);
		List<Item> list = gson.fromJson(json, new TypeToken<List<Item>>() {
		}.getType());
		for (Item item : list) {
			System.out.println(item.toString());
			try {
				stockMap.put(item.getKEY(), Integer.parseInt(item.getDISPO()));
			} catch (Exception e) {
				i++;
			}
		}

		for (Iterator<Map.Entry<String, Integer>> itor = stockMap.entrySet()
				.iterator(); itor.hasNext();) {
			Map.Entry<String, Integer> entry = itor.next();
			SupplierStockDTO supplierStockDTO = new SupplierStockDTO();
			supplierStockDTO.setSupplierId(SUPPLIER_ID);
			supplierStockDTO.setId(UUIDGenerator.getUUID());
			supplierStockDTO.setSupplierSkuId(entry.getKey());
			supplierStockDTO.setQuantity(entry.getValue());
			supplierStockDTO.setOptTime(new Date());

			try {
				supplierStockService.saveStock(supplierStockDTO);

			} catch (ServiceMessageException e) {
				try {
					if (e.getMessage().equals("数据插入失败键重复")) {
						// update
						supplierStockService.updateStock(supplierStockDTO);
						logger.info(supplierStockDTO.getSupplierSkuId()
								+ " : 更新库存" + supplierStockDTO.getQuantity());
					} else {
						e.printStackTrace();
					}

				} catch (ServiceException e1) {
					e1.printStackTrace();
				}
			}
		}
		// 实时更新库存
		if (stockMap.size() > 0) {
			this.updateSOPInventory(stockMap);
		}
		try {
			updateStockService.updateTime(SUPPLIER_ID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("fail:"+i);
	}

	/**
	 * 更新库存数量
	 */
	private void updateSOPInventory(Map<String, Integer> stockMap) {

		try {
			UpdateProductSock updateProductSock = new UpdateProductSock();
			Map<String, String> skuRelationMap = new HashMap<>();
			for (Iterator<String> itor = stockMap.keySet().iterator(); itor
					.hasNext();) {
				try {
					SkuRelationDTO skuRelationDTO = skuRelationService
							.getSkuRelationBySupplierIdAndSupplierSkuNo(
									SUPPLIER_ID, itor.next());
					if (skuRelationDTO != null) {
						skuRelationMap.put(skuRelationDTO.getSupplierSkuId(),
								skuRelationDTO.getSopSkuId());
					}

				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}

			updateProductSock
					.updateStock(stockMap, skuRelationMap, SUPPLIER_ID);
		} catch (Exception e) {
			loggerError.error("时时同步库存失败");
			e.printStackTrace();
		}
	}
}
