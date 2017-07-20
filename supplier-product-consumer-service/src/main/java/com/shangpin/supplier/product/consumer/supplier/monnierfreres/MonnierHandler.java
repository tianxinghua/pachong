package com.shangpin.supplier.product.consumer.supplier.monnierfreres;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.monnierfreres.dto.Product;

import lombok.extern.slf4j.Slf4j;

@Component("monnierHandler")
@Slf4j
public class MonnierHandler implements ISupplierHandler {
	
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
				Product item = JsonUtil.deserialize(message.getData(), Product.class);
				String supplierId = message.getSupplierId();
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(supplierId,item,hubSpu,message.getData());
				
				mongoService.save(supplierId, hubSpu.getSupplierSpuNo(), item);
				
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				boolean skuSuc = convertSku(supplierId,hubSpu.getSupplierSpuId(),item,hubSku);
				if(skuSuc){
					hubSkus.add(hubSku);
				}
				//处理图片				
//				SupplierPicture supplierPicture = null;
//				if(pictureHandler.isCurrentSeason(supplierId, hubSpu.getSupplierSeasonname())){
				SupplierPicture	supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, converImage(supplierId,item));
//				}
				if(success){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
			log.error("monnier异常："+e.getMessage(),e); 
		}
		
	}
	
	/**
	 * monnier处理图片
	 * @param itemImages
	 * @return
	 */
	private List<Image> converImage(String supplierId,Product itemImages){
		
		String[] img = new String[5];
		if (!StringUtils.isEmpty((itemImages.getImage_url_1()))) {
			img[0] = itemImages.getImage_url_1();
		}
		if (!StringUtils.isEmpty((itemImages.getImage_url_2()))) {
			img[1] = itemImages.getImage_url_2();
		}
		if (!StringUtils.isEmpty((itemImages.getImage_url_3()))) {
			img[2] = itemImages.getImage_url_3();
		}
		if (!StringUtils.isEmpty((itemImages.getImage_url_4()))) {
			img[3] = itemImages.getImage_url_4();
		}
		if (!StringUtils.isEmpty((itemImages.getImage_url_5()))) {
			img[4] = itemImages.getImage_url_5();
		}
		
		Map<String,String> existPics = null;
		String sku = itemImages.getSku();
		if(StringUtils.isNotBlank(sku)){
			String supplierSpuNo = null;
			if(sku.length()==19){
				supplierSpuNo = sku.substring(0,16);
			}
			if(sku.length()==12){
				supplierSpuNo = sku.substring(0,9);
			}
			if(supplierSpuNo!=null){
				existPics = pictureHandler.monnerPicExistsOfSpu(supplierId, supplierSpuNo);	
			}
		}
		
		List<Image> images = new ArrayList<Image>();
		if(null != itemImages){			
			if(img.length>0){
				for(String url : img){
					if(!StringUtils.isEmpty(url) && !url.contains("_COLOR")){//将带 _COLOR 的小图屏蔽掉
						
						if(existPics!=null&&(sku.length()==19||sku.length()==12)){
							String picurl = url.substring(0,url.length()-11)+url.substring(url.length()-8);
							if(existPics.containsKey(picurl)){
								continue;
							}
						}
						Image image = new Image();
						image.setUrl(url);
						images.add(image);
					}
				}
			}
		}
		return images;
	}
	/**
	 * 将monnier原始dto转换成hub spu
	 * @param supplierId 供应商门户id
	 * @param item 供应商原始dto
	 * @param hubSpu hub spu表
	 */
	public boolean convertSpu(String supplierId,Product item, HubSupplierSpuDto hubSpu,String data) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			String supplierSpuNo = getPre9OfSku(item.getSku());
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(supplierSpuNo);
			String spuModel = "";
			if(!StringUtils.isEmpty(item.getPvr_model()) && !StringUtils.isEmpty(item.getPvr_color())){
				spuModel = item.getPvr_model() + " "+item.getPvr_color();
			}else{
				if(supplierSpuNo.contains("CONFIG")){
					spuModel = supplierSpuNo.substring(7);	
				}else{
					spuModel = supplierSpuNo;
				}
			}
			hubSpu.setSupplierSpuModel(spuModel);
			hubSpu.setSupplierSpuName(item.getName());
			hubSpu.setSupplierSpuColor(item.getColor());
			hubSpu.setSupplierGender(item.getGender());
			hubSpu.setSupplierCategoryname(item.getType());
			hubSpu.setSupplierBrandname(item.getBrand());
			hubSpu.setSupplierSeasonname(item.getFashioncollection());
			hubSpu.setSupplierGender("female");
			hubSpu.setSupplierMaterial(item.getMaterial());
			hubSpu.setSupplierOrigin(item.getOrigin());
			hubSpu.setSupplierSpuDesc(item.getDescription());
			hubSpu.setMemo(data);
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 将monnier原始dto转换成hub sku
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
			String size = "";
			String supplierSkuNo = item.getSku();
			hubSku.setSupplierSkuNo(supplierSkuNo);
			hubSku.setSupplierSkuName(item.getName());
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(item.getPrice_before_discount())));
			hubSku.setMarketPriceCurrencyorg("EUR");
			if(supplierSkuNo.contains("CONFIG")){
				supplierSkuNo = supplierSkuNo.substring(7);
			}
			if(supplierSkuNo.length()==12){
				size = supplierSkuNo.substring(9);
				if(size.endsWith("0")){
					size = size.substring(0, 2);
				}else{
					size = size.substring(0,2)+"."+size.substring(2);	
				}
				if(size.startsWith("0")){
					size = size.substring(1);
				}
				Double d1 = new Double(9);
				Double d2 = new Double(11);
				if(d1.compareTo(Double.parseDouble(size))<0&&d2.compareTo(Double.parseDouble(size))>0){
					log.info("【supplierSkuNo:"+supplierSkuNo+"尺码大于9小于11被排除");
					return false;	
				}
				Double d3 = new Double(39);
				if(d3.compareTo(Double.parseDouble(size))<0){
					log.info("【supplierSkuNo:"+supplierSkuNo+"尺码大于39被排除");
					return false;	
				}
			}else{
				size = item.getSize();
			}
			hubSku.setSupplierSkuSize(size);
			hubSku.setStock(StringUtil.verifyStock(item.getQty()));
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 截取前9位
	 * @param sku
	 * @return
	 */
	private static String getPre9OfSku(String sku){
		
		if(StringUtils.isNotBlank(sku) ||sku.contains("CONFIG")){
			if(sku.length()==19){
				return sku.substring(0,16);
			}
			if(sku.length()==16){
				return sku.substring(0,16);
			}
		}
		if(StringUtils.isEmpty(sku) || sku.length() < 9){
			return sku;
		}else{
			return sku.substring(0, 9);
		}
	}
}
