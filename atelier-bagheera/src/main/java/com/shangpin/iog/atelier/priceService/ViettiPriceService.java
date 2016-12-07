package com.shangpin.iog.atelier.priceService;

import org.springframework.stereotype.Component;

import com.shangpin.iog.atelier.dto.AtelierPrice;
import com.shangpin.iog.atelier.dto.AtelierSpu;
import com.shangpin.iog.atelier.service.FetchProduct;
import com.shangpin.iog.dto.SkuDTO;

@Component("viettiPriceService")
public class ViettiPriceService extends FetchProduct {

	@Override
	public void setProductPrice(SkuDTO sku, AtelierSpu atelierSpu,
			AtelierPrice atelierPrice) {
		
		if(null != atelierSpu){
			sku.setMarketPrice(atelierSpu.getSupplierPrice());
		}
		if(null != atelierPrice){
			sku.setSupplierPrice(atelierPrice.getPrice1().replaceAll(",", "."));
		}
		sku.setSalePrice("");
	}

}
