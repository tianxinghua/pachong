package com.shangpin.supplier.product.consumer.supplier.monnierfreres;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.shangpin.supplier.product.consumer.supplier.monnierfreres.dto.Product;

import lombok.extern.slf4j.Slf4j;

@Component("monnierHandler")
@Slf4j
public class MonnierHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;
	
	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				Product item = JsonUtil.deserialize(message.getData(), Product.class);
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(message.getSupplierId(),item,hubSpu,message.getData());
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				boolean skuSuc = convertSku(message.getSupplierId(),hubSpu.getSupplierSpuId(),item,hubSku);
				if(skuSuc){
					hubSkus.add(hubSku);
				}
				//处理图片				
				SupplierPicture supplierPicture = null;
				if(pictureHandler.isCurrentSeason(message.getSupplierId(), hubSpu.getSupplierSeasonname())){
					supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, converImage(item));
				}
				if(success){
					supplierProductSaveAndSendToPending.gebSaveAndSendToPending(message.getSupplierNo(),message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}	
		} catch (EpHubSupplierProductConsumerException e) {
			log.error("geb异常："+e.getMessage(),e); 
		}
		
	}
	
	/**
	 * geb处理图片
	 * @param itemImages
	 * @return
	 */
	private List<Image> converImage(Product itemImages){
		
		String[] img = new String[5];
		if (!StringUtils.isEmpty((itemImages.getImage_url_1()))) {
			img[0] = itemImages.getImage_url_1();
		}
		if (!StringUtils.isEmpty((itemImages.getImage_url_2()))) {
			img[1] = itemImages.getImage_url_2();
		}
		if (!StringUtils.isEmpty((itemImages.getImage_url_3()))) {
			img[2] = itemImages.getImage_url_3();
		}
		if (!StringUtils.isEmpty((itemImages.getImage_url_4()))) {
			img[3] = itemImages.getImage_url_4();
		}
		if (!StringUtils.isEmpty((itemImages.getImage_url_5()))) {
			img[4] = itemImages.getImage_url_5();
		}
		
		List<Image> images = new ArrayList<Image>();
		if(null != itemImages){			
			if(img.length>0){
				for(String url : img){
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
	public boolean convertSpu(String supplierId,Product item, HubSupplierSpuDto hubSpu,String data) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(item.getProduct_id());
			hubSpu.setSupplierSpuModel(item.getProduct_id());
			hubSpu.setSupplierSpuName(item.getName());
			hubSpu.setSupplierSpuColor(item.getColor());
			hubSpu.setSupplierGender(item.getGender());
			hubSpu.setSupplierCategoryname(item.getType());
			hubSpu.setSupplierBrandname(item.getBrand());
//			hubSpu.setSupplierSeasonname(item.getSeason_year()+item.getSeason_reference());
			hubSpu.setSupplierMaterial(item.getMaterial());
			hubSpu.setSupplierOrigin(item.getManufacturer());
			hubSpu.setSupplierSpuDesc(item.getDescription());
			hubSpu.setMemo(data);
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
	public boolean convertSku(String supplierId, Long supplierSpuId,Product item, HubSupplierSkuDto hubSku) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			String size = null;
			size = item.getSize();
			String supplierSkuNo = item.getSku();
			hubSku.setSupplierSkuNo(supplierSkuNo);
			hubSku.setSupplierSkuName(item.getName());
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(item.getPrice_before_discount())));
			hubSku.setMarketPriceCurrencyorg(item.getPrice_type());
			hubSku.setSupplierSkuSize(size);
			hubSku.setStock(StringUtil.verifyStock(item.getQty()));
			return true;
		}else{
			return false;
		}
	}

}
