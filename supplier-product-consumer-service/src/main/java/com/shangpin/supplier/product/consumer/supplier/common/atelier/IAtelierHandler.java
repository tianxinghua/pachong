package com.shangpin.supplier.product.consumer.supplier.common.atelier;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.shangpin.supplier.product.consumer.conf.client.mysql.sku.bean.HubSupplierSku;
import com.shangpin.supplier.product.consumer.conf.client.mysql.spu.bean.HubSupplierSpu;
import com.shangpin.supplier.product.consumer.conf.stream.sink.message.SupplierProduct;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierDate;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierPrice;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierSku;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierSpu;

/**
 * * 
 * <p>Title:IAtelierHandler </p>
 * <p>Description: 规范Atelier供应商对接的抽象类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月10日 上午11:41:03
 *
 */
@Component
public abstract class IAtelierHandler implements ISupplierHandler {

	/**
	 * 处理spu行数据，返回一个spu对象
	 * @param spuColumn spu的一条数据
	 * @return
	 */
	public abstract AtelierSpu handleSpuData(String spuColumn);
	
	/**
	 * 处理sku行数据,返回一个sku对象
	 * @param skuColumn sku的一条数据
	 * @return
	 */
	public abstract AtelierSku handleSkuData(String skuColumn);
	
	/**
	 * 处理价格行数据,返回一个价格对象
	 * @param priceColumn
	 * @return
	 */
	public abstract AtelierPrice handlePriceData(String priceColumn);
	
	/**
	 * 从atelierSpu数据中，或者atelierSku数据中找到价格信息，并且给hubSku赋值
	 * @param hubSku hub被赋值的对象
	 * @param atelierSpu atelierSpu对象
	 * @param atelierPrice atelierSku对象
	 */
	public abstract void setProductPrice(HubSupplierSku hubSku, AtelierSpu atelierSpu, AtelierPrice atelierPrice);
	
	/**
	 * atelier通用处理主流程
	 * @param message
	 * @param headers
	 */
	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers){
		if(!StringUtils.isEmpty(message.getData())){
			AtelierDate atelierDate = new Gson().fromJson(message.getData(),AtelierDate.class);
			AtelierSpu atelierSpu = handleSpuData(atelierDate.getSpu());			
			HubSupplierSpu hubSpu =  new HubSupplierSpu();
			boolean success = convertSpu(message.getSupplierId(),atelierSpu,hubSpu);
			if(success){
				//TODO 保存 hubSpu
			}
			if(null != atelierDate.getSku()){
				AtelierPrice atelierPrice = handlePriceData(atelierDate.getPrice());
				for(String skuColumn : atelierDate.getSku()){
					HubSupplierSku hubSku = new HubSupplierSku();
					AtelierSku atelierSku = handleSkuData(skuColumn);					
					boolean skuSucc = convertSku(message.getSupplierId(),hubSpu.getSupplierSpuId(),atelierSpu,atelierSku,atelierPrice,hubSku);
					if(skuSucc){
						//TODO 保存 hubSku
					}					
				}
			}
		}
		
	}
	
	/**
	 * 将Atelier对象转换成hub对象
	 * @param supplierId 供应商门户编号
	 * @param supplierSpuId hub spuid
	 * @param atelierSpu atelier spu对象
	 * @param atelierSku atelier sku对象
	 * @param atelierPrice atelier price对象
	 * @param hubSku hub sku对象
	 * @return
	 */
	public boolean convertSku(String supplierId,Long supplierSpuId, AtelierSpu atelierSpu,AtelierSku atelierSku,AtelierPrice atelierPrice, HubSupplierSku hubSku){
		if(null != atelierSku){
			
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(atelierSku.getSpuId()+"-"+atelierSku.getBarcode());
			setProductPrice(hubSku,atelierSpu,atelierPrice);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 将Atelier对象转换成hub对象
	 * @param supplierId 供应商门户编号
	 * @param atelierSpu atelier spu对象
	 * @param hubSpu hub spu对象
	 */
	public boolean convertSpu(String supplierId,AtelierSpu atelierSpu,HubSupplierSpu hubSpu){
		if(null != atelierSpu){
			
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(atelierSpu.getSpuId());
			hubSpu.setSupplierSpuModel(atelierSpu.getStyleCode()+" "+atelierSpu.getColorCode());
			hubSpu.setSupplierSpuName(atelierSpu.getCategoryName()+" "+atelierSpu.getBrandName());
			hubSpu.setSupplierSpuColor(atelierSpu.getColorName());
			hubSpu.setSupplierGender(atelierSpu.getCategoryGender());
			hubSpu.setSupplierCategoryname(atelierSpu.getCategoryName());
			hubSpu.setSupplierBrandname(atelierSpu.getBrandName());
			hubSpu.setSupplierSeasonname(atelierSpu.getSeasonName());
			hubSpu.setSupplierMaterial(atelierSpu.getMaterial1()+" "+atelierSpu.getMaterial3());
			hubSpu.setSupplierOrigin(atelierSpu.getProductOrigin());
			hubSpu.setSupplierSpuDesc(atelierSpu.getDescription()); 
			return true;
		}else{
			return false;
		}
	}
}
