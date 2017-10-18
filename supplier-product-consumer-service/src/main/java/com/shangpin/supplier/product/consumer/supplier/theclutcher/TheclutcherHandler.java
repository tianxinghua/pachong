package com.shangpin.supplier.product.consumer.supplier.theclutcher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.shangpin.supplier.product.consumer.supplier.theclutcher.dto.Product;

import lombok.extern.slf4j.Slf4j;

@Component("theclutcherHandler")
@Slf4j
public class TheclutcherHandler implements ISupplierHandler{
	
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
				Product product = JsonUtil.deserialize(message.getData(), Product.class);
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				String supplierId = message.getSupplierId();
				boolean success = convertSpu(supplierId,product,hubSpu);
				
				mongoService.save(supplierId, hubSpu.getSupplierSpuNo(), product);
				
				if(success){
					List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
					HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
					boolean succSku = convertSku(supplierId,product,hubSku);
					if(succSku){
						hubSkus.add(hubSku);
					}
					List<Image> images = converImage(product.getImages());
					SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
		} catch (Exception e) {
			log.error("theclutcher异常："+e.getMessage(),e);
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
	public boolean convertSpu(String supplierId,Product studioSpuDto,HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException{
		if(null == studioSpuDto){
			return false;
		}else{
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(studioSpuDto.getSpuID());
			hubSpu.setSupplierSpuModel(studioSpuDto.getCode());
			hubSpu.setSupplierSpuName(studioSpuDto.getName());
			hubSpu.setSupplierSpuColor(studioSpuDto.getColor());
			hubSpu.setSupplierGender(studioSpuDto.getGender());
			hubSpu.setSupplierCategoryno(studioSpuDto.getCategory());
			hubSpu.setSupplierCategoryname(studioSpuDto.getCategory());
			hubSpu.setSupplierBrandname(studioSpuDto.getBrand());
			hubSpu.setSupplierSeasonname(studioSpuDto.getSeason());
		
			String notes = studioSpuDto.getNotes();
			hubSpu.setSupplierSpuDesc(studioSpuDto.getDescription()+","+notes);
			String origin = null;
			String material = null;
			if(notes!=null&&notes.contains("Made in")){
				String []arr = notes.split("\\.");
				for(String a:arr){
					if(a.contains("Made in")){
						origin = a.substring(a.indexOf("Made in")+7).trim();
					}
					log.info("origin: "+a+"===>"+origin);
					if(a.contains("Composition")){
						material = a;
					}
				}
			}
			if(material!=null){
				hubSpu.setSupplierMaterial(material);	
			}else{
				hubSpu.setSupplierMaterial(notes);
			}
			hubSpu.setSupplierOrigin(origin);
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
	public boolean convertSku(String supplierId,Product studioSkuDto,HubSupplierSkuDto hubSku){
		String skuId = studioSkuDto.getSkuID();
		hubSku.setSupplierId(supplierId);
		hubSku.setSupplierSkuNo(skuId);
		hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(studioSkuDto.getMarketPrice())));
		hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(studioSkuDto.getSupplierPrice())));
		hubSku.setSupplierSkuSize(studioSkuDto.getSize());
		if(skuId!=null&&skuId.split("-").length>1){
			if(StringUtils.isNotBlank(studioSkuDto.getBarCode())){
				hubSku.setSupplierBarcode(studioSkuDto.getBarCode()+" "+skuId.split("-")[1]);	
			}else{
				hubSku.setSupplierBarcode(skuId);
			}
		}
		hubSku.setStock(StringUtil.verifyStock((studioSkuDto.getStock())));
		return true;
	}
	private List<Image> converImage(String picture){
		
		String [] arr = picture.split("\\|\\|");
		List<Image> images = new ArrayList<Image>();
		if(arr!=null&&arr.length>0){			
			for(String url : arr){
				Image image = new Image();
				image.setUrl(url);
				images.add(image);
			}
		}
		return images;
	}

}
