package com.shangpin.supplier.product.consumer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.message.picture.ProductPicture;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.supplier.product.consumer.conf.stream.source.sender.PictureProductStreamSender;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:PictureProductService </p>
 * <p>Description: 供应商处理图片service </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2017年1月3日 下午4:29:27
 *
 */
@Service
@Slf4j
public class PictureProductService {

	@Autowired
	private PictureProductStreamSender pictureProductStreamSender;
	@Autowired
	private HubSpuPendingPicGateWay picClient;
	
	/**
	 * 发送供应商图片到图片消息队列
	 * @param supplierPicture
	 * @param headers
	 */
	public void sendSupplierPicture(SupplierPicture supplierPicture, Map<String, ?> headers){
		try {
			if(toPush(supplierPicture)){
				boolean result = pictureProductStreamSender.supplierPictureProductStream(supplierPicture, headers);
				log.info(supplierPicture.getSupplierName()+":"+supplierPicture.getSupplierSpuId()+" 发送图片 "+result);
			}else{
				log.info(supplierPicture.getSupplierName()+":"+supplierPicture.getSupplierSpuId()+" 下所有图片已存在，不推送");
			}
		} catch (Exception e) {
			log.error(supplierPicture.getSupplierName()+":"+supplierPicture.getProductPicture().getSupplierSpuNo()+" 发送图片异常："+e.getMessage(),e); 
		}
	}
	
	private boolean toPush(SupplierPicture supplierPicture){
		if(null == supplierPicture || null == supplierPicture.getProductPicture() || null == supplierPicture.getProductPicture().getImages() || supplierPicture.getProductPicture().getImages().size() == 0){
			return false;
		}else{
			Map<String,String> pics = findHubSpuPendingPics(supplierPicture.getSupplierSpuId());
			List<Image> images = new ArrayList<Image>();
			for(Image image : supplierPicture.getProductPicture().getImages()){
				if(!pics.containsKey(image.getUrl())){
					images.add(image);
				}
			}
			if(images.size() > 0){
				ProductPicture productPicture = new ProductPicture();
				productPicture.setSupplierSpuNo(supplierPicture.getProductPicture().getSupplierSpuNo()); 
				productPicture.setImages(images); 
				supplierPicture.setProductPicture(productPicture);
				return true;
			}else{
				return false;
			}
			
		}
	}
	
	private Map<String,String> findHubSpuPendingPics(Long supplierSpuId){
		HubSpuPendingPicCriteriaDto dto = new HubSpuPendingPicCriteriaDto();
		dto.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId);
		List<HubSpuPendingPicDto> pics =  picClient.selectByCriteria(dto);
		if(null != pics && pics.size() >0){
			Map<String,String> retMap = new HashMap<String,String>();
			for(HubSpuPendingPicDto picDto : pics){
				retMap.put(picDto.getPicUrl(), null);
			}
			return retMap;
		}else{
			return new HashMap<String,String>();
		}
	}
}
