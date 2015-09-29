package com.shangpin.iog.fashionesta.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.fashionesta.dto.Item;
import com.shangpin.iog.fashionesta.dto.Product;
import com.shangpin.iog.fashionesta.utils.DownloadAndReadCSV;
import com.shangpin.iog.service.ProductFetchService;

@Component("fashionesta")
public class FetchProduct {

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static Logger logMongo = Logger.getLogger("mongodb");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
	}
	@Autowired
	private ProductFetchService productFetchService;

	public void fetchProductAndSave() {
		//更改状态存储，不要忘了填币种
		try {
			Map<String, String> mongMap = new HashMap<>();
			OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
			timeConfig.confRequestOutTime(360000);
			timeConfig.confSocketOutTime(360000);
			String result = HttpUtil45.get(url, timeConfig, null);
			mongMap.put("supplierId", supplierId);
			mongMap.put("supplierName", "galiano");
			mongMap.put("result", result);
			logMongo.info(mongMap);
			List<Product> list = DownloadAndReadCSV.readLocalCSV();
			for (Product product : list) {
				SpuDTO spu = new SpuDTO();

				List<Item> items = product.getItems();

				if (items.size() == 0) {
					continue;
				}
				for (Item item : items) {
					SkuDTO sku = new SkuDTO();

					try {
						sku.setId(UUIDGenerator.getUUID());
						sku.setSkuId(item.getItemCode());
						sku.setSupplierId(supplierId);
						sku.setSpuId(product.getProductCode());

						sku.setColor(product.getColor());
						sku.setSaleCurrency("Euro");
						sku.setMarketPrice(item.getPrice());
						sku.setSupplierPrice(item.getSpecial_price());
						sku.setStock(item.getStock());
						
						String size = item.getSize();
						if(size.indexOf("1/2")>0){
		                    size=size.replace("-1/2","+");
		                }
						if (size.equals("-")) {
							size = "UNIQUE";
						}
						sku.setProductSize(size);
						sku.setProductName(product.getProductName());
						productFetchService.saveSKU(sku);

						ProductPictureDTO picture = new ProductPictureDTO();
						picture.setSupplierId(supplierId);
						picture.setId(UUIDGenerator.getUUID());
						picture.setPicUrl(product.getImage_url());
						picture.setSkuId(item.getItemCode());
						picture.setSpuId(product.getProductCode());
						try {
							productFetchService.savePictureForMongo(picture);
						} catch (ServiceException e) {
							e.printStackTrace();
						}

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

				}

				try {
					spu.setId(UUIDGenerator.getUUID());
					spu.setSpuId(product.getProductCode());
					spu.setSupplierId(supplierId);
					spu.setBrandName(product.getBrand());
					spu.setCategoryName(product.getCategory());
					spu.setCategoryGender(product.getGender());
					spu.setMaterial(product.getMaterial());
					spu.setPicUrl(product.getImage_url());
					spu.setProductOrigin(product.getMade());
					productFetchService.saveSPU(spu);
				} catch (ServiceException e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HttpUtil45.closePool();
		}
	}
}
