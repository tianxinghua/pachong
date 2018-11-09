package com.shangpin.supplier.product.consumer.supplier.accuratime;

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
import com.shangpin.supplier.product.consumer.supplier.accuratime.dto.ProductDTO;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("accuratimeHandler")
@Slf4j
public class AccuratimeHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;
	@Autowired
	private SupplierProductMongoService mongoService;

	ObjectMapper mapper = new ObjectMapper();
	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				ProductDTO item = mapper.readValue(message.getData(), ProductDTO.class);
				String supplierId = message.getSupplierId();
				
				mongoService.save(supplierId, item.getSpuId(), item);
				
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(supplierId,item,hubSpu);
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				boolean skuSuc = convertSku(supplierId,hubSpu.getSupplierSpuId(),item,hubSku);
				if(skuSuc){
					hubSkus.add(hubSku);
				}
				//处理图片				
				SupplierPicture supplierPicture = null;

				supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu,
						converImage(org.apache.commons.lang.StringUtils.isNotBlank(item.getSkuPicture())?item.getSkuPicture():item.getSpuPicture()));

				if(success){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}	
		} catch (Exception e) {
			log.error("mass异常："+e.getMessage(),e);
		}
		
	}
	
	/**
	 * mass处理图片
	 * @param imgUrl
	 * @return
	 */
	private List<Image> converImage(String imgUrl){
		List<Image> images = new ArrayList<Image>();
		if(org.apache.commons.lang.StringUtils.isNotBlank(imgUrl)){
			String[] imageSpuUrlArray = imgUrl.split("\\|\\|");
			if(null!=imageSpuUrlArray&&imageSpuUrlArray.length>0){

				for(String url : imageSpuUrlArray){
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
	public boolean convertSpu(String supplierId, ProductDTO item, HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(item.getId());
			hubSpu.setSupplierSpuModel(item.getId());
			hubSpu.setSupplierSpuName(item.getCategoryName());
			hubSpu.setSupplierSpuColor(item.getSpStatus());//颜色在描述中
			hubSpu.setSupplierGender(item.getCategoryGender());
			hubSpu.setSupplierCategoryname(item.getProductName());
			hubSpu.setSupplierBrandname(item.getSkuId());
			hubSpu.setSupplierSeasonname("四季");
			hubSpu.setSupplierMaterial(item.getSeasonName());
			hubSpu.setSupplierOrigin(item.getProductOrigin());
			hubSpu.setSupplierSpuDesc(item.getProductDescription());
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
	public boolean convertSku(String supplierId, Long supplierSpuId,ProductDTO item, HubSupplierSkuDto hubSku) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			String size = null;
			size = item.getSize();
			if(size==null){
				size = "A";
			}
			String supplierSkuNo = item.getId();
			hubSku.setSupplierSkuNo(supplierSkuNo);
			hubSku.setSupplierSkuName(item.getCategoryName());
			hubSku.setSupplierBarcode(supplierSkuNo);
			System.out.println("价格价格价格价格价格价格价格价格价格价格"+item.getSpuId());
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(item.getSpuId())));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(item.getSpuId())));
			/*hubSku.setMarketPriceCurrencyorg(item.getSaleCurrency());
			hubSku.setSupplyPriceCurrency(item.getSaleCurrency());*/
			hubSku.setSupplierSkuSize(size);

			String itemstr=item.getSupplierName();
			System.out.println(itemstr);
			hubSku.setStock(Integer.valueOf(itemstr.replace(".0","")));
			return true;
		}else{
			return false;
		}
	}

}
