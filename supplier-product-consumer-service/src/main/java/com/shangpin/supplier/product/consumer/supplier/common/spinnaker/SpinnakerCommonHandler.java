package com.shangpin.supplier.product.consumer.supplier.common.spinnaker;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.supplier.product.consumer.supplier.common.spinnaker.dto.Sku;
import com.shangpin.supplier.product.consumer.supplier.common.spinnaker.dto.Spu;

/**
 * <p>Title:SpinnakerCommonHandler </p>
 * <p>Description: spinnaker供应商一般处理逻辑类 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月10日 下午2:59:57
 *
 */
@Component
public class SpinnakerCommonHandler extends ISpinnakerHandler {

	@Override
	public boolean convertSpu(String supplierId, Spu spu, Sku sku, HubSupplierSpuDto hubSpu) {
		if(null != spu){
			
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(spu.getProduct_id()+"-"+sku.getItem_id());
			hubSpu.setSupplierSpuModel(spu.getProduct_name());
			hubSpu.setSupplierSpuName(spu.getDescription());
			hubSpu.setSupplierSpuColor(sku.getColor());
			hubSpu.setSupplierGender(spu.getType());
			hubSpu.setSupplierCategoryname(spu.getCategory());
			hubSpu.setSupplierBrandname(spu.getProducer_id());
			hubSpu.setSupplierSeasonname(spu.getSeason());
			hubSpu.setSupplierMaterial(spu.getProduct_detail());
			hubSpu.setSupplierOrigin(spu.getProduct_MadeIn());
			hubSpu.setSupplierSpuDesc(spu.getProduct_detail()); 
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean convertSku(String supplierId, Long supplierSpuId, Sku sku, HubSupplierSkuDto hubSku) {
		if(null != sku){
			
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(sku.getBarcode());
			hubSku.setMarketPrice(new BigDecimal(sku.getPrice().getMarket_price().replaceAll(",", ".")));
			hubSku.setSupplyPrice(new BigDecimal(sku.getPrice().getSuply_price().replaceAll(",", "."))); 
			hubSku.setSupplierBarcode(sku.getBarcode());
			if(sku.getItem_size().length()>4) {
				hubSku.setSupplierSkuSize(sku.getItem_size().substring(0,sku.getItem_size().length()-4));
            }else{
            	hubSku.setSupplierSkuSize(sku.getItem_size());
            }
			String stock = sku.getStock();
			if (StringUtils.isEmpty(stock)) {
				stock = "0";
			}else if (Integer.valueOf(stock) <= 0) {
				stock = "0";
			}
			hubSku.setStock(Integer.valueOf(stock)); 
			return true;
		}else{
			return false;
		}
	}

	
}
