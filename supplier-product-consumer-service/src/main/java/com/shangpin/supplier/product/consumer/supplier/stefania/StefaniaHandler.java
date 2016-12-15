package com.shangpin.supplier.product.consumer.supplier.stefania;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.shangpin.supplier.product.consumer.conf.client.mysql.sku.bean.HubSupplierSku;
import com.shangpin.supplier.product.consumer.conf.client.mysql.spu.bean.HubSupplierSpu;
import com.shangpin.supplier.product.consumer.conf.stream.sink.message.SupplierProduct;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.stefania.dto.StefItem;
import com.shangpin.supplier.product.consumer.supplier.stefania.dto.StefProduct;

/**
 * <p>Title:StefaniaHandler.java </p>
 * <p>Description: stefania供货商原始数据处理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijang
 * @date 2016年12月8日 上午11:36:22
 */
@Component("stefaniaHandler")
public class StefaniaHandler implements ISupplierHandler{
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	
	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		if(StringUtils.isEmpty(message.getData())){
			StefProduct stefProduct = new Gson().fromJson(message.getData(), StefProduct.class);
			for(StefItem stefItem :stefProduct.getItems().getItems()){
				HubSupplierSpu hubSpu = new HubSupplierSpu();
				boolean success = convertSpu(message.getSupplierId(), stefProduct, stefItem, hubSpu);
				List<HubSupplierSku> hubSkus = new ArrayList<HubSupplierSku>();
				HubSupplierSku hubSku = new HubSupplierSku();
				boolean skuSucc = convertSku(message.getSupplierId(), hubSpu.getSupplierSpuId(), stefItem, hubSku);
				if(skuSucc){
					hubSkus.add(hubSku);
				}
				if(success){
					supplierProductSaveAndSendToPending.stefaniaSaveAndSendToPending(message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus);
				}
			}
		}		
		
	}
	
	/**
	 * 将stefania原始数据转换成hub spu
	 * @param supplierId 供应商门户编号
	 * @param stefProduct stef 原始dto
	 * @param stefItem stef 原始dto
	 * @param hubSpu hub spu
	 * @return
	 */
	public boolean convertSpu(String supplierId,StefProduct stefProduct,StefItem stefItem,HubSupplierSpu hubSpu){
		if(null != stefProduct && stefItem != null){
			String productModle = findProductModleByItemId(stefItem.getItem_id());
			if(!StringUtils.isEmpty(productModle)){
				hubSpu.setSupplierId(supplierId);
				hubSpu.setSupplierSpuNo(productModle);
				hubSpu.setSupplierSpuModel(productModle);
				hubSpu.setSupplierSpuName(stefProduct.getProduct_name());
				hubSpu.setSupplierSpuColor(stefItem.getColor());
				hubSpu.setSupplierGender(stefProduct.getMain_category());
				hubSpu.setSupplierCategoryname(stefProduct.getCategory());
				hubSpu.setSupplierBrandname(stefProduct.getProduct_brand());
				hubSpu.setSupplierSeasonname(stefProduct.getSeason_code());
				hubSpu.setSupplierMaterial(stefProduct.getProduct_material());
				hubSpu.setSupplierOrigin(stefProduct.getMade_in());
				hubSpu.setSupplierSpuDesc(stefItem.getDescription());
				return true;
			}else{
				return false;
			}
			
		}else{			
			return false;
		}
	}
	
	/**
	 * 将stefania原始数据转换成hub sku
	 * @param supplierId
	 * @param supplierSpuId
	 * @param stefItem
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId,Long supplierSpuId,StefItem stefItem,HubSupplierSku hubSku){
		if(null != stefItem){
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(stefItem.getItem_id().replaceAll("½", "+"));
			hubSku.setMarketPrice(new BigDecimal(stefItem.getMarket_price()));
			hubSku.setSupplyPrice(new BigDecimal(stefItem.getSupply_price()));
			hubSku.setSalesPrice(new BigDecimal(stefItem.getSell_price()));
			hubSku.setSupplierBarcode(stefItem.getBarcode());
			hubSku.setSupplierSkuSize(stefItem.getItem_size().replaceAll("½", "+"));
			hubSku.setStock(Integer.parseInt(stefItem.getStock()));
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 根据item_id截取货号
	 * @param item_id
	 * @return
	 */
	private String findProductModleByItemId(String item_id){
		//00140-05#0422A#742######S
		if(!StringUtils.isEmpty(item_id)){
			if(item_id.contains("##")){
				return item_id.substring(0, item_id.indexOf("##"));
			}
		}
		return "";
	}
	
}
