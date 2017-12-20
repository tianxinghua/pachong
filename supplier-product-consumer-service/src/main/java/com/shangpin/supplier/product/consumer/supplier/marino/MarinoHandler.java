package com.shangpin.supplier.product.consumer.supplier.marino;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.AtelierCommonHandler;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.IAtelierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierPrice;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierSku;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierSpu;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * * 
 * <p>Title:MarinoHandler </p>
 * <p>Description: ostore供应商原始数据处理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月8日 下午4:00:05
 *
 */
@Component("marinoHandler")
public class MarinoHandler extends IAtelierHandler{
	
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
	public void setProductPrice(HubSupplierSkuDto hubSku, AtelierSpu atelierSpu, AtelierPrice atelierPrice) {
		if("A16".equals(atelierSpu.getSeasonName())){			
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(atelierPrice.getPrice3())));
		}else{			
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(atelierPrice.getPrice2())));
		}
		hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(atelierPrice.getPrice1())));
		
	}

	@Override
	public List<Image> converImage(List<String> atelierImags) {
		return atelierCommonHandler.converImage(atelierImags);
	}

	
}
