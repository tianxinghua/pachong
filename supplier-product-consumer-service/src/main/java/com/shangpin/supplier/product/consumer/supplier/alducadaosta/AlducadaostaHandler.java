package com.shangpin.supplier.product.consumer.supplier.alducadaosta;

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
import com.shangpin.supplier.product.consumer.supplier.alducadaosta.dto.AlducaSkuDto;
import com.shangpin.supplier.product.consumer.supplier.alducadaosta.dto.AlducaSpuDto;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: AlducadaostaHandler</p>
 * <p>Description: 供应商alducadaosta处理</p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年2月15日 下午4:14:20
 *
 */
@Component("alducadaostaHandler")
@Slf4j
public class AlducadaostaHandler implements ISupplierHandler{
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				AlducaSpuDto alducaSpuDto = JsonUtil.deserialize(message.getData(), AlducaSpuDto.class);
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(message.getSupplierId(),alducaSpuDto,hubSpu);
				if(success){
					List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
					List<AlducaSkuDto> skus = alducaSpuDto.getSkus();
					if(CollectionUtils.isNotEmpty(skus)){
						for(AlducaSkuDto alducaSkuDto : skus){
							HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
							boolean succSku = convertSku(message.getSupplierId(),alducaSkuDto,hubSku);
							if(succSku){
								hubSkus.add(hubSku);
							}
						}
					}
					List<Image> images = converImage(alducaSpuDto.getPictures());
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
	 * @param alducaSpuDto
	 * @param hubSpu
	 * @return
	 * @throws EpHubSupplierProductConsumerRuntimeException
	 */
	public boolean convertSpu(String supplierId,AlducaSpuDto alducaSpuDto,HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException{
		if(null == alducaSpuDto){
			return false;
		}else{
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(alducaSpuDto.getSpuId());
			hubSpu.setSupplierSpuModel(alducaSpuDto.getProductModel());
			hubSpu.setSupplierSpuName(alducaSpuDto.getSpuName());
			hubSpu.setSupplierSpuColor(alducaSpuDto.getColor());
			hubSpu.setSupplierGender(alducaSpuDto.getCategoryGender());
			hubSpu.setSupplierCategoryno(alducaSpuDto.getCategoryId());
			hubSpu.setSupplierCategoryname(alducaSpuDto.getCategoryName());
			hubSpu.setSupplierBrandname(alducaSpuDto.getBrandName());
			hubSpu.setSupplierSeasonname(alducaSpuDto.getSeasonName());
			hubSpu.setSupplierMaterial(alducaSpuDto.getMaterial());
			hubSpu.setSupplierOrigin(alducaSpuDto.getProductOrigin());
			return true;
		}
	}
	/**
	 * 
	 * @param supplierId
	 * @param alducaSkuDto
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId,AlducaSkuDto alducaSkuDto,HubSupplierSkuDto hubSku){
		hubSku.setSupplierId(supplierId);
		hubSku.setSupplierSkuNo(alducaSkuDto.getSkuId());
		hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(alducaSkuDto.getMarketPrice())));
		hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(alducaSkuDto.getSupplierPrice())));
		hubSku.setMarketPriceCurrencyorg(alducaSkuDto.getSaleCurrency());
		hubSku.setSupplierSkuSize(alducaSkuDto.getProductSize());
		hubSku.setStock(StringUtil.verifyStock((alducaSkuDto.getStock())));
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
