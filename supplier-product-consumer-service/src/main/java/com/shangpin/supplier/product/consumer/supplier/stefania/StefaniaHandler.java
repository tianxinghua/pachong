package com.shangpin.supplier.product.consumer.supplier.stefania;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerException;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.stefania.dto.StefItem;
import com.shangpin.supplier.product.consumer.supplier.stefania.dto.StefProduct;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:StefaniaHandler.java </p>
 * <p>Description: stefania供货商原始数据处理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijang
 * @date 2016年12月8日 上午11:36:22
 */
@Component("stefaniaHandler")
@Slf4j
public class StefaniaHandler implements ISupplierHandler{
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;
	
	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(StringUtils.isEmpty(message.getData())){
				StefProduct stefProduct = JsonUtil.deserialize(message.getData(), StefProduct.class);
				for(StefItem stefItem :stefProduct.getItems().getItems()){
					HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
					List<Image> images = converImage(stefItem.getPicture());
					if(null == images){
						hubSpu.setIsexistpic(PicState.NO_PIC.getIndex());
					}else{
						hubSpu.setIsexistpic(PicState.PIC_INFO_COMPLETED.getIndex()); 
					}
					boolean success = convertSpu(message.getSupplierId(), stefProduct, stefItem, hubSpu);
					List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
					HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
					boolean skuSucc = convertSku(message.getSupplierId(), hubSpu.getSupplierSpuId(), stefItem, hubSku);
					if(skuSucc){
						hubSkus.add(hubSku);
					}
					//处理图片
					SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
					if(success){
						supplierProductSaveAndSendToPending.stefaniaSaveAndSendToPending(message.getSupplierNo(),message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
					}
				}
			}	
		} catch (EpHubSupplierProductConsumerException e) {
			log.error("stefania异常："+e.getMessage(),e); 
		}		
		
	}
	
	/**
	 * stefania处理图片
	 * @param stefPicture
	 * @return
	 */
	private List<Image> converImage(String stefPicture){
		if(StringUtils.isEmpty(stefPicture)){
			return null;
		}else{
			List<Image> images = new ArrayList<Image>();
			String[] picArray = stefPicture.split("\\|");
			for(String url : picArray){
				Image image = new Image();
				image.setUrl(url);
				images.add(image);
			}
			return images;
		}
		
	}
	
	/**
	 * 将stefania原始数据转换成hub spu
	 * @param supplierId 供应商门户编号
	 * @param stefProduct stef 原始dto
	 * @param stefItem stef 原始dto
	 * @param hubSpu hub spu
	 * @return
	 */
	public boolean convertSpu(String supplierId,StefProduct stefProduct,StefItem stefItem,HubSupplierSpuDto hubSpu){
		if(null != stefProduct && stefItem != null){
			String productModle = findProductModleByItemId(stefItem.getItem_id());
			if(!StringUtils.isEmpty(productModle)){
				hubSpu.setSupplierId(supplierId);
				hubSpu.setSupplierSpuNo(productModle);
				hubSpu.setSupplierSpuModel(productModle);
				hubSpu.setSupplierSpuName(stefProduct.getProduct_name());
				hubSpu.setSupplierSpuColor(stefItem.getColor());
				hubSpu.setSupplierGender(stefProduct.getMain_category());
				hubSpu.setSupplierCategoryname(stefProduct.getCategory());
				hubSpu.setSupplierBrandname(stefProduct.getProduct_brand());
				hubSpu.setSupplierSeasonname(stefProduct.getSeason_code());
				hubSpu.setSupplierMaterial(stefProduct.getProduct_material());
				hubSpu.setSupplierOrigin(stefProduct.getMade_in());
				hubSpu.setSupplierSpuDesc(stefItem.getDescription());
				return true;
			}else{
				return false;
			}
			
		}else{			
			return false;
		}
	}
	
	/**
	 * 将stefania原始数据转换成hub sku
	 * @param supplierId
	 * @param supplierSpuId
	 * @param stefItem
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId,Long supplierSpuId,StefItem stefItem,HubSupplierSkuDto hubSku){
		if(null != stefItem){
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(stefItem.getItem_id().replaceAll("½", "+"));
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(stefItem.getMarket_price())));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(stefItem.getSupply_price())));
			hubSku.setSalesPrice(new BigDecimal(StringUtil.verifyPrice(stefItem.getSell_price())));
			hubSku.setSupplierBarcode(stefItem.getBarcode());
			hubSku.setSupplierSkuSize(stefItem.getItem_size().replaceAll("½", "+"));
			hubSku.setStock(StringUtil.verifyStock(stefItem.getStock()));
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 根据item_id截取货号
	 * @param item_id
	 * @return
	 */
	private String findProductModleByItemId(String item_id){
		//00140-05#0422A#742######S
		if(!StringUtils.isEmpty(item_id)){
			if(item_id.contains("##")){
				return item_id.substring(0, item_id.indexOf("##"));
			}
		}
		return "";
	}
	
}
