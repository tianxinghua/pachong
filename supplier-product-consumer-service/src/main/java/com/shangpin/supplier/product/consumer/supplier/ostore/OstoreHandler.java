package com.shangpin.supplier.product.consumer.supplier.ostore;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.supplier.product.consumer.conf.client.mysql.sku.bean.HubSupplierSku;
import com.shangpin.supplier.product.consumer.conf.stream.sink.message.SupplierProduct;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.AtelierCommonHandler;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.IAtelierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierPrice;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierSku;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierSpu;

/**
 * * 
 * <p>Title:OstoreHandler </p>
 * <p>Description: ostore供应商原始数据处理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月8日 下午4:00:05
 *
 */
@Component("ostoreHandler")
public class OstoreHandler extends IAtelierHandler{
	
	@Autowired
	private AtelierCommonHandler atelierCommonHandler;

	@Override
	public AtelierSpu handleSpuData(String spuColumn) {
		
		return atelierCommonHandler.handleSpuData(spuColumn); 
	}

	@Override
	public AtelierSku handleSkuData(String skuColumn) {
		
		return atelierCommonHandler.handleSkuData(skuColumn); 
	}

	@Override
	public AtelierPrice handlePriceData(String priceColumn) {
		
		return atelierCommonHandler.handlePriceData(priceColumn); 
	}

	@Override
	public void setProductPrice(HubSupplierSku hubSku, AtelierSpu atelierSpu, AtelierPrice atelierPrice) {
		if("A16".equals(atelierSpu.getSeasonName())){			
			hubSku.setMarketPrice(new BigDecimal(atelierPrice.getPrice3()));
		}else{			
			hubSku.setMarketPrice(new BigDecimal(atelierPrice.getPrice2()));
		}
		hubSku.setSupplyPrice(new BigDecimal(atelierPrice.getPrice1()));
		
	}

	
}
