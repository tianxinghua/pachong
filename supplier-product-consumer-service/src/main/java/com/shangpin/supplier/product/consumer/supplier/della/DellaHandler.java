package com.shangpin.supplier.product.consumer.supplier.della;

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
import com.shangpin.supplier.product.consumer.supplier.della.dto.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("dellaHandler")
@Slf4j
public class DellaHandler implements ISupplierHandler {
	
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
				
				mongoService.save(supplierId, item.getSupplier_item_code(), item);
				
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				boolean success = convertSpu(supplierId,item,hubSpu);
				if(success){
					HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
					boolean succSku = convertSku(supplierId,item,hubSku);
					if(succSku){
						hubSkus.add(hubSku);
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
			log.error("della异常："+e.getMessage(),e);
		}
		
	}
	
	/**
	 * 处理图片
	 * @param item
	 * @return
	 */
	private List<Image> converImage(Item item){

		List<Image> images = new ArrayList<Image>();
		List<String> listPics = new ArrayList<String>();
		if(org.apache.commons.lang.StringUtils.isNotBlank(item.getLINK_PHOTO_1())){
			listPics.add(item.getLINK_PHOTO_1());
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(item.getLINK_PHOTO_2())){
			listPics.add(item.getLINK_PHOTO_2());
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(item.getLINK_PHOTO_3())){
			listPics.add(item.getLINK_PHOTO_3());
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(item.getLINK_PHOTO_4())){
			listPics.add(item.getLINK_PHOTO_4());
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(item.getLINK_PHOTO_5())){
			listPics.add(item.getLINK_PHOTO_5());
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(item.getLINK_PHOTO_6())){
			listPics.add(item.getLINK_PHOTO_6());
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(item.getLINK_PHOTO_7())){
			listPics.add(item.getLINK_PHOTO_7());
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(item.getLINK_PHOTO_8())){
			listPics.add(item.getLINK_PHOTO_8());
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(item.getLINK_PHOTO_9())){
			listPics.add(item.getLINK_PHOTO_9());
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(item.getLINK_PHOTO_10())){
			listPics.add(item.getLINK_PHOTO_10());
		}
		if(!StringUtils.isEmpty(listPics)){
			for(String url : listPics){
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
	public boolean convertSpu(String supplierId,Item item, HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuModel(item.getSupplier_item_code().replaceAll("\"", "").trim()+"-"+item.getColor_code().replaceAll("\"", "").trim());
			hubSpu.setSupplierSpuNo(hubSpu.getSupplierSpuModel());
			hubSpu.setSupplierSpuColor(item.getColor_description().replaceAll("\"", ""));
			hubSpu.setSupplierGender(null==item.getGender()?"":item.getGender().replaceAll("\"", ""));
			hubSpu.setSupplierCategoryname(null==item.getBrand_line()?"":item.getBrand_line().replaceAll("\"", ""));
			hubSpu.setSupplierBrandname(null==item.getBrand_name()?"":item.getBrand_name().replaceAll("\"", ""));
			hubSpu.setSupplierSeasonname(null==item.getSeason()?"":item.getSeason().trim());
			String material = item.getItem_detailed_info();
			try{
				material = material.substring(material.lastIndexOf(":")+1);
			}catch(Exception e){
				e.printStackTrace();
			}
			hubSpu.setSupplierMaterial(material);
			hubSpu.setSupplierOrigin(null==item.getMade_in()?"":item.getMade_in().replaceAll("\"", ""));
			hubSpu.setSupplierSpuDesc(standard(item.getItem_detailed_info().replaceAll(",", ".").replaceAll("\"", "")));
			hubSpu.setSupplierSpuName((item.getBrand_name()+" "+(null==item.getBrand_line()?"":item.getBrand_line()).replaceAll("\"", "")));




			return true;
		}else{
			return false;
		}
	}
	/**
	 * 将geb原始dto转换成hub sku
	 * @param supplierId   	 *
	 * @param item
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId,Item item, HubSupplierSkuDto hubSku) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(item.getItem_code().trim());
			hubSku.setSupplierBarcode(item.getItem_code()); //
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(item.getRetail_price())));
			hubSku.setSalesPrice(new BigDecimal(StringUtil.verifyPrice(item.getSold_price())));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(item.getYour_price())));
			hubSku.setSupplierSkuSize(item.getSize().replaceAll("\"", "").replaceAll(",", "."));
			hubSku.setStock(StringUtil.verifyStock(item.getQuantity()));
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
