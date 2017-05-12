package com.shangpin.supplier.product.consumer.supplier.emonti;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerRuntimeException;
import com.shangpin.supplier.product.consumer.service.SupplierProductMongoService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.emonti.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("emontiHandler")
@Slf4j
public class EmontiHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;
	@Autowired
	private SupplierProductMongoService mongoService;
	
	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				ProductDTO item = JsonUtil.deserialize(message.getData(), ProductDTO.class);
				String supplierId = message.getSupplierId();
				
				mongoService.save(supplierId, item.getSpuId(), item);
				
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				boolean success = convertSpu(supplierId,item,hubSpu);
				if(success){
					HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
					boolean succSku = convertSku(supplierId,item,hubSku);
					if(succSku){
						hubSkus.add(hubSku);
					}

				}
                 //处理图片
//				SupplierPicture supplierPicture = null;
//				if(pictureHandler.isCurrentSeason(supplierId, hubSpu.getSupplierSeasonname())){
				SupplierPicture	supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, converImage(item));
//				}
				if(success){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}	
		} catch (Exception e) {
			log.error("della异常："+e.getMessage(),e);
		}
		
	}
	
	/**
	 * 处理图片
	 * @param item
	 * @return
	 */
	private List<Image> converImage(ProductDTO item){

		List<Image> images = new ArrayList<Image>();


		if(!StringUtils.isEmpty(item.getImageUrls())){
			for(String url : item.getImageUrls()){
				Image image = new Image();
				image.setUrl(url);
				images.add(image);
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
			hubSpu.setSupplierSpuNo(item.getSpuId().trim());
			hubSpu.setSupplierSpuModel(item.getProductCode().trim());
			hubSpu.setSupplierSpuColor(item.getColor());
			hubSpu.setSupplierGender(item.getCategoryGender());
			hubSpu.setSupplierCategoryname(item.getCategoryName());
			hubSpu.setSupplierBrandname(item.getBrandName());
			hubSpu.setSupplierSeasonname(item.getSeasonName());

			hubSpu.setSupplierMaterial(item.getMaterial());
			hubSpu.setSupplierOrigin(item.getProductOrigin());
			hubSpu.setSupplierSpuDesc(item.getProductDescription());
			hubSpu.setSupplierSpuName(item.getSpuName());
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 将geb原始dto转换成hub sku
	 * @param supplierId   	 *
	 * @param item
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId,ProductDTO item, HubSupplierSkuDto hubSku) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(item.getProductSkuDTO().getSkuId().trim());
			hubSku.setSupplierBarcode(item.getProductSkuDTO().getBarcode());
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(item.getProductSkuDTO().getMarketPrice())));
			hubSku.setMarketPriceCurrencyorg(item.getProductSkuDTO().getSaleCurrency());
			hubSku.setSalesPrice(new BigDecimal(StringUtil.verifyPrice(item.getProductSkuDTO().getSalePrice())));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(item.getProductSkuDTO().getSupplierPrice())));
			hubSku.setSupplierSkuSize(item.getProductSkuDTO().getProductSize());
			hubSku.setStock(StringUtil.verifyStock(item.getProductSkuDTO().getStock()));
			return true;
		}else{
			return false;
		}
	}

	private static String standard(String origine){
		if(!StringUtils.isEmpty(origine)){
			return origine.replaceAll("\n", "").replaceAll("\r", "").trim();
		}else{
			return "";
		}
	}

}
