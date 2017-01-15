package com.shangpin.supplier.product.consumer.supplier.common.atelier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerException;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierDate;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierPrice;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierSku;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierSpu;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public abstract class IAtelierHandler{
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;

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
	public abstract void setProductPrice(HubSupplierSkuDto hubSku, AtelierSpu atelierSpu, AtelierPrice atelierPrice);
	/**
	 * Atelier处理图片
	 * @param atelierImags
	 * @return
	 */
	public abstract List<Image> converImage(List<String> atelierImags);
	
	/**
	 * atelier通用处理主流程
	 * @param message
	 * @param headers
	 */
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers){
		try {
			if(!StringUtils.isEmpty(message.getData())){
				AtelierDate atelierDate = JsonUtil.deserialize(message.getData(),AtelierDate.class);
				AtelierSpu atelierSpu = handleSpuData(atelierDate.getSpu());			
				HubSupplierSpuDto hubSpu =  new HubSupplierSpuDto();
				List<Image> images = converImage(atelierDate.getImage());
				if(null == images){
					hubSpu.setIsexistpic(PicState.NO_PIC.getIndex());
				}else{
					hubSpu.setIsexistpic(PicState.PIC_INFO_COMPLETED.getIndex()); 
				}
				boolean success = convertSpu(message.getSupplierId(),atelierSpu,hubSpu);
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				if(null != atelierDate.getSku()){				
					AtelierPrice atelierPrice = handlePriceData(atelierDate.getPrice());
					for(String skuColumn : atelierDate.getSku()){
						HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
						AtelierSku atelierSku = handleSkuData(skuColumn);					
						boolean skuSucc = convertSku(message.getSupplierId(),hubSpu.getSupplierSpuId(),atelierSpu,atelierSku,atelierPrice,hubSku);
						if(skuSucc){
							hubSkus.add(hubSku);
						}					
					}
				}
				//处理图片
				SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
				if(success){
					supplierProductSaveAndSendToPending.atelierSaveAndSendToPending(message.getSupplierNo(),message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
		} catch (EpHubSupplierProductConsumerException e) {
			log.error("Atelier系统供应商 "+message.getSupplierName()+"异常："+e.getMessage(),e); 
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
	public boolean convertSku(String supplierId,Long supplierSpuId, AtelierSpu atelierSpu,AtelierSku atelierSku,AtelierPrice atelierPrice, HubSupplierSkuDto hubSku){
		if(null != atelierSku){
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuSize(atelierSku.getSize());
			hubSku.setSupplierBarcode(atelierSku.getBarcode());
			hubSku.setSupplierSkuNo(atelierSku.getSpuId()+"-"+atelierSku.getBarcode());
			setProductPrice(hubSku,atelierSpu,atelierPrice);
			hubSku.setStock(StringUtil.verifyStock(atelierSku.getStock()));
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
	public boolean convertSpu(String supplierId,AtelierSpu atelierSpu,HubSupplierSpuDto hubSpu){
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
