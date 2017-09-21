package com.shangpin.supplier.product.consumer.supplier.fratinardi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.supplier.product.consumer.service.SupplierProductMongoService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.forzieri.dto.CategoryMap;
import com.shangpin.supplier.product.consumer.supplier.fratinardi.dto.Item;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:StefaniaHandler.java </p>
 * <p>Description: fratinardi供货商原始数据处理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijang
 * @date 2016年12月8日 上午11:36:22
 */
@Component("fratinardi")
@Slf4j
public class FratinardiHandler implements ISupplierHandler{
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;
	@Autowired
	private SupplierProductMongoService mongoService;
	Map<String, String> categoryMap = new CategoryMap(new HashMap<String,String>()).getCategoryMap();
	
	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			Gson gson = new Gson();
			if(!StringUtils.isEmpty(message.getData())){
				log.info(message.getData());
				Item jsonObject = gson.fromJson(message.getData(),Item.class);
				String supplierId = message.getSupplierId();
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				String supplierSpuNo = splitSupplierSpuNo(jsonObject.getProduct_No());
				jsonObject.setProduct_No(supplierSpuNo);
				log.info("supplierSpuNo:"+supplierSpuNo);
				List<Image> images = converImage(supplierId,jsonObject);
//				if(null == images){
//					hubSpu.setIsexistpic(Isexistpic.NO.getIndex());
//				}else{
//					hubSpu.setIsexistpic(Isexistpic.YES.getIndex()); 
//				}
				boolean success = convertSpu(supplierId, jsonObject, hubSpu,message.getData());
				mongoService.save(supplierId, hubSpu.getSupplierSpuNo(), jsonObject);
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				boolean skuSucc = convertSku(supplierId, hubSpu.getSupplierSpuId(), jsonObject, hubSku);
				if(skuSucc){
					hubSkus.add(hubSku);
				}
				//处理图片
				SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
				if(success){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}	
		} catch (Exception e) {
			log.error("fratinardi异常："+e.getMessage(),e); 
		}		
		
	}
	
	private static String splitSupplierSpuNo(String spuNo) {
		if(spuNo!=null){
			String [] arr = spuNo.split("-");
			if(arr.length>0){
				spuNo = arr[0];
				if(spuNo!=null){
					String [] arr2 = spuNo.split("_");
					if(arr2.length>0){
						spuNo = arr2[0];
					}
				}
			}
		}
		return spuNo;
	}
	
	/**
	 * stefania处理图片
	 * @param stefPicture
	 * @return
	 */
	private List<Image> converImage(String supplierId,Item jsonObject){
		String supplierSpuNo =jsonObject.getProduct_No();
		Map<String,String> existPics = pictureHandler.checkPicExistsOfSpu(supplierId, supplierSpuNo);
		String picture0 = jsonObject.getImage1();
		String picture1 = jsonObject.getImage2();
		String picture2 = jsonObject.getImage3();
		String picture3 = jsonObject.getImage4();
		List<Image> images = new ArrayList<Image>();
		if(org.apache.commons.lang.StringUtils.isNotBlank(picture0)&&!existPics.containsKey(picture0)){
			log.info("fratinardi "+picture0+" 将推送");
			Image image = new Image();
			image.setUrl(picture0);
			images.add(image);
		}else{
			log.info("XXXXXXXXX fratinardi "+picture0+" 已存在XXXXXXXXXXXX");
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(picture1)&&!existPics.containsKey(picture1)){
			log.info("fratinardi "+picture1+" 将推送");
			Image image = new Image();
			image.setUrl(picture1);
			images.add(image);
		}else{
			log.info("XXXXXXXXX fratinardi "+picture1+" 已存在XXXXXXXXXXXX");
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(picture2)&&!existPics.containsKey(picture2)){
			log.info("fratinardi "+picture2+" 将推送");
			Image image = new Image();
			image.setUrl(picture2);
			images.add(image);
		}else{
			log.info("XXXXXXXXX fratinardi "+picture2+" 已存在XXXXXXXXXXXX");
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(picture3)&&!existPics.containsKey(picture3)){
			log.info("fratinardi "+picture3+" 将推送");
			Image image = new Image();
			image.setUrl(picture3);
			images.add(image);
		}else{
			log.info("XXXXXXXXX fratinardi "+picture3+" 已存在XXXXXXXXXXXX");
		}
		return images;
	}
	
	/**
	 * 将stefania原始数据转换成hub spu
	 * @param supplierId 供应商门户编号
	 * @param stefProduct stef 原始dto
	 * @param stefItem stef 原始dto
	 * @param hubSpu hub spu
	 * @return
	 */
	public boolean convertSpu(String supplierId,Item ob,HubSupplierSpuDto hubSpu,String data){
		
		if(null != ob && ob != null){
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(ob.getProduct_No());
			hubSpu.setSupplierSpuModel(ob.getProduct_No());
			hubSpu.setSupplierSpuName(ob.getName());
			hubSpu.setSupplierSpuColor(ob.getColor());
			hubSpu.setSupplierGender(ob.getGender());
			hubSpu.setSupplierCategoryname(ob.getCategory());
			hubSpu.setSupplierBrandname(ob.getBrand());
			hubSpu.setSupplierSeasonname(ob.getSeason());
			hubSpu.setSupplierMaterial(ob.getMaterials());
			String desc = ob.getDescription();
			String origin = null;
			if(desc!=null&&desc.contains("Made in")){
				String [] arr = desc.split("<br>");
				for(String s :arr){
					if(s.contains("Made in")){
						origin = s.substring(s.indexOf("Made in")+7).trim();
						break;
					}
				}
			}
			hubSpu.setSupplierOrigin(origin);
			hubSpu.setSupplierSpuDesc(desc);
			return true;
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
	public boolean convertSku(String supplierId,Long supplierSpuId,Item ob,HubSupplierSkuDto hubSku){
		if(null != ob){
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(ob.getSku_No());
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(ob.getMarket_price())));
			hubSku.setSupplierBarcode(ob.getBarcode());
			hubSku.setSupplierSkuSize(ob.getSize()); 
			hubSku.setStock(StringUtil.verifyStock(ob.getQty()));
			return true;
		}else{
			return false;
		}
	}
}
