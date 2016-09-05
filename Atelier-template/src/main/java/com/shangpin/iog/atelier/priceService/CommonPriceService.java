package com.shangpin.iog.atelier.priceService;

import org.springframework.stereotype.Component;

import com.shangpin.iog.atelier.dto.AtelierPrice;
import com.shangpin.iog.atelier.dto.AtelierSpu;
import com.shangpin.iog.atelier.service.FetchProduct;
import com.shangpin.iog.dto.SkuDTO;

/**
 * 价格通用处理类
 * @author lubaijiang
 *
 */

@Component("commonPriceService")
public class CommonPriceService  extends FetchProduct {

	@Override
	public void setProductPrice(SkuDTO sku, AtelierSpu atelierSpu,
			AtelierPrice atelierPrice) {
		
		if(null != atelierPrice){
			sku.setSupplierPrice(atelierPrice.getPrice1().replaceAll(",", "."));
			sku.setMarketPrice(atelierPrice.getPrice2().replaceAll(",", ".")); 
		}
		sku.setSalePrice("");
		
	}

}
