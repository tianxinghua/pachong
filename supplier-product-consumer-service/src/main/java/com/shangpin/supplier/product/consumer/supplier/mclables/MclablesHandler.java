package com.shangpin.supplier.product.consumer.supplier.mclables;

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
import com.shangpin.supplier.product.consumer.service.SupplierProductMongoService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.mclables.dto.AttributeInfo;
import com.shangpin.supplier.product.consumer.supplier.mclables.dto.ImageInfoSubmit;
import com.shangpin.supplier.product.consumer.supplier.mclables.dto.Item;

import lombok.extern.slf4j.Slf4j;

@Component("mclablesHandler")
@Slf4j
public class MclablesHandler implements ISupplierHandler {

	
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
				Item item = JsonUtil.deserialize(message.getData(), Item.class);
				String supplierId = message.getSupplierId();
				
				mongoService.save(supplierId, item.getSku(), item);
				
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				convertSpu(supplierId,hubSpu,item);
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				boolean success = convertSku(supplierId,hubSku,item);
				if(success){
					hubSkus.add(hubSku);
				}
				SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, converImage(item));
				supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
			}
		} catch (Exception e) {
			log.error("mclables异常："+e.getMessage(),e); 
		}
		
		
	}
	/**
	 * 
	 * @param supplierId
	 * @param hubSpu
	 * @param item
	 * @return
	 */
	private boolean convertSpu(String supplierId,HubSupplierSpuDto hubSpu,Item item){
		if(null != item){
			if("true".equals(item.getVariationInfo().getIsParent())){
				hubSpu.setSupplierId(supplierId);
				hubSpu.setSupplierSpuNo(item.getSku());
				hubSpu.setSupplierSpuModel(item.getSku());
				hubSpu.setSupplierSpuName(item.getTitle());
				List<AttributeInfo> attributeInfolist = item.getAttributeList().getAttributeInfo();
				String color = "";
				String gender = "";
				String material = "";
				String season = "";
				for(AttributeInfo attr:attributeInfolist){
					if("Color".equals(attr.getName())){
						color = attr.getValue();
					}
					if("Materials_node".equals(attr.getName())){
						material = attr.getValue();
					}
					if("Gender".equals(attr.getName())){
						gender = attr.getValue();
					}
					if("season".equals(attr.getName())){
						season = attr.getValue();
					}
				}
				hubSpu.setSupplierSpuColor(color);
				hubSpu.setSupplierGender(gender);
				hubSpu.setSupplierCategoryname(item.getClassification());
				hubSpu.setSupplierBrandname(item.getBrand());
				hubSpu.setSupplierSeasonname(season);
				hubSpu.setSupplierMaterial(material);
				hubSpu.setSupplierOrigin("");
				hubSpu.setSupplierSpuDesc(item.getStoreInfo().getDescription());
				return true;
			}
		}
		return false;
	}
	/**
	 * 
	 * @param supplierId
	 * @param hubSku
	 * @param item
	 * @return
	 */
	private boolean convertSku(String supplierId,HubSupplierSkuDto hubSku, Item item){
		if(null != item){
			if("false".equals(item.getVariationInfo().getIsParent())){
				hubSku.setSupplierId(supplierId);
				hubSku.setSupplierSkuNo(item.getSku());
				hubSku.setSupplierSkuName(item.getTitle());
				hubSku.setSupplierBarcode(item.getEAN());
				hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(item.getPriceInfo().getRetailPrice())));
				hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(item.getPriceInfo().getChannelPrice())));
				String size = "";
				for(AttributeInfo attr:item.getAttributeList().getAttributeInfo()){
					if("size".equals(attr.getName())){
						size  = attr.getValue();
						break;
					}
				}
				hubSku.setSupplierSkuSize(size);
				hubSku.setStock(StringUtil.verifyStock(item.getDistributionCenterList().getDistributionCenterInfoSubmit().getQuantity()));
				return true;
			}
		}
		return false;
	}
	
	private List<Image> converImage(Item item){
		List<Image> images = new ArrayList<Image>();
		if(null != item && null != item.getImageList() && CollectionUtils.isNotEmpty(item.getImageList().getImageInfoSubmit())){
			List<ImageInfoSubmit> listImages = item.getImageList().getImageInfoSubmit();
			for(ImageInfoSubmit imageInfoSubmit:listImages){
				Image image = new Image();
				image.setUrl(imageInfoSubmit.getFilenameOrUrl());
				images.add(image);
			}
		}
		return images;
	}

}
