package com.shangpin.supplier.product.consumer.supplier.optical;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerRuntimeException;
import com.shangpin.supplier.product.consumer.service.SupplierProductMongoService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 暂停对接
 */
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
				ObjectMapper mapper=new ObjectMapper();
				List<String> contentList = null;
				try {
					contentList = mapper.readValue(message.getData(), new TypeReference<List<String>>(){}) ;
				} catch (IOException e) {
					e.printStackTrace();
				}
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				String supplierId = message.getSupplierId();
				boolean success = convertSpu(supplierId,contentList,hubSpu,message.getData());
				
				mongoService.save(supplierId, hubSpu.getSupplierSpuNo(), message.getData());
				
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				boolean skuSuc = convertSku(supplierId,hubSpu.getSupplierSpuId(),contentList,hubSku);
				if(skuSuc){
					hubSkus.add(hubSku);
				}
				//处理图片				
				SupplierPicture supplierPicture = null;
				if(pictureHandler.isCurrentSeason(supplierId, hubSpu.getSupplierSeasonname())){
					supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, converImage(contentList));
				}
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
	 * 将geb原始dto转换成hub spu
	 * @param supplierId 供应商门户id
	 * @param item 供应商原始dto
	 * @param hubSpu hub spu表
	 */
	public boolean convertSpu(String supplierId,List<String> item, HubSupplierSpuDto hubSpu,String data) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(getValue(item.get(3))+"-"+(null==getValue(item.get(7))?"":getValue(item.get(7))));
			hubSpu.setSupplierSpuModel(getValue(item.get(3)));
			hubSpu.setSupplierSpuName(getValue(item.get(6)));
			hubSpu.setSupplierSpuColor(getValue(item.get(7)));
			hubSpu.setSupplierGender(getValue(item.get(5)));
			hubSpu.setSupplierCategoryname(getValue(item.get(1)));
			hubSpu.setSupplierBrandname(getValue(item.get(2)));
			hubSpu.setSupplierSeasonname(getValue(item.get(19)));
			hubSpu.setSupplierOrigin(getValue(item.get(10)));
			hubSpu.setSupplierSpuDesc(getValue(item.get(1)));
			hubSpu.setSupplierMaterial(getValue(item.get(9)));

//			hubSpu.setMemo(data);
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
	public boolean convertSku(String supplierId, Long supplierSpuId,List<String> item, HubSupplierSkuDto hubSku) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			String size = null;
			size = getValue(item.get(8));
			hubSku.setSupplierSkuNo(getValue(item.get(4)));
			hubSku.setSupplierSkuName(getValue(item.get(6)));
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(getValue(item.get(16)))));
			hubSku.setMarketPriceCurrencyorg(getValue(item.get(18)));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(getValue(item.get(17)))));
			hubSku.setSupplierSkuSize(size);
			hubSku.setStock(StringUtil.verifyStock(getValue(item.get(15))));
			return true;
		}else{
			return false;
		}
	}


	public String getValue(String value){

		if(org.apache.commons.lang.StringUtils.isNotBlank(value)){
			return value;
		}else{
			return null;
		}


	}



}
