package com.shangpin.supplier.product.consumer.supplier.common.picture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.season.gateway.HubSeasonDicGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.ProductPicture;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.supplier.product.consumer.util.DateTimeUtil;
import com.shangpin.supplier.product.consumer.util.UUIDGenerator;
/**
 * <p>Title:PictureHandler </p>
 * <p>Description: 图片处理器 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2017年1月3日 下午2:30:48
 *
 */
@Component
public class PictureHandler {
	
	@Autowired
	private HubSeasonDicGateWay seasonClient;
	@Autowired
	private HubSpuPendingPicGateWay picClient;

	/**
	 * 初始化发送图片消息体
	 * @param message 供应商原始消息
	 * @param hubSpu
	 * @param images
	 * @return
	 */
	public SupplierPicture initSupplierPicture(SupplierProduct message,HubSupplierSpuDto hubSpu,List<Image> images){
		SupplierPicture supplierPicture = new SupplierPicture();
		supplierPicture.setMessageId(UUIDGenerator.getUUID());
		supplierPicture.setMessageDate(DateTimeUtil.getDateTime());
		supplierPicture.setSupplierId(message.getSupplierId());
		supplierPicture.setSupplierName(message.getSupplierName());
		ProductPicture productPicture = new ProductPicture();
		productPicture.setSupplierSpuNo(hubSpu.getSupplierSpuNo());
		productPicture.setImages(images); 
		supplierPicture.setProductPicture(productPicture);
		return supplierPicture;
	}
	
	/**
	 * 是否当前季
	 * @param supplierId
	 * @param supplierSeason
	 * @return
	 */
	public boolean isCurrentSeason(String supplierId,String supplierSeason){
		if(StringUtils.isEmpty(supplierSeason)){
			return false;
		}
		Map<String,String> currentSeason =  null;
		if(null == currentSeason){
			currentSeason = new HashMap<String,String>();
			HubSeasonDicCriteriaDto criteriaDto = new HubSeasonDicCriteriaDto();
			criteriaDto.createCriteria().andSupplieridEqualTo(supplierId).andFilterFlagEqualTo((byte)1);
			List<HubSeasonDicDto> dics = seasonClient.selectByCriteria(criteriaDto);
			if(null != dics && dics.size() > 0){
				for(HubSeasonDicDto dic : dics){
					currentSeason.put(dic.getSupplierSeason().trim().toUpperCase(), null);
				}
			}
		}
		if(currentSeason.containsKey(supplierSeason.trim().toUpperCase())){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断该spu是否存在图片
	 * @param supplierSpuId
	 * @return
	 */
	public Map<String,String> stefaniaPicExistsOfSpu(String supplierId,String supplierSpuNo){
		Map<String,String> existPics = new HashMap<String,String>();
		HubSpuPendingPicCriteriaDto dto = new HubSpuPendingPicCriteriaDto();
		dto.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo);
		List<HubSpuPendingPicDto> pics =  picClient.selectByCriteria(dto);
		if(null != pics && pics.size() >0){
			String picurl = "";
			for(HubSpuPendingPicDto picDto : pics){
				picurl =  picDto.getPicUrl();
				picurl = picurl.substring(0, picurl.indexOf("&U"));
				existPics.put(picurl, null);
			}
		}
		return existPics;
	}
}
