package com.shangpin.supplier.product.consumer.supplier.coltortiv2;

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
import com.shangpin.supplier.product.consumer.supplier.coltortiv2.convert.ColtortiProductConvert;
import com.shangpin.supplier.product.consumer.supplier.coltortiv2.dto.ColtortiProduct;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;

import lombok.extern.slf4j.Slf4j;
/**
 * 供应商coltorti处理类
 * <p>Title: ColtortiHandler</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年2月24日 上午10:09:05
 *
 */
@Component("coltortiHandler")
@Slf4j
public class ColtortiHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;
	@Autowired
	private SupplierProductMongoService mongoService;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(null != message && !StringUtils.isEmpty(message.getData())){
				ColtortiProduct p = JsonUtil.deserialize(message.getData(), ColtortiProduct.class);
				String supplierId = message.getSupplierId();
				
				mongoService.save(supplierId, p.getSupplierSpuNo(), p);
				
				HubSupplierSpuDto supplierSpuDto = ColtortiProductConvert.product2spu(supplierId, p);
				List<Image> images = ColtortiProductConvert.productPic(p);
				List<HubSupplierSkuDto> hubSkus = ColtortiProductConvert.product2sku(supplierId, p);
				
				SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, supplierSpuDto, images);
				supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(), supplierId, message.getSupplierName(), supplierSpuDto, hubSkus,supplierPicture);
			}
		} catch (Exception e) {
			log.error("coltorti异常："+e.getMessage(),e); 
		}
		
	}

}
