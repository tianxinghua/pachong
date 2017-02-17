package com.shangpin.supplier.product.consumer.supplier.geb;

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
import com.shangpin.supplier.product.consumer.supplier.geb.dto.Item;
import com.shangpin.supplier.product.consumer.supplier.geb.dto.Item_images;
import com.shangpin.supplier.product.consumer.supplier.geb.dto.Material;

import lombok.extern.slf4j.Slf4j;

@Component("gebHandler")
@Slf4j
public class GebHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;
	
	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				Item item = JsonUtil.deserialize(message.getData(), Item.class);
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(message.getSupplierId(),item,hubSpu);
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				boolean skuSuc = convertSku(message.getSupplierId(),hubSpu.getSupplierSpuId(),item,hubSku);
				if(skuSuc){
					hubSkus.add(hubSku);
				}
				//处理图片				
				SupplierPicture supplierPicture = null;
//				if(pictureHandler.isCurrentSeason(message.getSupplierId(), hubSpu.getSupplierSeasonname())){
					supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, converImage(item.getItem_images()));
//				}
				if(success){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
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
	private List<Image> converImage(Item_images itemImages){
		List<Image> images = new ArrayList<Image>();
		if(null != itemImages){			
			if(null != itemImages.getFull()){
				for(String url : itemImages.getFull()){
					Image image = new Image();
					image.setUrl(url);
					images.add(image);
				}
			}
//			if(null != itemImages.getThumb()){
//				for(String url : itemImages.getThumb()){
//					Image image = new Image();
//					image.setUrl(url);
//					images.add(image);
//				}
//			}
		}
		return images;
	}
	
	/**
	 * 将geb原始dto转换成hub spu
	 * @param supplierId 供应商门户id
	 * @param item 供应商原始dto
	 * @param hubSpu hub spu表
	 */
	public boolean convertSpu(String supplierId,Item item, HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(item.getProduct_id());
			hubSpu.setSupplierSpuModel(item.getProduct_reference()+" "+item.getColor_reference());
			hubSpu.setSupplierSpuName(item.getItem_intro());
			hubSpu.setSupplierSpuColor(item.getColor());
			hubSpu.setSupplierGender(item.getGender());
			hubSpu.setSupplierCategoryname(item.getSecond_category());
			hubSpu.setSupplierBrandname(item.getBrand());
			hubSpu.setSupplierSeasonname(item.getSeason_year()+item.getSeason_reference());
			List<Material> list = item.getTechnical_info();
			if(list!=null&&!list.isEmpty()){
				StringBuffer str = new StringBuffer();
				for(Material m:list){
					str.append(",").append(m.getPercentage()).append(m.getName());
				}
				hubSpu.setSupplierMaterial(str.substring(1));
			}
			hubSpu.setSupplierOrigin(item.getMade_in());
			hubSpu.setSupplierSpuDesc(item.getItem_description());
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
	public boolean convertSku(String supplierId, Long supplierSpuId,Item item, HubSupplierSkuDto hubSku) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			String size = null;
			size = item.getSize();
			if(size==null){
				size = "A";
			}
			String supplierSkuNo = item.getProduct_id()+"-"+size;
			hubSku.setSupplierSkuNo(supplierSkuNo);
			hubSku.setSupplierSkuName(item.getItem_intro());
			hubSku.setSupplierBarcode(supplierSkuNo);
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(item.getRetail_price())));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(item.getPrice())));
			hubSku.setMarketPriceCurrencyorg(item.getCurrency());
			hubSku.setSupplyPriceCurrency(item.getCurrency());
			hubSku.setSupplierSkuSize(size);
			hubSku.setStock(StringUtil.verifyStock((item.getQuantity())));
			return true;
		}else{
			return false;
		}
	}

}
