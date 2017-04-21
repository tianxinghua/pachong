package com.shangpin.supplier.product.consumer.supplier.optical;

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

import lombok.extern.slf4j.Slf4j;

@Component("opticalHandler")
@Slf4j
public class OpticalHandler implements ISupplierHandler {
	
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
				List<String> contentList = JsonUtil.deserialize(message.getData(), List.class);
				String supplierId = message.getSupplierId();
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(supplierId,contentList,hubSpu);
				
				mongoService.save(supplierId, hubSpu.getSupplierSpuNo(), message.getData());
				
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				boolean skuSuc = convertSku(supplierId,contentList,hubSku);
				if(skuSuc){
					hubSkus.add(hubSku);
				}
				//处理图片				
				SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, converImage(contentList));
				if(success){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}	
		} catch (Exception e) {
			log.error("optical异常："+e.getMessage(),e);
		}
		
	}
	
	/**
	 * 处理图片
	 * @param item
	 * @return
	 */
	private List<Image> converImage(List<String> item){
		List<Image> images = new ArrayList<Image>();
		String url ="";
		for(int i=13;i<=17;i++){
			if(item.size()==i){
				break;
			}
			url = getValue(item.get(i));
			if(url!=null){
				if(url.startsWith("http")){
					Image image = new Image();
					image.setUrl(url);
					images.add(image);
				}

			}
		}
		return images;
	}
	
	/**
	 * 将optical原始dto转换成hub spu
	 * @param supplierId 供应商门户id
	 * @param item 供应商原始dto
	 * @param hubSpu hub spu表
	 */
	public boolean convertSpu(String supplierId,List<String> item, HubSupplierSpuDto hubSpu){
		if(null != item){			
			
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(item.get(3)+"-"+getValue(item.get(7)));
			hubSpu.setSupplierSpuModel(item.get(3)+"-"+getValue(item.get(7)));
			hubSpu.setSupplierSpuName(item.get(6));
			hubSpu.setSupplierSpuColor(item.get(7));
			hubSpu.setSupplierGender(item.get(5));
			hubSpu.setSupplierCategoryname(item.get(1));
			hubSpu.setSupplierBrandname(item.get(2));
			hubSpu.setSupplierSeasonname(item.get(19));
			hubSpu.setSupplierOrigin(item.get(10));
			hubSpu.setSupplierSpuDesc(item.get(14));
			hubSpu.setSupplierMaterial(item.get(9));
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 将optical原始dto转换成hub sku
	 * @param supplierId
	 * @param supplierSpuId hub spuid
	 * @param item
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId,List<String> item, HubSupplierSkuDto hubSku){
		if(null != item){			
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(item.get(4));
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(item.get(16))));
			hubSku.setMarketPriceCurrencyorg(item.get(18));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(item.get(17))));
			hubSku.setSupplierSkuSize(getValue(item.get(8)));
			hubSku.setStock(StringUtil.verifyStock(item.get(15)));
			return true;
		}else{
			return false;
		}
	}


	public String getValue(String value){

		if(org.apache.commons.lang.StringUtils.isNotBlank(value)){
			return value;
		}else{
			return "";
		}


	}



}
