package com.shangpin.supplier.product.consumer.supplier.mass;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerException;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerRuntimeException;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.geb.dto.Item;
import com.shangpin.supplier.product.consumer.supplier.geb.dto.Item_images;
import com.shangpin.supplier.product.consumer.supplier.geb.dto.Material;
import com.shangpin.supplier.product.consumer.supplier.mass.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("massHandler")
@Slf4j
public class MassHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;
	
	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				ProductDTO item = JsonUtil.deserialize(message.getData(), ProductDTO.class);
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(message.getSupplierId(),item,hubSpu);
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				boolean skuSuc = convertSku(message.getSupplierId(),hubSpu.getSupplierSpuId(),item,hubSku);
				if(skuSuc){
					hubSkus.add(hubSku);
				}
				//处理图片				
				SupplierPicture supplierPicture = null;

				supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, converImage(item.getSpuPicture()));

				if(success){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}	
		} catch (EpHubSupplierProductConsumerException e) {
			log.error("mass异常："+e.getMessage(),e);
		}
		
	}
	
	/**
	 * mass处理图片
	 * @param imgUrl
	 * @return
	 */
	private List<Image> converImage(String imgUrl){
		List<Image> images = new ArrayList<Image>();
		if(org.apache.commons.lang.StringUtils.isNotBlank(imgUrl)){
			String[] imageSpuUrlArray = imgUrl.split("\\|\\|");
			if(null!=imageSpuUrlArray&&imageSpuUrlArray.length>0){

				for(String url : imageSpuUrlArray){
					Image image = new Image();
					image.setUrl(url);
					images.add(image);
				}
			}
		}


		return images;
	}
	
	/**
	 * 将geb原始dto转换成hub spu
	 * @param supplierId 供应商门户id
	 * @param item 供应商原始dto
	 * @param hubSpu hub spu表
	 */
	public boolean convertSpu(String supplierId,ProductDTO item, HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(item.getSpuId());
			hubSpu.setSupplierSpuModel(item.getProductCode());
			hubSpu.setSupplierSpuName(item.getProductName());
			hubSpu.setSupplierSpuColor(item.getColor());
			hubSpu.setSupplierGender(item.getCategoryGender());
			hubSpu.setSupplierCategoryname(item.getCategoryName());
			hubSpu.setSupplierBrandname(item.getBrandName());
			hubSpu.setSupplierSeasonname(item.getSeasonName());
			hubSpu.setSupplierMaterial(item.getMaterial());
			hubSpu.setSupplierOrigin(item.getProductOrigin());
			hubSpu.setSupplierSpuDesc(item.getProductDescription());
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
	public boolean convertSku(String supplierId, Long supplierSpuId,ProductDTO item, HubSupplierSkuDto hubSku) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			String size = null;
			size = item.getSize();
			if(size==null){
				size = "A";
			}
			String supplierSkuNo = item.getSkuId();
			hubSku.setSupplierSkuNo(supplierSkuNo);
			hubSku.setSupplierSkuName(item.getProductName());
			hubSku.setSupplierBarcode(supplierSkuNo);
			hubSku.setMarketPrice(item.getMarketPrice());
			hubSku.setSupplyPrice(item.getSupplierPrice());
			hubSku.setMarketPriceCurrencyorg(item.getSaleCurrency());
			hubSku.setSupplyPriceCurrency(item.getSaleCurrency());
			hubSku.setSupplierSkuSize(size);
			hubSku.setStock(StringUtil.verifyStock((item.getStock().toString())));
			return true;
		}else{
			return false;
		}
	}

}
