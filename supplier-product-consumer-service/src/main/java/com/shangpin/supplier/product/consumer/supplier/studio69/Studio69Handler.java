package com.shangpin.supplier.product.consumer.supplier.studio69;

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
import com.shangpin.supplier.product.consumer.supplier.studio69.dto.StudioSkuDto;
import com.shangpin.supplier.product.consumer.supplier.studio69.dto.StudioSpuDto;

import lombok.extern.slf4j.Slf4j;
/**
 * 供应商studio69消费者
 * <p>Title: Studio69Handler</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年2月24日 上午10:12:02
 *
 */
@Component("studio69Handler")
@Slf4j
public class Studio69Handler implements ISupplierHandler{
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				StudioSpuDto studioSpuDto = JsonUtil.deserialize(message.getData(), StudioSpuDto.class);
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(message.getSupplierId(),studioSpuDto,hubSpu);
				if(success){
					List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
					List<StudioSkuDto> skus = studioSpuDto.getSkus();
					if(CollectionUtils.isNotEmpty(skus)){
						for(StudioSkuDto studioSkuDto : skus){
							HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
							boolean succSku = convertSku(message.getSupplierId(),studioSkuDto,hubSku);
							if(succSku){
								hubSkus.add(hubSku);
							}
						}
					}
					List<Image> images = converImage(studioSpuDto.getPictures());
					SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
		} catch (EpHubSupplierProductConsumerException e) {
			log.error("studio69异常："+e.getMessage(),e);
		}
		
	}
	/**
	 * 
	 * @param supplierId
	 * @param studioSpuDto
	 * @param hubSpu
	 * @return
	 * @throws EpHubSupplierProductConsumerRuntimeException
	 */
	public boolean convertSpu(String supplierId,StudioSpuDto studioSpuDto,HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException{
		if(null == studioSpuDto){
			return false;
		}else{
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(studioSpuDto.getSpuId());
			hubSpu.setSupplierSpuModel(studioSpuDto.getProductModel());
			hubSpu.setSupplierSpuName(studioSpuDto.getSpuName());
			hubSpu.setSupplierSpuColor(studioSpuDto.getColor());
			hubSpu.setSupplierGender(studioSpuDto.getCategoryGender());
			hubSpu.setSupplierCategoryno(studioSpuDto.getCategoryId());
			hubSpu.setSupplierCategoryname(studioSpuDto.getCategoryName());
			hubSpu.setSupplierBrandname(studioSpuDto.getBrandName());
			hubSpu.setSupplierSeasonname(studioSpuDto.getSeasonName());
			hubSpu.setSupplierMaterial(studioSpuDto.getMaterial());
			hubSpu.setSupplierOrigin(studioSpuDto.getProductOrigin());
			return true;
		}
	}
	/**
	 * 
	 * @param supplierId
	 * @param studioSkuDto
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId,StudioSkuDto studioSkuDto,HubSupplierSkuDto hubSku){
		hubSku.setSupplierId(supplierId);
		hubSku.setSupplierSkuNo(studioSkuDto.getSkuId());
		hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(studioSkuDto.getMarketPrice())));
		hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(studioSkuDto.getSupplierPrice())));
		hubSku.setMarketPriceCurrencyorg(studioSkuDto.getSaleCurrency());
		hubSku.setSupplierSkuSize(studioSkuDto.getProductSize());
		hubSku.setStock(StringUtil.verifyStock((studioSkuDto.getStock())));
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
