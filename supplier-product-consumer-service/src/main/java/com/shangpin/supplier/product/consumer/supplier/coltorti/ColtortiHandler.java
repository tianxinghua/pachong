package com.shangpin.supplier.product.consumer.supplier.coltorti;

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
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.coltorti.convert.ColtortiProductConvert;
import com.shangpin.supplier.product.consumer.supplier.coltorti.dto.ColtortiProduct;
import com.shangpin.supplier.product.consumer.supplier.common.enumeration.Isexistpic;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;

import lombok.extern.slf4j.Slf4j;

@Component("coltortiHandler")
@Slf4j
public class ColtortiHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(null != message && !StringUtils.isEmpty(message.getData())){
				ColtortiProduct p = JsonUtil.deserialize(message.getData(), ColtortiProduct.class);
				HubSupplierSpuDto supplierSpuDto = ColtortiProductConvert.product2spu(message.getSupplierId(), p);
				List<Image> images = ColtortiProductConvert.productPic(p);
				if(null != images){
					supplierSpuDto.setIsexistpic(Isexistpic.YES.getIndex());
				}else{
					supplierSpuDto.setIsexistpic(Isexistpic.NO.getIndex());
				}
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto supplierSkuDto = ColtortiProductConvert.product2sku(message.getSupplierId(), p);
				hubSkus.add(supplierSkuDto);
				SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, supplierSpuDto, images);
				supplierProductSaveAndSendToPending.coltortiSaveAndSendToPending(message.getSupplierNo(), message.getSupplierId(), message.getSupplierName(), supplierSpuDto, hubSkus,supplierPicture);
			}
		} catch (Exception e) {
			log.error("coltorti异常："+e.getMessage(),e); 
		}
		
	}

}
