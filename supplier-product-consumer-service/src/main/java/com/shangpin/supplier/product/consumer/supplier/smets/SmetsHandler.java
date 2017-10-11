package com.shangpin.supplier.product.consumer.supplier.smets;

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
import com.shangpin.supplier.product.consumer.service.SupplierProductMongoService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.smets.dto.ProductDTO;

import lombok.extern.slf4j.Slf4j;

@Component("smetsHandler")
@Slf4j
public class SmetsHandler implements ISupplierHandler {
	
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
				ProductDTO product = JsonUtil.deserialize(message.getData(), ProductDTO.class);
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
					List<Image> images = converImage(product);
					SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
		} catch (Exception e) {
			log.error("smets异常："+e.getMessage(),e); 
		}
		
	}
	
	public boolean convertSpu(String supplierId,ProductDTO product,HubSupplierSpuDto hubSpu){
		if(null != product){
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuName(product.getProductName());
			hubSpu.setSupplierSpuNo(product.getSpuId());
			hubSpu.setSupplierSpuModel(product.getProductCode());
			hubSpu.setSupplierSpuColor(product.getColor());
			hubSpu.setSupplierGender(product.getCategoryGender());
			hubSpu.setSupplierCategoryname(product.getCategoryName());
			hubSpu.setSupplierBrandname(product.getBrandName());
			hubSpu.setSupplierSeasonname(product.getSeasonName());
			hubSpu.setSupplierMaterial(product.getMaterial());
			hubSpu.setSupplierOrigin(product.getProductOrigin());
			hubSpu.setSupplierSpuDesc(product.getProductDescription());
			return true;
		}else{
			return false;
		}
	}
	
	public boolean convertSku(String supplierId, ProductDTO product,HubSupplierSkuDto hubSku){
		if(null != product){
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(product.getSkuId());
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(product.getNewMarketPrice())));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(product.getNewSupplierPrice())));
			hubSku.setSalesPrice(new BigDecimal(StringUtil.verifyPrice(product.getNewSalePrice()))); 
			hubSku.setMarketPriceCurrencyorg(product.getSaleCurrency()); 
			hubSku.setSupplierBarcode(product.getBarcode());
			hubSku.setSupplierSkuSize(product.getSize());
			hubSku.setSupplierSkuSizeType(product.getSizeClass());
			hubSku.setStock(StringUtil.verifyStock(product.getStock()));
			return true;
		}else{
			return false;
		}
	}
	
	private List<Image> converImage(ProductDTO product){
		if(null == product){
			return null;
		}else{
			List<Image> images = new ArrayList<Image>();
			setSpuPicture(product.getSkuPicture(),images);
			setSpuPicture(product.getSpuPicture(),images);
			setImage(product.getItemPictureUrl1(),images);
			setImage(product.getItemPictureUrl2(),images);
			setImage(product.getItemPictureUrl3(),images);
			setImage(product.getItemPictureUrl4(),images);
			setImage(product.getItemPictureUrl5(),images);
			setImage(product.getItemPictureUrl6(),images);
			setImage(product.getItemPictureUrl7(),images);
			setImage(product.getItemPictureUrl8(),images);
			return images;
		}
	}
	
	private void setSpuPicture(String skuPicture, List<Image> images) {
		if(skuPicture!=null){
			String [] picArr = skuPicture.split("\\|");
			for(String pic:picArr){
				Image image = new Image();
				image.setUrl(pic);
				images.add(image);
			}
		}
		
	}

	private void setImage(String url,List<Image> images){
		if(StringUtils.isEmpty(url)){
			Image image = new Image();
			image.setUrl(url);
			images.add(image);
		}
	}
}
