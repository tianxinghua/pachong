package com.shangpin.supplier.product.consumer.supplier.pritelli;

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
import com.shangpin.supplier.product.consumer.supplier.pritelli.dto.PritelliSkuDto;
import com.shangpin.supplier.product.consumer.supplier.pritelli.dto.PritelliSpuDto;

import lombok.extern.slf4j.Slf4j;

@Component("pritelliHandler")
@Slf4j
public class PritelliHandler implements ISupplierHandler{
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				PritelliSpuDto pritelliSpuDto = JsonUtil.deserialize(message.getData(), PritelliSpuDto.class);
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(message.getSupplierId(),pritelliSpuDto,hubSpu);
				if(success){
					List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
					List<PritelliSkuDto> skus = pritelliSpuDto.getSkus();
					if(CollectionUtils.isNotEmpty(skus)){
						for(PritelliSkuDto pritelliSkuDto : skus){
							HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
							boolean succSku = convertSku(message.getSupplierId(),pritelliSkuDto,hubSku);
							if(succSku){
								hubSkus.add(hubSku);
							}
						}
					}
					List<Image> images = converImage(pritelliSpuDto.getPictures());
					SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
		} catch (EpHubSupplierProductConsumerException e) {
			log.error("pritelli异常："+e.getMessage(),e);
		}
		
	}
	
	/**
	 * 
	 * @param supplierId
	 * @param pritelliSpuDto
	 * @param hubSpu
	 * @return
	 * @throws EpHubSupplierProductConsumerRuntimeException
	 */
	public boolean convertSpu(String supplierId,PritelliSpuDto pritelliSpuDto,HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException{
		if(null == pritelliSpuDto){
			return false;
		}else{
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(pritelliSpuDto.getSpuId());
			String productModel = pritelliSpuDto.getProductModel();
			hubSpu.setSupplierSpuModel(productModel);
			hubSpu.setSupplierSpuName(pritelliSpuDto.getSpuName());
			hubSpu.setSupplierSpuColor(pritelliSpuDto.getColor());
			hubSpu.setSupplierGender(pritelliSpuDto.getCategoryGender());
			hubSpu.setSupplierCategoryno(pritelliSpuDto.getCategoryId());
			hubSpu.setSupplierCategoryname(pritelliSpuDto.getCategoryName());
			hubSpu.setSupplierBrandname(pritelliSpuDto.getBrandName());
			hubSpu.setSupplierSeasonname(pritelliSpuDto.getSeasonName());
			hubSpu.setSupplierMaterial(pritelliSpuDto.getMaterial());
			hubSpu.setSupplierOrigin(pritelliSpuDto.getProductOrigin());
			return true;
		}
	}
	/**
	 * 
	 * @param supplierId
	 * @param pritelliSkuDto
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId,PritelliSkuDto pritelliSkuDto,HubSupplierSkuDto hubSku){
		hubSku.setSupplierId(supplierId);
		hubSku.setSupplierSkuNo(pritelliSkuDto.getSkuId());
		hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(pritelliSkuDto.getMarketPrice())));
		hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(pritelliSkuDto.getSupplierPrice())));
		hubSku.setMarketPriceCurrencyorg(pritelliSkuDto.getSaleCurrency());
		hubSku.setSupplierSkuSize(pritelliSkuDto.getProductSize());
		hubSku.setStock(StringUtil.verifyStock((pritelliSkuDto.getStock())));
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
