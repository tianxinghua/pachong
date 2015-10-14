package com.shangpin.iog.price.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsPriceService;
import com.shangpin.iog.dto.PriceCountDetailDTO;
import com.shangpin.iog.service.PriceCountDetailService;
import com.shangpin.iog.service.SkuPriceService;
public class UpdatePriceService extends AbsPriceService{

	
	@Autowired
	PriceCountDetailService priceCountDetailService;
	@Autowired
	SkuPriceService skuPriceService;
	/**
	 * @param skuNo 线上需要更新的sku
	 * @return Map  最新的Map<skuno,marketPrice|supplierPrice>
	 */
	@Override
	public Map<String, String> getPriceOfSupplier(Collection<String> skuNo)
			throws ServiceException, Exception {
		Map<String, String> returnMap = new HashMap<String,String>();
		//可用的供应商
		List<PriceCountDetailDTO> list = priceCountDetailService.findAllByFlag("1");
		for (PriceCountDetailDTO priceCountDetailDTO : list) {
			//得到数据库中的最新价格
			Map<String, Map<String, String>> newSkuPrice = skuPriceService.getNewSkuPrice(priceCountDetailDTO.getSupplierId());
			Map<String, String> skuIdPrice = newSkuPrice.get(priceCountDetailDTO.getSupplierId());
			//放到map中
			for(String skuId : skuNo){
				if (skuIdPrice.get(skuId)!=null) {
					returnMap.put(skuId, skuIdPrice.get(skuId));
				}
			}
		}
		return returnMap;
	}

}
