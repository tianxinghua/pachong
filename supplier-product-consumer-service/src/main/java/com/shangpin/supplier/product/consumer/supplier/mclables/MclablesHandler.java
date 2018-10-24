package com.shangpin.supplier.product.consumer.supplier.mclables;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.shangpin.supplier.product.consumer.supplier.mclables.dto.*;
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
				ItemInfo item = JsonUtil.deserialize(message.getData(), ItemInfo.class);
				String supplierId = message.getSupplierId();
				mongoService.save(supplierId, item.getSku(), item);

				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean succ = convertSpu(supplierId,hubSpu,item);
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				boolean success = convertSku(supplierId,hubSkus,item);
				if (success == false){
					log.error("sku异常");
					return;
				}
				SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, converImage(item));
				if(succ){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
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
	private boolean convertSpu(String supplierId,HubSupplierSpuDto hubSpu,ItemInfo item){
		if(null != item){
			//if("false".equals(item.getVariationInfo().getIsParent())){
				hubSpu.setSupplierId(supplierId);
				hubSpu.setSupplierSpuNo(item.getSku());
				hubSpu.setSupplierSpuModel(item.getSku());
				hubSpu.setSupplierSpuName(item.getTitle());
				hubSpu.setSupplierSpuColor(item.getColor());
				hubSpu.setSupplierGender(item.getGender());
				hubSpu.setSupplierCategoryname(item.getCategory());
				hubSpu.setSupplierBrandname(item.getBrand());
				hubSpu.setSupplierSeasonname(item.getSeason());
				hubSpu.setSupplierMaterial(item.getMaterial().toString());
				hubSpu.setSupplierOrigin("");
				hubSpu.setSupplierSpuDesc(item.getDescription());
				return true;
			}
		//}
		/*List<AttributeInfo> attributeInfolist = item.getAttributeList().getAttributeInfo();
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
				}*/
		return false;
	}
	/**
	 * 
	 * @param supplierId
	 * @param hubSkus
	 * @param item
	 * @return
	 */
	private boolean convertSku(String supplierId,List<HubSupplierSkuDto> hubSkus, ItemInfo item){
		if(null != item){
			//if("false".equals(item.getVariationInfo().getIsParent())){
			List<Variantinfo> variants = item.getVariants();
			if (null != variants && variants.size() > 0){
				variants.stream().forEach(t->{
					HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
					hubSku.setSupplierId(supplierId);
					hubSku.setSupplierSkuNo(t.getCode());
					hubSku.setSupplierSkuName(item.getTitle());
					hubSku.setSupplierBarcode(t.getCode());
					hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(item.getRetailPrice())));
					hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(item.getListPrice())));
					hubSku.setSupplierSkuSize(t.getSize());
					hubSku.setStock(StringUtil.verifyStock(t.getQuantity()));
					hubSkus.add(hubSku);
				});
				return true;
			}
				/*
				String size = "";
					for(AttributeInfo attr:item.getAttributeList().getAttributeInfo()){
						if("Brand_size".equals(attr.getName())){//size
							size  = attr.getValue();
							break;
						}
					}hubSku.setSupplierId(supplierId);
				hubSku.setSupplierSkuNo(item.getSku());
				hubSku.setSupplierSkuName(item.getTitle());
				hubSku.setSupplierBarcode(item.getEAN());
				hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(item.getRetailPrice())));
				hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(item.getPriceInfo().getChannelPrice())));
				String size = "";
				for(AttributeInfo attr:item.getAttributeList().getAttributeInfo()){
					if("Brand_size".equals(attr.getName())){//size
						size  = attr.getValue();
						break;
					}
				}
				hubSku.setSupplierSkuSize(size);
				hubSku.setStock(StringUtil.verifyStock(item.getDistributionCenterList().getDistributionCenterInfoSubmit().getQuantity()));*/
			}
		//}
		return false;
	}
	
	private List<Image> converImage(ItemInfo item){
		List<Image> images = new ArrayList<Image>();
		List<String> imagesList = item.getImages();
		if(null != item && null != imagesList && CollectionUtils.isNotEmpty(imagesList)){
			for (String str : imagesList) {
				Image image = new Image();
				image.setUrl(str);
				images.add(image);
			}
		}
		return images;
	}

}
