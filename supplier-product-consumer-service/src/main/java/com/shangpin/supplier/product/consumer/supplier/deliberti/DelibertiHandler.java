package com.shangpin.supplier.product.consumer.supplier.deliberti;

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
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerException;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerRuntimeException;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.dto.Color;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.deliberti.dto.DelibertiSkuDto;
import com.shangpin.supplier.product.consumer.supplier.deliberti.dto.DelibertiSpuDto;

import lombok.extern.slf4j.Slf4j;

@Component("delibertiHandler")
@Slf4j
public class DelibertiHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				DelibertiSpuDto delibertiSpuDto = JsonUtil.deserialize(message.getData(), DelibertiSpuDto.class);
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(message.getSupplierId(),delibertiSpuDto,hubSpu);
				if(success){
					List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
					List<DelibertiSkuDto> skus = delibertiSpuDto.getSkus();
					if(CollectionUtils.isNotEmpty(skus)){
						for(DelibertiSkuDto delibertiSkuDto : skus){
							HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
							boolean succSku = convertSku(message.getSupplierId(),delibertiSkuDto,hubSku);
							if(succSku){
								hubSkus.add(hubSku);
							}
						}
					}
					List<Image> images = converImage(delibertiSpuDto.getPictures());
					SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
		} catch (EpHubSupplierProductConsumerException e) {
			log.error("alducadaosta异常："+e.getMessage(),e);
		}
		
	}
	
	/**
	 * 
	 * @param supplierId
	 * @param delibertiSpuDto
	 * @param hubSpu
	 * @return
	 * @throws EpHubSupplierProductConsumerRuntimeException
	 */
	public boolean convertSpu(String supplierId,DelibertiSpuDto delibertiSpuDto,HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException{
		if(null == delibertiSpuDto){
			return false;
		}else{
			Color color = StringUtil.splitColor(delibertiSpuDto.getColor());
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(delibertiSpuDto.getSpuId());
			String productModel = delibertiSpuDto.getProductModel();
			if(!StringUtils.isEmpty(color.getColorCode())){
				productModel = productModel + " " + color.getColorCode();
			}
			hubSpu.setSupplierSpuModel(productModel);
			hubSpu.setSupplierSpuName(delibertiSpuDto.getSpuName());
			hubSpu.setSupplierSpuColor(color.getColorValue());
			hubSpu.setSupplierGender(delibertiSpuDto.getCategoryGender());
			hubSpu.setSupplierCategoryno(delibertiSpuDto.getCategoryId());
			hubSpu.setSupplierCategoryname(delibertiSpuDto.getCategoryName());
			hubSpu.setSupplierBrandname(delibertiSpuDto.getBrandName());
			hubSpu.setSupplierSeasonname(delibertiSpuDto.getSeasonName());
			hubSpu.setSupplierMaterial(delibertiSpuDto.getMaterial());
			hubSpu.setSupplierOrigin(delibertiSpuDto.getProductOrigin());
			return true;
		}
	}
	/**
	 * 
	 * @param supplierId
	 * @param delibertiSkuDto
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId,DelibertiSkuDto delibertiSkuDto,HubSupplierSkuDto hubSku){
		hubSku.setSupplierId(supplierId);
		hubSku.setSupplierSkuNo(delibertiSkuDto.getSkuId());
		hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(delibertiSkuDto.getMarketPrice())));
		hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(delibertiSkuDto.getSupplierPrice())));
		hubSku.setMarketPriceCurrencyorg(delibertiSkuDto.getSaleCurrency());
		hubSku.setSupplierSkuSize(delibertiSkuDto.getProductSize());
		hubSku.setStock(StringUtil.verifyStock((delibertiSkuDto.getStock())));
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
