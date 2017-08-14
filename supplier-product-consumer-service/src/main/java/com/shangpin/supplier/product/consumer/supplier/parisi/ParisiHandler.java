package com.shangpin.supplier.product.consumer.supplier.parisi;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
import com.shangpin.supplier.product.consumer.supplier.parisi.dto.Product;

import lombok.extern.slf4j.Slf4j;

/**
 * 暂停对接
 */
@Component("parisiHandler")
@Slf4j
public class ParisiHandler implements ISupplierHandler {
	
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
				Product product=null;
				try {
					product = mapper.readValue(message.getData(), Product.class) ;
				} catch (IOException e) {
					e.printStackTrace();
				}
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				String supplierId = message.getSupplierId();
				boolean success = convertSpu(supplierId,product,hubSpu,message.getData());
				
				mongoService.save(supplierId, hubSpu.getSupplierSpuNo(), product);
				
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				boolean skuSuc = convertSku(supplierId,hubSpu.getSupplierSpuId(),product,hubSku);
				if(skuSuc){
					hubSkus.add(hubSku);
				}
				//处理图片				
//				SupplierPicture supplierPicture = null;
//				if(pictureHandler.isCurrentSeason(supplierId, hubSpu.getSupplierSeasonname())){
				SupplierPicture	supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, converImage(product.getImages()));
//				}
				if(success){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}	
		} catch (Exception e) {
			log.error("parisi异常："+e.getMessage(),e);
		}
		
	}


	
	/**
	 * 处理图片
	 * @param imgUrl
	 * @return
	 */
	private List<Image> converImage(String imgUrl){
		

		List<Image> images = new ArrayList<Image>();
		String[] imageUrlArray = imgUrl.split("\\|\\|");
		if(null!=imageUrlArray&&imageUrlArray.length>0){
			List<String> picUrlList = Arrays.asList(imageUrlArray);
			for(String url :picUrlList){
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
	public boolean convertSpu(String supplierId,Product item, HubSupplierSpuDto hubSpu,String data) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(item.getProduct());
			hubSpu.setSupplierSpuModel(item.getProductCode());
			hubSpu.setSupplierSpuName(item.getName());
			hubSpu.setSupplierSpuColor(item.getColor());
			hubSpu.setSupplierGender(item.getGender());
			hubSpu.setSupplierCategoryname(item.getCategory());
			hubSpu.setSupplierBrandname(item.getBrand());
			hubSpu.setSupplierSeasonname(item.getSeason());
			hubSpu.setSupplierOrigin(item.getCountry());
			hubSpu.setSupplierSpuDesc(item.getSpecification());
			hubSpu.setSupplierMaterial(item.getComposition());

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
	public boolean convertSku(String supplierId, Long supplierSpuId,Product item, HubSupplierSkuDto hubSku) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			String size = null;
			size = item.getSize();
			hubSku.setSupplierSkuNo(item.getSku());
			hubSku.setSupplierSkuName(item.getName());
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(item.getPrice())));
			hubSku.setMarketPriceCurrencyorg(item.getCurrency());
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(item.getSupplierPrice())));
			hubSku.setSupplierSkuSize(size);
			hubSku.setStock(StringUtil.verifyStock(item.getStock()));
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
