package com.shangpin.supplier.product.consumer.supplier.baseblu;

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
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerRuntimeException;
import com.shangpin.supplier.product.consumer.service.SupplierProductMongoService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.baseblu.dto.Item;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Component("basebluHandler")
@Slf4j
public class BasebluHandler implements ISupplierHandler {
	
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
				Item item = JsonUtil.deserialize(message.getData(), Item.class);
				String supplierId = message.getSupplierId();
				
				mongoService.save(supplierId, item.getSku_brand()+item.getColor(), item);
				
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(supplierId,item,hubSpu,message.getData());
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();

				String size_stock_barcode = item.getSize_stock_barcode();
				String[] strs = size_stock_barcode.split("\\|");
				for(int i=0;i<strs.length;i++) {
					if (i % 3 == 0) {
						HubSupplierSkuDto hubSku = new HubSupplierSkuDto();

						boolean skuSuc = convertSku(supplierId,hubSpu.getSupplierSpuId(),item,hubSku,strs[i],strs[i+1],strs[i+2]);
						if(skuSuc){
							hubSkus.add(hubSku);
						}
					}
				}



				//处理图片
//				SupplierPicture supplierPicture = null;
//				if(pictureHandler.isCurrentSeason(supplierId, hubSpu.getSupplierSeasonname())){
				SupplierPicture	supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, converImage(item));
//				}
				if(success){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}	
		} catch (Exception e) {
			log.error("baseblu异常："+e.getMessage(),e);
		}
		
	}
	
	/**
	 * 处理图片
	 * @param itemImages
	 * @return
	 */
	private List<Image> converImage(Item itemImages){

		List<Image> images = new ArrayList<Image>();
		String urls = itemImages.getUrl_pictures();
		if(!StringUtils.isEmpty(urls)){
			for(String url : urls.split("\\|")){
				Image image = new Image();
				image.setUrl(url);
				images.add(image);
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
	public boolean convertSpu(String supplierId,Item item, HubSupplierSpuDto hubSpu,String data) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(item.getSku_brand()+item.getColor());
			String color_code = item.getCod_colore_brand();
			if(color_code.length() == 1){
				color_code = "00"+color_code;
			}else if(color_code.length() == 2){
				color_code = "0"+color_code;
			}
			hubSpu.setSupplierSpuModel(item.getSku_brand()+"  "+color_code);
			hubSpu.setSupplierSpuName(standard(item.getShort_description()));
			hubSpu.setSupplierSpuColor(item.getColor());
			hubSpu.setSupplierGender(item.getGender());
			hubSpu.setSupplierCategoryname(item.getCategory());
			hubSpu.setSupplierBrandname(item.getBrand());
			hubSpu.setSupplierSeasonname(item.getSeason());
			hubSpu.setSupplierMaterial(item.getMaterial());
			hubSpu.setSupplierOrigin(item.getMade_in());
			hubSpu.setSupplierSpuDesc(standard(item.getLong_description()));
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
	public boolean convertSku(String supplierId, Long supplierSpuId,Item item, HubSupplierSkuDto hubSku,String size,String quantity,String barcode) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){


				hubSku.setSupplierSpuId(supplierSpuId);
				hubSku.setSupplierId(supplierId);
				hubSku.setSupplierSkuNo(barcode);
				hubSku.setSupplierBarcode(barcode);
				hubSku.setSupplierSkuName(standard(item.getShort_description()));
				hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(item.getRtl_price())));
				hubSku.setMarketPriceCurrencyorg("EUR");
				hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(item.getSupply_price())));
				hubSku.setSupplierSkuSize(size);
				hubSku.setStock(StringUtil.verifyStock(quantity));






			return true;
		}else{
			return false;
		}
	}

	private static String standard(String origine){
		if(!StringUtils.isEmpty(origine)){
			return origine.replaceAll("\n", "").replaceAll("\r", "").trim();
		}else{
			return "";
		}
	}

}
