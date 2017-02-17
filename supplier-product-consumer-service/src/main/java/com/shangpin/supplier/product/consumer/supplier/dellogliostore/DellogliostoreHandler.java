package com.shangpin.supplier.product.consumer.supplier.dellogliostore;

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
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.dellogliostore.dto.DellogSkuDto;
import com.shangpin.supplier.product.consumer.supplier.dellogliostore.dto.DellogSpuDto;

import lombok.extern.slf4j.Slf4j;

@Component("dellogliostoreHandler")
@Slf4j
public class DellogliostoreHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				DellogSpuDto dellogSpuDto = JsonUtil.deserialize(message.getData(), DellogSpuDto.class);
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(message.getSupplierId(),dellogSpuDto,hubSpu);
				if(success){
					List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
					List<DellogSkuDto> skus = dellogSpuDto.getSkus();
					if(CollectionUtils.isNotEmpty(skus)){
						for(DellogSkuDto dellogSkuDto : skus){
							HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
							boolean succSku = convertSku(message.getSupplierId(),dellogSkuDto,hubSku);
							if(succSku){
								hubSkus.add(hubSku);
							}
						}
					}
					List<Image> images = converImage(dellogSpuDto.getPictures());
					SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
		} catch (EpHubSupplierProductConsumerException e) {
			log.error("dellogliostore异常："+e.getMessage(),e);
		}
	}
	
	/**
	 * 
	 * @param supplierId
	 * @param dellogSpuDto
	 * @param hubSpu
	 * @return
	 * @throws EpHubSupplierProductConsumerRuntimeException
	 */
	public boolean convertSpu(String supplierId,DellogSpuDto dellogSpuDto,HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException{
		if(null == dellogSpuDto){
			return false;
		}else{
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(dellogSpuDto.getSpuId());
			String productModel = dellogSpuDto.getProductModel();
			hubSpu.setSupplierSpuModel(productModel);
			hubSpu.setSupplierSpuName(dellogSpuDto.getSpuName());
			hubSpu.setSupplierSpuColor(dellogSpuDto.getColor());
			hubSpu.setSupplierGender(dellogSpuDto.getCategoryGender());
			hubSpu.setSupplierCategoryno(dellogSpuDto.getCategoryId());
			hubSpu.setSupplierCategoryname(dellogSpuDto.getCategoryName());
			hubSpu.setSupplierBrandname(dellogSpuDto.getBrandName());
			hubSpu.setSupplierSeasonname(dellogSpuDto.getSeasonName());
			hubSpu.setSupplierMaterial(dellogSpuDto.getMaterial());
			hubSpu.setSupplierOrigin(dellogSpuDto.getProductOrigin());
			return true;
		}
	}
	/**
	 * 
	 * @param supplierId
	 * @param dellogSkuDto
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId,DellogSkuDto dellogSkuDto,HubSupplierSkuDto hubSku){
		hubSku.setSupplierId(supplierId);
		hubSku.setSupplierSkuNo(dellogSkuDto.getSkuId());
		hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(dellogSkuDto.getMarketPrice())));
		hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(dellogSkuDto.getSupplierPrice())));
		hubSku.setMarketPriceCurrencyorg(dellogSkuDto.getSaleCurrency());
		hubSku.setSupplierSkuSize(dellogSkuDto.getProductSize());
		hubSku.setStock(StringUtil.verifyStock((dellogSkuDto.getStock())));
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
