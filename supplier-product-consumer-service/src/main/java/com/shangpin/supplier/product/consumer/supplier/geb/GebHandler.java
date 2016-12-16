package com.shangpin.supplier.product.consumer.supplier.geb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.geb.dto.Item;
import com.shangpin.supplier.product.consumer.supplier.geb.dto.Material;

@Component("gebHandler")
public class GebHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		if(!StringUtils.isEmpty(message.getData())){
			Item item = new Gson().fromJson(message.getData(), Item.class);
			HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
			boolean success = convertSpu(message.getSupplierId(),item,hubSpu);
			List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
			HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
			boolean skuSuc = convertSku(message.getSupplierId(),hubSpu.getSupplierSpuId(),item,hubSku);
			if(skuSuc){
				hubSkus.add(hubSku);
			}
			if(success){
				supplierProductSaveAndSendToPending.gebSaveAndSendToPending(message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus);
			}
		}	
		
	}
	
	/**
	 * 将geb原始dto转换成hub spu
	 * @param supplierId 供应商门户id
	 * @param item 供应商原始dto
	 * @param hubSpu hub spu表
	 */
	public boolean convertSpu(String supplierId,Item item, HubSupplierSpuDto hubSpu){
		if(null != item){			
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(item.getProduct_id());
			hubSpu.setSupplierSpuModel(item.getProduct_reference()+" "+item.getColor_reference());
			hubSpu.setSupplierSpuName(item.getItem_intro());
			hubSpu.setSupplierSpuColor(item.getColor());
			hubSpu.setSupplierGender(item.getGender());
			hubSpu.setSupplierCategoryname(item.getSecond_category());
			hubSpu.setSupplierBrandname(item.getBrand());
			hubSpu.setSupplierSeasonname(item.getSeason_year()+item.getSeason_reference());
			List<Material> list = item.getTechnical_info();
			if(list!=null&&!list.isEmpty()){
				StringBuffer str = new StringBuffer();
				for(Material m:list){
					str.append(",").append(m.getPercentage()).append(m.getName());
				}
				hubSpu.setSupplierMaterial(str.substring(1));
			}
			hubSpu.setSupplierOrigin(item.getMade_in());
			hubSpu.setSupplierSpuDesc(item.getItem_description());
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 将geb原始dto转换成hub sku
	 * @param supplierId
	 * @param supplierSpuId hub spuid
	 * @param item
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId, Long supplierSpuId,Item item, HubSupplierSkuDto hubSku){
		if(null != item){			
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			String size = null;
			size = item.getSize();
			if(size==null){
				size = "A";
			}
			String supplierSkuNo = item.getProduct_id()+"-"+size;
			hubSku.setSupplierSkuNo(supplierSkuNo);
			hubSku.setSupplierSkuName(item.getItem_intro());
			hubSku.setMarketPrice(new BigDecimal(item.getRetail_price()));
			hubSku.setSupplyPrice(new BigDecimal(item.getPrice()));
			hubSku.setMarketPriceCurrencyorg(item.getCurrency());
			hubSku.setSupplyPriceCurrency(item.getCurrency());
			hubSku.setSupplierSkuSize(size);
			hubSku.setStock(Integer.parseInt(item.getQuantity()));
			return true;
		}else{
			return false;
		}
	}

}
