package com.shangpin.supplier.product.consumer.supplier.common.atelier;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierPrice;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierSku;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierSpu;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
/**
 * <p>Title:AtelierCommonHandler </p>
 * <p>Description: atelier供应商一般处理逻辑类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月8日 下午8:54:28
 *
 */
@Component
public class AtelierCommonHandler extends IAtelierHandler {
	
	@Override
	public AtelierSpu handleSpuData(String spuColumn){
		if(!StringUtils.isEmpty(spuColumn)){
			String[] spuArr = spuColumn.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
			AtelierSpu atelierSpu = new AtelierSpu();
			atelierSpu.setSpuId(spuArr[0]);
			atelierSpu.setSeasonName(spuArr[1]);
			atelierSpu.setBrandName(spuArr[2]);
			atelierSpu.setStyleCode(spuArr[3]);
			atelierSpu.setColorCode(spuArr[4]);
			atelierSpu.setCategoryGender(spuArr[5]);
			atelierSpu.setCategoryName(spuArr[8]);
			atelierSpu.setColorName(spuArr[10]);
			atelierSpu.setMaterial1(spuArr[11]);
			atelierSpu.setDescription(spuArr[15]); 
			atelierSpu.setSupplierPrice(spuArr[16]);
			atelierSpu.setMaterial3(spuArr[42]);
			atelierSpu.setProductOrigin(spuArr[40]); 
			return atelierSpu;
		}else{
			return null;
		}
	}
	
	@Override
	public AtelierSku handleSkuData(String skuColumn){
		if(!StringUtils.isEmpty(skuColumn)){
			String[] skuArr = skuColumn.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
			AtelierSku atelierSku = new AtelierSku();
			atelierSku.setSpuId(skuArr[0]);
			atelierSku.setSize(skuArr[1]);
			atelierSku.setStock(skuArr[2]);
			atelierSku.setBarcode(skuArr[5]); 
			return atelierSku;
		}else{
			return null;
		}
	}
	
	@Override
	public AtelierPrice handlePriceData(String priceColumn){
		if(!StringUtils.isEmpty(priceColumn)){
			String[] priceArr = priceColumn.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
			AtelierPrice atelierPrice = new AtelierPrice();
			atelierPrice.setPrice1(priceArr[2].replaceAll(",", "."));
			atelierPrice.setPrice2(priceArr[3].replaceAll(",", "."));
			atelierPrice.setPrice3(priceArr[4].replaceAll(",", "."));
			atelierPrice.setPrice4(priceArr[5].replaceAll(",", "."));
			atelierPrice.setPrice5(priceArr[6].replaceAll(",", "."));
			atelierPrice.setPrice6(priceArr[7].replaceAll(",", "."));
			return atelierPrice;
		}else{
			return null;
		}
	}
	
	@Override
	public void setProductPrice(HubSupplierSkuDto sku, AtelierSpu atelierSpu,
			AtelierPrice atelierPrice) {
		
		if(null != atelierPrice){
			sku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(atelierPrice.getPrice1())));
			sku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(atelierPrice.getPrice2()))); 
		}
	}
	
}
