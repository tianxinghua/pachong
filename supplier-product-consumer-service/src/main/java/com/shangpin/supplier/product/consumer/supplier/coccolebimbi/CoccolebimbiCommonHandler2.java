package com.shangpin.supplier.product.consumer.supplier.coccolebimbi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
import com.shangpin.supplier.product.consumer.supplier.coccolebimbi.dto.Sku;
import com.shangpin.supplier.product.consumer.supplier.coccolebimbi.dto.Spu;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CoccolebimbiCommonHandler2 implements ISupplierHandler {
	
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
				Spu item = JsonUtil.deserialize(message.getData(), Spu.class);
				String supplierId = message.getSupplierId();
				
				mongoService.save(supplierId, item.getSpuId(), item);
				
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(supplierId,item,hubSpu);
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				
				List<Sku> skus = item.getSkus();
				for(Sku sku:skus){
					boolean skuSuc = convertSku(supplierId,hubSpu.getSupplierSpuId(),sku,hubSku);
					if(skuSuc){
						hubSkus.add(hubSku);
					}
				}
				
				//处理图片				
				SupplierPicture supplierPicture = null;
//				if(pictureHandler.isCurrentSeason(message.getSupplierId(), hubSpu.getSupplierSeasonname())){
					supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, converImage(item.getImages()));
//				}
				if(success){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}	
		} catch (Exception e) {
			log.error("geb异常："+e.getMessage(),e); 
		}
		
	}
	
	/**
	 * geb处理图片
	 * @param itemImages
	 * @return
	 */
	private List<Image> converImage(List<String> itemImages){
		List<Image> images = new ArrayList<Image>();
		if(null != itemImages){			
			for(String url : itemImages){
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
	public boolean convertSpu(String supplierId,Spu item, HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(item.getSpuId());
//			hubSpu.setSupplierSpuModel(item.getProduct_reference()+" "+item.getColor_reference());
			hubSpu.setSupplierSpuName(item.getSpuName());
			hubSpu.setSupplierSpuColor(item.getColor());
			hubSpu.setSupplierGender(item.getGender());
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
	public boolean convertSku(String supplierId, Long supplierSpuId,Sku item, HubSupplierSkuDto hubSku) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(item.getSkuId());
			hubSku.setSupplierBarcode(item.getBarCode());
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(item.getMarketPrice())));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(item.getSupplierPrice())));
//			hubSku.setMarketPriceCurrencyorg(item.getCurrency());
//			hubSku.setSupplyPriceCurrency(item.getCurrency());
			hubSku.setSupplierSkuSize(item.getSize());
			hubSku.setStock(StringUtil.verifyStock((item.getQty())));
			return true;
		}else{
			return false;
		}
	}

}
