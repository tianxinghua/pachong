package com.shangpin.supplier.product.consumer.supplier.piccadilly;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerRuntimeException;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.piccadilly.dto.PiccadillySkuDto;
import com.shangpin.supplier.product.consumer.supplier.piccadilly.dto.PiccadillySpuDto;

import lombok.extern.slf4j.Slf4j;

@Component("piccadillyHandler")
@Slf4j
public class PiccadillyHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				PiccadillySpuDto piccadillySpuDto = JsonUtil.deserialize(message.getData(), PiccadillySpuDto.class);
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(message.getSupplierId(),piccadillySpuDto,hubSpu);
				if(success){
					List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
					List<PiccadillySkuDto> skus = piccadillySpuDto.getSkus();
					if(CollectionUtils.isNotEmpty(skus)){
						for(PiccadillySkuDto piccadillySkuDto : skus){
							HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
							boolean succSku = convertSku(message.getSupplierId(),piccadillySkuDto,hubSku);
							if(succSku){
								hubSkus.add(hubSku);
							}
						}
					}
					List<Image> images = converImage(piccadillySpuDto.getPictures());
					SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
		} catch (Exception e) {
			log.error("piccadilly异常："+e.getMessage(),e);
		}
		
	}
	
	/**
	 * 
	 * @param supplierId
	 * @param piccadillySpuDto
	 * @param hubSpu
	 * @return
	 * @throws EpHubSupplierProductConsumerRuntimeException
	 */
	public boolean convertSpu(String supplierId,PiccadillySpuDto piccadillySpuDto,HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException{
		if(null == piccadillySpuDto){
			return false;
		}else{
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(piccadillySpuDto.getSpuId());
			String productModel = piccadillySpuDto.getProductModel();
			hubSpu.setSupplierSpuModel(productModel);
			hubSpu.setSupplierSpuName(piccadillySpuDto.getSpuName());
			hubSpu.setSupplierSpuColor(piccadillySpuDto.getColor());
			hubSpu.setSupplierGender(piccadillySpuDto.getCategoryGender());
			hubSpu.setSupplierCategoryno(piccadillySpuDto.getCategoryId());
			hubSpu.setSupplierCategoryname(piccadillySpuDto.getCategoryName());
			hubSpu.setSupplierBrandname(piccadillySpuDto.getBrandName());
			hubSpu.setSupplierSeasonname(piccadillySpuDto.getSeasonName());
			hubSpu.setSupplierMaterial(piccadillySpuDto.getMaterial());
			hubSpu.setSupplierOrigin(piccadillySpuDto.getProductOrigin());
			return true;
		}
	}
	/**
	 * 
	 * @param supplierId
	 * @param piccadillySkuDto
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId,PiccadillySkuDto piccadillySkuDto,HubSupplierSkuDto hubSku){
		hubSku.setSupplierId(supplierId);
		hubSku.setSupplierSkuNo(piccadillySkuDto.getSkuId());
		hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(piccadillySkuDto.getMarketPrice())));
		hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(piccadillySkuDto.getSupplierPrice())));
		hubSku.setMarketPriceCurrencyorg(piccadillySkuDto.getSaleCurrency());
		hubSku.setSupplierSkuSize(piccadillySkuDto.getProductSize());
		hubSku.setStock(StringUtil.verifyStock((piccadillySkuDto.getStock())));
		return true;
	}
	
	private List<Image> converImage(List<String> pictures){
		List<Image> images = new ArrayList<Image>();
		if(CollectionUtils.isNotEmpty(pictures)){			
			for(String url : pictures){
				Image image = new Image();
				image.setUrl(url);
				images.add(image);
			}
		}
		return images;
	}

}
