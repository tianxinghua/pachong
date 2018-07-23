package com.shangpin.supplier.product.consumer.supplier.lungolivigno;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.shangpin.ephub.client.message.picture.ProductPicture;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.util.DateTimeUtil;
import com.shangpin.supplier.product.consumer.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.service.SupplierProductMongoService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.lungolivigno.dto.Result;
import com.shangpin.supplier.product.consumer.supplier.lungolivigno.dto.Sizes;

import lombok.extern.slf4j.Slf4j;

@Component("lungolivignoHandler") 
@Slf4j
public class LungolivignoHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private SupplierProductMongoService mongoService;

	@Autowired
	private PictureHandler pictureHandler;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				Result result = JsonUtil.deserialize(message.getData(), Result.class);
				String supplierId = message.getSupplierId();
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(supplierId,hubSpu,result);
				
				mongoService.save(supplierId, hubSpu.getSupplierSpuNo(), result);
				
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				for(Sizes size : result.getSizes()){
					HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
					boolean succ = convertSku(supplierId,hubSku,result.getSku(),size);
					if(succ){
						hubSkus.add(hubSku);
					}
				}
				if(success){
					//处理图片
					SupplierPicture supplierPicture = null;

					supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu,converImage(result.getPicUrls()) );

					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
			
		} catch (Exception e) {
			log.error("lungolivigno异常："+e.getMessage(),e); 
		}
		
	}
	
	private boolean convertSpu(String supplierId,HubSupplierSpuDto hubSpu,Result result){
		if(null != result){
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(result.getSku());
			hubSpu.setSupplierSpuModel(result.getAttributes().get(5).getCode());
			hubSpu.setSupplierSpuName(result.getName());
			hubSpu.setSupplierSpuColor(result.getAttributes().get(9).getValue().trim());
			hubSpu.setSupplierGender(result.getAttributes().get(2).getValue());
			hubSpu.setSupplierCategoryname(result.getAttributes().get(8).getValue().trim());
			hubSpu.setSupplierBrandname(result.getAttributes().get(1).getValue());
			hubSpu.setSupplierSeasonname(result.getAttributes().get(3).getValue()+result.getAttributes().get(4).getValue());
			String material = result.getAttributes().get(10).getValue().trim();
			hubSpu.setSupplierMaterial(!StringUtils.isEmpty(material)? material:result.getAttributes().get(10).getCode().trim());
			String origin = result.getAttributes().get(6).getValue();
			hubSpu.setSupplierOrigin(!StringUtils.isEmpty(origin)? origin:result.getAttributes().get(6).getCode());
			return true;
		}else{
			return false;
		}
	}
	
	private boolean convertSku(String supplierId,HubSupplierSkuDto hubSku,String supplierSpuNo,Sizes size){
		if(null != size){
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(supplierSpuNo+"-"+size.getSizeIndex());
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(size.getPrice())));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(size.getSupplierPrice())));
			String skuSize = size.getLabel();
			if(StringUtils.isEmpty(skuSize)) return false;
			if(!StringUtils.isEmpty(skuSize) && skuSize.contains("½")){
				skuSize = skuSize.replaceAll("½", "+");
			}
			hubSku.setSupplierSkuSize(skuSize);
			hubSku.setStock(size.getQty());
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 初始化发送图片消息体
	 * @param message 供应商原始消息
	 * @param hubSpu
	 * @param images
	 * @return
	 */
	public SupplierPicture initSupplierPicture(SupplierProduct message,HubSupplierSpuDto hubSpu,List<Image> images){
		SupplierPicture supplierPicture = new SupplierPicture();
		supplierPicture.setMessageId(UUIDGenerator.getUUID());
		supplierPicture.setMessageDate(DateTimeUtil.getDateTime());
		supplierPicture.setSupplierId(message.getSupplierId());
		supplierPicture.setSupplierName(message.getSupplierName());
		ProductPicture productPicture = new ProductPicture();
		productPicture.setSupplierSpuNo(hubSpu.getSupplierSpuNo());
		productPicture.setImages(images);
		supplierPicture.setProductPicture(productPicture);
		return supplierPicture;
	}


	private List<Image> converImage(List<String> imgUrl){
		List<Image> images = new ArrayList<Image>();
		if(null!=imgUrl&&imgUrl.size()>0){
			for(String url : imgUrl){
				Image image = new Image();
				image.setUrl(url.trim());
				images.add(image);
			}
		}

		return images;
	}

}
