package com.shangpin.supplier.product.consumer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.enumeration.PicHandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.SupplierValueMappingType;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicWithCriteriaDto;
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
	@Autowired
	HubSupplierValueMappingGateWay mappingGateWay;

	/**
	 * 发送供应商图片到图片消息队列
	 * @param supplierPicture
	 * @param headers
	 */
	public void sendSupplierPicture(SupplierPicture supplierPicture, Map<String, ?> headers){
		try {
			if(toPush(supplierPicture)){
				
				Map<String, String> tmpBrandSupplierMap = new HashMap<>();
	            HubSupplierValueMappingCriteriaDto criteriaDto = new HubSupplierValueMappingCriteriaDto();
	            criteriaDto.setPageNo(1);
	            criteriaDto.setPageSize(10000);
	            criteriaDto.createCriteria().andHubValTypeEqualTo(SupplierValueMappingType.TYPE_BRAND_SUPPLIER.getIndex().byteValue()).andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
	            List<HubSupplierValueMappingDto> hubSupplierValueMappingDtos = mappingGateWay.selectByCriteria(criteriaDto);
	            hubSupplierValueMappingDtos.forEach(mapping ->{
	                tmpBrandSupplierMap.put(mapping.getSupplierId(),mapping.getSupplierId());
	            });
			
				boolean result = false;
				if(tmpBrandSupplierMap.containsKey(supplierPicture.getSupplierId())){
					result = pictureProductStreamSender.brandPictureProductStream(supplierPicture, headers);	
				}else if("2015092401528".equals(supplierPicture.getSupplierId())){
					result = pictureProductStreamSender.stefaniamodePictureProductStream(supplierPicture, headers);
				}else if("2016111101957".equals(supplierPicture.getSupplierId())){
					result = pictureProductStreamSender.mclablesPictureProductStream(supplierPicture,headers);
				}else if("2018101602078".equals(supplierPicture.getSupplierId())){
					result = pictureProductStreamSender.eraldoPictureProductStream(supplierPicture,headers);
				}else {
					result = pictureProductStreamSender.supplierPictureProductStream(supplierPicture, headers);
				}
				
				log.info(supplierPicture.getSupplierName()+":"+supplierPicture.getSupplierSpuId()+" 发送图片 "+result);
			}else{
				log.info(supplierPicture.getSupplierName()+":"+supplierPicture.getSupplierSpuId()+" 下所有图片已存在，不推送");
			}
		} catch (Exception e) {
			log.error(supplierPicture.getSupplierName()+":"+supplierPicture.getProductPicture().getSupplierSpuNo()+" 发送图片异常："+e.getMessage()); 
		}
	}
	/**
	 * 判断图片是否发送，会过滤掉已完成的图片，只发送新的，或者下载失败的图片
	 * @param supplierPicture
	 * @return
	 */
	private boolean toPush(SupplierPicture supplierPicture){
//		if(null!=supplierPicture){
//			log.info("pic = " + supplierPicture.toString());
//		}

		if(null == supplierPicture || null == supplierPicture.getProductPicture() || null == supplierPicture.getProductPicture().getImages() || supplierPicture.getProductPicture().getImages().size() == 0){
			return false;
		}else{
			Map<String,String> supplierUrls = Maps.newHashMap();
			Map<String,String> pics = findHubSpuPendingPics(supplierPicture.getSupplierSpuId());
			/**
			 * 首先吧原始数据中的链接，已经在库里存在的排除掉，返回一个未处理的集合
			 */
			List<Image> images = new ArrayList<Image>();
			String imageUrl = "";
			for(Image image : supplierPicture.getProductPicture().getImages()){
				imageUrl = image.getUrl();
				if(!StringUtils.isEmpty(imageUrl) && (imageUrl.startsWith("http") || imageUrl.startsWith("HTTP") ||
						imageUrl.startsWith("ftp")) && !pics.containsKey(imageUrl)){
					images.add(image);
				}
				supplierUrls.put(imageUrl, null);
			}
			/**
			 * 其次吧库里的链接，在原始数据中不存在的进行逻辑删除（dataState更新为0）
			 */
			if(pics.size() > 0){
				List<String> deletedUrls = Lists.newArrayList();
				pics.keySet().forEach(url -> add(url, supplierUrls, deletedUrls));
				if(deletedUrls.size() > 0){
					updateDataStateToDelete(supplierPicture.getSupplierSpuId(), deletedUrls);
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
	/**
	 * 查找已处理的供应商图片链接
	 * @param supplierSpuId
	 * @return
	 */
	private Map<String,String> findHubSpuPendingPics(Long supplierSpuId){
		HubSpuPendingPicCriteriaDto dto = new HubSpuPendingPicCriteriaDto();
		dto.setPageNo(1);
		dto.setPageSize(10000);
		dto.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId).andPicHandleStateEqualTo(PicHandleState.HANDLED.getIndex()).andDataStateNotEqualTo(Byte.valueOf("0")); 
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
	
	private void add(String url, Map<String,String> supplierUrls, List<String> deletedUrls){
		if(!supplierUrls.containsKey(url)){
			log.info("【供应商链接"+url+"供应商不再维护，即将逻辑删除】"); 
			deletedUrls.add(url);
		}
	}
	
	private void updateDataStateToDelete(Long supplierSpuId, List<String> deletedUrls){
		HubSpuPendingPicWithCriteriaDto withCriteria = new HubSpuPendingPicWithCriteriaDto();
		HubSpuPendingPicCriteriaDto criteria =  new HubSpuPendingPicCriteriaDto();
		criteria.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId).andPicUrlIn(deletedUrls);
		withCriteria.setCriteria(criteria );
		HubSpuPendingPicDto hubSpuPendingPic =  new HubSpuPendingPicDto();
		hubSpuPendingPic.setDataState(DataState.DELETED.getIndex());
		hubSpuPendingPic.setMemo("供应商不维护已删除"); 
		hubSpuPendingPic.setUpdateTime(new Date());
		withCriteria.setHubSpuPendingPic(hubSpuPendingPic );
		picClient.updateByCriteriaSelective(withCriteria );
	}
	/**
	 * 将供应商不在维护图片的spu，逻辑删除原有的图片链接
	 * @param supplierId 供应商门户编号
	 * @param supplierSpuNo 供应商spu编号
	 */
	public void updateDataStateToDelete(String supplierId, String supplierSpuNo){
		if(count(supplierId,supplierSpuNo) > 0){
			HubSpuPendingPicWithCriteriaDto withCriteria = new HubSpuPendingPicWithCriteriaDto();
			HubSpuPendingPicCriteriaDto criteria =  new HubSpuPendingPicCriteriaDto();
			criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo); 
			withCriteria.setCriteria(criteria );
			HubSpuPendingPicDto hubSpuPendingPic =  new HubSpuPendingPicDto();
			hubSpuPendingPic.setDataState(DataState.DELETED.getIndex());
			hubSpuPendingPic.setMemo("供应商不维护已删除"); 
			hubSpuPendingPic.setUpdateTime(new Date());
			withCriteria.setHubSpuPendingPic(hubSpuPendingPic );
			picClient.updateByCriteriaSelective(withCriteria );
		}
	}
	
	/**
	 * 根据供应商门户编号和供应商spu编号查找图片数量
	 * @param supplierId
	 * @param supplierSpuNo
	 * @return
	 */
	private int count(String supplierId, String supplierSpuNo){
		HubSpuPendingPicCriteriaDto criteria = new HubSpuPendingPicCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo).andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
		return picClient.countByCriteria(criteria );
	}
}
