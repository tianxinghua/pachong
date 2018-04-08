package com.shangpin.supplier.product.consumer.supplier.redi;

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

import com.shangpin.supplier.product.consumer.supplier.redi.dto.Sku;
import com.shangpin.supplier.product.consumer.supplier.redi.dto.Spu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("rediHandler")
@Slf4j
public class RediHandler implements ISupplierHandler {
	
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
				Spu product = JsonUtil.deserialize(message.getData(), Spu.class);
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				String supplierId = message.getSupplierId();
				boolean success = convertSpu(supplierId,product,hubSpu);
				mongoService.save(supplierId, hubSpu.getSupplierSpuNo(), product);
				if(success){
					List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();

					convertSku(supplierId,product,hubSkus);

					List<Image> images = converImage(product);

					SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
		} catch (Exception e) {
			log.error("redi异常："+e.getMessage(),e);
		}
		
	}
	
	public boolean convertSpu(String supplierId,Spu product,HubSupplierSpuDto hubSpu){
		if(null != product){
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuName(product.getItem_name());
			hubSpu.setSupplierSpuNo(product.getItem_id());
			hubSpu.setSupplierSpuModel(product.getItem_sku());
			hubSpu.setSupplierSpuColor(product.getItem_color());
			hubSpu.setSupplierGender(product.getItem_gender());
			hubSpu.setSupplierCategoryname(product.getItem_category());
			hubSpu.setSupplierBrandname(product.getItem_brand());
			hubSpu.setSupplierBrandno(product.getItem_brand_id());
			hubSpu.setSupplierSeasonname(product.getItem_detailed_composition());
			hubSpu.setSupplierMaterial(product.getItem_sku());
			hubSpu.setSupplierOrigin(product.getItem_madein());
			hubSpu.setSupplierSpuDesc(product.getItem_longDescription());
			return true;
		}else{
			return false;
		}
	}
	
	public void  convertSku(String supplierId, Spu  product,List<HubSupplierSkuDto> hubSkus){
		if(null != product){
			List<Sku> skus = product.getItem_sizes();
			if(null!=skus&&skus.size()>0){
				skus.forEach(sku->{
					HubSupplierSkuDto hubSku = null;
					try {
						hubSku = new HubSupplierSkuDto();
						hubSku.setSupplierId(supplierId);
						hubSku.setSupplierSkuNo(sku.getItem_size_id());
						hubSku.setMarketPrice(new BigDecimal(product.getItem_retail_price()));
						hubSku.setSupplyPrice(new BigDecimal(product.getItem_supply_price()));
						hubSku.setMarketPriceCurrencyorg(null);
						hubSku.setSupplierBarcode(sku.getItem_size_id());
						hubSku.setSupplierSkuSize(sku.getItem_size());
						hubSku.setSupplierSkuSizeType(sku.getItem_size_system());
						hubSku.setStock(StringUtil.verifyStock(sku.getItem_stock()));
						hubSkus.add(hubSku);
					} catch (Exception e) {
						log.error("sku: " +sku.toString() + " 保存失败。");
					}
				});
			}


		}
	}
	
	private List<Image> converImage(Spu product){
		if(null == product){
			return null;
		}else{
			List<Image> images = new ArrayList<Image>();
			if(null!=product.getItem_images()&&product.getItem_images().size()>0){
				product.getItem_images().forEach(url->{
					Image image = new Image();
					image.setUrl(url);
					images.add(image);
				});
			}
			return images;
		}
	}
	


}
