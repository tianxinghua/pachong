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
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotState;
import com.shangpin.ephub.client.data.studio.enumeration.UploadPicSign;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotWithCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.gateway.StudioSlotGateWay;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;
import com.shangpin.ephub.client.data.studio.slot.spu.gateway.StudioSlotSpuSendDetailGateWay;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.studio.common.operation.dto.OperationQuery;
import com.shangpin.ephub.product.business.ui.studio.common.operation.enumeration.OperationQueryType;
import com.shangpin.ephub.product.business.ui.studio.common.operation.service.OperationService;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.StudioSlotVo;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.detail.StudioSlotSpuSendDetailVo;
import com.shangpin.ephub.product.business.ui.studio.common.pictrue.service.PictureService;
import com.shangpin.ephub.product.business.ui.studio.defective.service.DefectiveProductService;
import com.shangpin.ephub.product.business.ui.studio.imageupload.service.ImageUploadService;
import com.shangpin.ephub.response.HubResponse;

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
	@Autowired
	private DefectiveProductService defectiveProductService;
	@Autowired
	private StudioSlotSpuSendDetailGateWay studioSlotSpuSendDetailGateWay;
	@Autowired
	private StudioSlotGateWay studioSlotGateWay;

	@Override
	public List<StudioSlotVo> list(OperationQuery operationQuery) {
		try {
			operationQuery.setOperationQueryType(OperationQueryType.IMAGE_UPLOAD.getIndex());
			log.info("图片上传页面接收到的参数："+JsonUtil.serialize(operationQuery)); 
			List<StudioSlotDto>  list = operationService.slotList(operationQuery);
			
			List<StudioSlotVo> vos = new ArrayList<StudioSlotVo>();			
			if(CollectionUtils.isNotEmpty(list)){
				for(StudioSlotDto dto : list){
					vos.add(formatDto(dto));
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
		List<StudioSlotSpuSendDetailVo> vos = new ArrayList<StudioSlotSpuSendDetailVo>();
		List<StudioSlotSpuSendDetailDto> list = operationService.selectDetailOfArrived(slotNo);
		List<StudioSlotDefectiveSpuDto> defects = defectiveProductService.selectDefectiveProduct(slotNo);
		if(CollectionUtils.isNotEmpty(defects)){
			Map<String,String> map = new HashMap<String,String>();
			for(StudioSlotDefectiveSpuDto dto : defects){
				map.put(dto.getSlotSpuNo(), "");
			}
			for(StudioSlotSpuSendDetailDto dto : list){
				if(!map.containsKey(dto.getSlotSpuNo())){
					vos.add(convertDto(dto));
				}
			}
		}else{
			for(StudioSlotSpuSendDetailDto dto : list){
				vos.add(convertDto(dto));
			}
		}
		return vos;
	}
	
	
	
	/**
	 * 转换
	 * @param dto
	 * @return
	 */
	private StudioSlotSpuSendDetailVo convertDto(StudioSlotSpuSendDetailDto dto) {
		StudioSlotSpuSendDetailVo vo = new StudioSlotSpuSendDetailVo();
		vo.setArriveState(dto.getArriveState());
		vo.setBrand(dto.getSupplierBrandName());
		vo.setItemCode(dto.getSupplierSpuModel());
		vo.setItemName(dto.getSupplierSpuName());
		vo.setOperator(dto.getUpdateUser());
//		vo.setStudioCode(dto.getSlotNo()+"-"+dto.getSlotSpuNo());
		vo.setTime(dto.getCreateTime());
		vo.setStudioCode(dto.getBarcode());
		vo.setUploadPicSign(null != dto.getUploadPicSign() ? dto.getUploadPicSign() : 0); 
		return vo;
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
	
	private StudioSlotVo formatDto(StudioSlotDto studioSlotDto) {
		StudioSlotVo slotVo = new StudioSlotVo();
		slotVo.setSlotNo(studioSlotDto.getSlotNo());
		slotVo.setOperateDate(studioSlotDto.getPlanShootTime());
		setDetailQty(studioSlotDto.getSlotNo(), slotVo);
		slotVo.setTrackingNo(studioSlotDto.getTrackNo()); 
		return slotVo;
	}
	/**
	 * 获取详情数量
	 * @param slotNo
	 * @return
	 */
	private void setDetailQty(String slotNo, StudioSlotVo slotVo){
		List<StudioSlotSpuSendDetailDto> list = operationService.selectDetailOfArrived(slotNo);
		int qty = 0;
		int uploadQty = 0;
		if(CollectionUtils.isNotEmpty(list)){
			for(StudioSlotSpuSendDetailDto dto : list){
				if(null != dto.getUploadPicSign() && dto.getUploadPicSign() == UploadPicSign.HAVE_UPLOADED.getIndex().byteValue()){
					uploadQty ++;
				}
			}
			//qty=所有已到货-残品
			int defective = defectiveProductService.countDefectiveProduct(slotNo);
			qty = list.size() - defective;
		}
		log.info("slotNo=="+slotNo+"【qty="+qty+">>>>>>>>uploadQty="+uploadQty+"】");
		slotVo.setQty(qty); 
		slotVo.setUploadQty(uploadQty);
	}

	@Override
	public int updateUploadPicSign(Long studioSlotSpuSendDetailId,UploadPicSign uploadPicSign) {
		StudioSlotSpuSendDetailDto detailDto = new StudioSlotSpuSendDetailDto();
		detailDto.setUploadPicSign(uploadPicSign.getIndex().byteValue());
		detailDto.setStudioSlotSpuSendDetailId(studioSlotSpuSendDetailId); 
		return studioSlotSpuSendDetailGateWay.updateByPrimaryKeySelective(detailDto );
	}

	@Override
	public HubResponse<?> confirm(String slotNo) {
		StudioSlotVo slotVo = new StudioSlotVo();
		setDetailQty(slotNo,slotVo);
		if(slotVo.getQty() == slotVo.getUploadQty()){
			StudioSlotWithCriteriaDto withCriteria = new StudioSlotWithCriteriaDto();
			StudioSlotDto studioSlot = new StudioSlotDto();
			studioSlot.setSlotStatus(StudioSlotState.HAVE_SHOOT.getIndex().byteValue());
			withCriteria.setStudioSlot(studioSlot );
			StudioSlotCriteriaDto criteria = new StudioSlotCriteriaDto();
			criteria.createCriteria().andSlotNoEqualTo(slotNo);
			withCriteria.setCriteria(criteria );
			int result = studioSlotGateWay.updateByCriteriaSelective(withCriteria );
			if(result == 1){
				return HubResponse.successResp("成功");
			}else{
				return HubResponse.errorResp("更新状态失败");
			}
		}else{
			return HubResponse.errorResp("Qty与Upload Qty数量不相等");
		}
	}

	@Override
	public List<String> findPictures(String barcode) {
		try {
			log.info("barcode========="+barcode); 
			List<String> pics = new ArrayList<String>();
			StudioSlotSpuSendDetailDto detailDto = operationService.selectSlotSpuSendDetailOfRrrived(barcode);
			if(null != detailDto){
				String slotSpuNo = detailDto.getSlotSpuNo();
				log.info("slotSpuNo=========="+slotSpuNo); 
				HubSlotSpuPicCriteriaDto criteria = new HubSlotSpuPicCriteriaDto();
				criteria.setFields("sp_pic_url");
				criteria.setPageNo(1);
				criteria.setPageSize(1000); 
				criteria.createCriteria().andSlotSpuNoEqualTo(slotSpuNo).andDataStateEqualTo(DataState.NOT_DELETED.getIndex()); 
				List<HubSlotSpuPicDto> list = hubSlotSpuPicGateway.selectByCriteria(criteria );
				log.info("查找到的图片数量==========="+list.size()); 
				if(CollectionUtils.isNotEmpty(list)){
					for(HubSlotSpuPicDto dto : list){
						pics.add(dto.getSpPicUrl());
					}
				}
			}
			return pics;
		} catch (Exception e) {
			log.error("获取图片异常："+e.getMessage(),e);
		}
		return null;
	}

	
}
