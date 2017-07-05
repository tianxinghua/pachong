package com.shangpin.ephub.product.business.ui.studio.imageupload.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.studio.pic.dto.HubSlotSpuPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.pic.dto.HubSlotSpuPicDto;
import com.shangpin.ephub.client.data.mysql.studio.pic.dto.HubSlotSpuPicWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.pic.gateway.HubSlotSpuPicGateway;
import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.studio.common.operation.dto.OperationQuery;
import com.shangpin.ephub.product.business.ui.studio.common.operation.enumeration.OperationQueryType;
import com.shangpin.ephub.product.business.ui.studio.common.operation.service.OperationService;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.StudioSlotVo;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.detail.StudioSlotSpuSendDetailVo;
import com.shangpin.ephub.product.business.ui.studio.common.pictrue.service.PictureService;
import com.shangpin.ephub.product.business.ui.studio.imageupload.service.ImageUploadService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageUploadServiceImpl implements  ImageUploadService{
	
	@Autowired
	private OperationService operationService;
	@Autowired
	private HubSlotSpuPicGateway hubSlotSpuPicGateway;
	@Autowired
	private PictureService pictureService;

	@Override
	public List<StudioSlotVo> list(OperationQuery operationQuery) {
		try {
			operationQuery.setOperationQueryType(OperationQueryType.IMAGE_UPLOAD.getIndex());
			log.info("图片上传页面接收到的参数："+JsonUtil.serialize(operationQuery)); 
			List<StudioSlotDto>  list = operationService.slotList(operationQuery);
			
			List<StudioSlotVo> vos = new ArrayList<StudioSlotVo>();			
			if(CollectionUtils.isNotEmpty(list)){
				for(StudioSlotDto dto : list){
					vos.add(operationService.formatDto(dto));
				}
			}
			log.info("图片上传页面返回数据条数===="+vos.size()); 
			return vos;
		} catch (Exception e) {
			log.error("图片上传列表页异常："+e.getMessage(),e); 
		}
		return null;
	}

	@Override
	public List<StudioSlotSpuSendDetailVo> slotDetail(String slotNo) {
		return operationService.slotDetail(slotNo);
	}

	@Override
	public Map<String, String> hasSlotSpuPic(List<String> spPics) {
		Map<String,String> map = new HashMap<String,String>();
		HubSlotSpuPicCriteriaDto criteria = new HubSlotSpuPicCriteriaDto();
		criteria.setFields("sp_pic_url");
		criteria.setPageNo(1);
		criteria.setPageSize(1000);
		criteria.createCriteria().andSpPicUrlIn(spPics);
		List<HubSlotSpuPicDto> list = hubSlotSpuPicGateway.selectByCriteria(criteria );
		if(CollectionUtils.isNotEmpty(list)){
			for(HubSlotSpuPicDto picDto : list){
				map.put(picDto.getSpPicUrl(), "");
			}
		}
		return map;
	}

	@Override
	public boolean insertSlotSpuPic(String slotSpuNo, String spPicUrl, HubSlotSpuDto spuDto,
			HubSlotSpuSupplierDto supplierDto, String extension) {
		try {
			HubSlotSpuPicDto hubSlotSpuPicDto = new HubSlotSpuPicDto();
			hubSlotSpuPicDto.setSlotSpuId(spuDto.getSlotSpuId());
			hubSlotSpuPicDto.setSlotSpuSupplierId(supplierDto.getSlotSpuSupplierId());
			hubSlotSpuPicDto.setSlotSpuNo(slotSpuNo);
			hubSlotSpuPicDto.setSupplierNo(supplierDto.getSupplierNo());
			hubSlotSpuPicDto.setSupplierId(supplierDto.getSupplierId());
			Date time = new Date();
			hubSlotSpuPicDto.setCreateTime(time);
			hubSlotSpuPicDto.setUpdateTime(time);
			hubSlotSpuPicDto.setSpPicUrl(spPicUrl);
			hubSlotSpuPicDto.setPicExtension(extension);
			hubSlotSpuPicDto.setDataState(DataState.NOT_DELETED.getIndex());
			hubSlotSpuPicGateway.insert(hubSlotSpuPicDto);
			return true;
		} catch (Exception e) {
			log.error("插入hub_slot_spu_pic异常："+e.getMessage(),e);
		}
		return false;
	}

	@Override
	public boolean deleteSlotSpuPic(String spPicUrl) {
		try {
			log.info("开始删除图片："+spPicUrl);
			if(StringUtils.isNotBlank(spPicUrl)){
				List<String> urls =  new ArrayList<String>();
				urls.add(spPicUrl);
				log.info("第一步：删除fsdfs "+spPicUrl);
				Map<String,Integer> map = pictureService.deletePics(urls);
				log.info("返回的结果是："+JsonUtil.serialize(map)); 
				if(0 == map.get(spPicUrl)){
					Map<String,String> hasPics = hasSlotSpuPic(urls);
					log.info("第二步：判断mysql中是否存在该图片====> "+JsonUtil.serialize(hasPics)+"====>"+spPicUrl); 
					if(hasPics.containsKey(spPicUrl)){ 
						int result = delete(spPicUrl);
						log.info("第三步：删除mysql返回的结果====>"+result+"====>"+spPicUrl);
						if(1 == result){
							return true;
						}else{
							return false;
						}
					}
					return true;
				}
			}
		} catch (Exception e) {
			log.error("删除图片异常："+e.getMessage(),e); 
		}
		return false;
	}

	private int delete(String spPicUrl) {
		HubSlotSpuPicWithCriteriaDto withCritera =  new HubSlotSpuPicWithCriteriaDto();
		HubSlotSpuPicCriteriaDto criteria = new HubSlotSpuPicCriteriaDto();
		criteria.createCriteria().andSpPicUrlEqualTo(spPicUrl);
		withCritera.setCriteria(criteria );
		HubSlotSpuPicDto hubSlotSpuPic = new HubSlotSpuPicDto();
		hubSlotSpuPic.setDataState(DataState.DELETED.getIndex());
		hubSlotSpuPic.setUpdateTime(new Date()); 
		withCritera.setHubSlotSpuPic(hubSlotSpuPic );
		int result = hubSlotSpuPicGateway.updateByCriteriaSelective(withCritera );
//		log.info("删除数据库结果=============="+result);
		return result;
	}

	
}
