package com.shangpin.supplier.product.consumer.supplier.zitafabiani;

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
import com.shangpin.supplier.product.consumer.supplier.zitafabiani.dto.ZitaSkuDto;
import com.shangpin.supplier.product.consumer.supplier.zitafabiani.dto.ZitaSpuDto;

import lombok.extern.slf4j.Slf4j;

@Component("zitafabianiHandler")
@Slf4j
public class ZitafabianiHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				ZitaSpuDto zitaSpuDto = JsonUtil.deserialize(message.getData(), ZitaSpuDto.class);
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(message.getSupplierId(),zitaSpuDto,hubSpu);
				if(success){
					List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
					List<ZitaSkuDto> skus = zitaSpuDto.getSkus();
					if(CollectionUtils.isNotEmpty(skus)){
						for(ZitaSkuDto zitaSkuDto : skus){
							HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
							boolean succSku = convertSku(message.getSupplierId(),zitaSkuDto,hubSku);
							if(succSku){
								hubSkus.add(hubSku);
							}
						}
					}
					List<Image> images = converImage(zitaSpuDto.getPictures());
					SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
		} catch (Exception e) {
			log.error("zitafabiani异常："+e.getMessage(),e);
		}
		
	}
	
	/**
	 * 
	 * @param supplierId
	 * @param zitaSpuDto
	 * @param hubSpu
	 * @return
	 * @throws EpHubSupplierProductConsumerRuntimeException
	 */
	public boolean convertSpu(String supplierId,ZitaSpuDto zitaSpuDto,HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException{
		if(null == zitaSpuDto){
			return false;
		}else{
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(zitaSpuDto.getSpuId());
			String productModel = zitaSpuDto.getProductModel();
			hubSpu.setSupplierSpuModel(productModel);
			hubSpu.setSupplierSpuName(zitaSpuDto.getSpuName());
			hubSpu.setSupplierSpuColor(zitaSpuDto.getColor());
			hubSpu.setSupplierGender(zitaSpuDto.getCategoryGender());
			hubSpu.setSupplierCategoryno(zitaSpuDto.getCategoryId());
			hubSpu.setSupplierCategoryname(zitaSpuDto.getCategoryName());
			hubSpu.setSupplierBrandname(zitaSpuDto.getBrandName());
			hubSpu.setSupplierSeasonname(zitaSpuDto.getSeasonName());
			hubSpu.setSupplierMaterial(zitaSpuDto.getMaterial());
			hubSpu.setSupplierOrigin(zitaSpuDto.getProductOrigin());
			return true;
		}
	}
	/**
	 * 
	 * @param supplierId
	 * @param zitaSkuDto
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId,ZitaSkuDto zitaSkuDto,HubSupplierSkuDto hubSku){
		hubSku.setSupplierId(supplierId);
		hubSku.setSupplierSkuNo(zitaSkuDto.getSkuId());
		hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(zitaSkuDto.getMarketPrice())));
		hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(zitaSkuDto.getSupplierPrice())));
		hubSku.setMarketPriceCurrencyorg(zitaSkuDto.getSaleCurrency());
		hubSku.setSupplierSkuSize(zitaSkuDto.getProductSize());
		hubSku.setStock(StringUtil.verifyStock((zitaSkuDto.getStock())));
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
