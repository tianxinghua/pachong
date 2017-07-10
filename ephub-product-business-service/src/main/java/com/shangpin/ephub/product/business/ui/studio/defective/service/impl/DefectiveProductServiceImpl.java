package com.shangpin.ephub.product.business.ui.studio.defective.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotArriveState;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicWithCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.defective.gateway.StudioSlotDefectiveSpuGateWay;
import com.shangpin.ephub.client.data.studio.slot.defective.gateway.StudioSlotDefectiveSpuPicGateWay;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.slot.slot.gateway.StudioSlotGateWay;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;
import com.shangpin.ephub.client.data.studio.slot.spu.gateway.StudioSlotSpuSendDetailGateWay;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.studio.common.operation.service.OperationService;
import com.shangpin.ephub.product.business.ui.studio.common.pictrue.service.PictureService;
import com.shangpin.ephub.product.business.ui.studio.defective.dto.DefectiveQuery;
import com.shangpin.ephub.product.business.ui.studio.defective.service.DefectiveProductService;
import com.shangpin.ephub.product.business.ui.studio.defective.vo.DefectiveProductVo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DefectiveProductServiceImpl implements DefectiveProductService {
	
	@Autowired
	private StudioSlotGateWay studioSlotGateWay;
	@Autowired
	private StudioSlotDefectiveSpuGateWay defectiveSpuGateWay;
	@Autowired
	private StudioSlotDefectiveSpuPicGateWay defectiveSpuPicGateWay;
	@Autowired
	private StudioSlotSpuSendDetailGateWay studioSlotSpuSendDetailGateWay;
	@Autowired
	private PictureService pictureService;
	@Autowired
	private OperationService operationService;
	

	@Override
	public List<DefectiveProductVo> list(DefectiveQuery defectiveQuery) {
		try {
			List<DefectiveProductVo> products = new ArrayList<DefectiveProductVo>();
			List<StudioSlotDto> studioSlots = findStudioSlot(defectiveQuery.getStudioNo());
			if(CollectionUtils.isNotEmpty(studioSlots)){
				List<String> slotNos = new ArrayList<String>();
				for(StudioSlotDto slotDto : studioSlots){
					slotNos.add(slotDto.getSlotNo());
				}
				StudioSlotDefectiveSpuCriteriaDto criteria = new StudioSlotDefectiveSpuCriteriaDto();
				criteria.setOrderByClause("create_time desc");
				if(null != defectiveQuery.getPageIndex() && null != defectiveQuery.getPageSize()){
					criteria.setPageNo(defectiveQuery.getPageIndex());
					criteria.setPageSize(defectiveQuery.getPageSize()); 
				}
				criteria.createCriteria().andSlotNoIn(slotNos);
				List<StudioSlotDefectiveSpuDto> list = defectiveSpuGateWay.selectByCriteria(criteria );
				if(CollectionUtils.isNotEmpty(list)){
					for(StudioSlotDefectiveSpuDto dto : list){
						StudioSlotSpuSendDetailDto detailDto =  findDetail(dto.getSlotNo(),dto.getSlotSpuNo());
						if(null != detailDto){
							DefectiveProductVo vo = convertDto(dto, detailDto); 
							products.add(vo);
						}
					}
				}
				
			}
			return products;
		} catch (Exception e) {
			log.error("查询残次品列表失败："+e.getMessage(),e); 
		}
		return null;
	}
	private DefectiveProductVo convertDto(StudioSlotDefectiveSpuDto dto, StudioSlotSpuSendDetailDto detailDto) {
		DefectiveProductVo vo =  new DefectiveProductVo();
		vo.setBrand(detailDto.getSupplierBrandName());
		vo.setItemCode(detailDto.getSupplierSpuModel());
		vo.setItemName(detailDto.getSupplierSpuName());
		vo.setStudioCode(detailDto.getBarcode());
		vo.setStudioSlotDefectiveSpuId(dto.getStudioSlotDefectiveSpuId());
		return vo;
	}
	/**
	 * 查详情页
	 * @param slotNo
	 * @param slotSpuNo
	 * @return
	 */
	private StudioSlotSpuSendDetailDto findDetail(String slotNo, String slotSpuNo){
		StudioSlotSpuSendDetailCriteriaDto criteria = new StudioSlotSpuSendDetailCriteriaDto();
		criteria.createCriteria().andSlotNoEqualTo(slotNo).andSlotSpuNoEqualTo(slotSpuNo);
		List<StudioSlotSpuSendDetailDto> list = studioSlotSpuSendDetailGateWay.selectByCriteria(criteria);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	private List<StudioSlotDto> findStudioSlot(String studioNo ) throws Exception{
		StudioSlotCriteriaDto criteria = new StudioSlotCriteriaDto();
		criteria.setOrderByClause("create_time desc"); 
		criteria.setFields("slot_no");
		criteria.setPageNo(1);
		criteria.setPageSize(100); 
		criteria.createCriteria().andArriveStatusEqualTo(StudioSlotArriveState.RECEIVED.getIndex().byteValue());
		Long studioId = operationService.getStudioId(studioNo);
		if(null != studioId){
			criteria.createCriteria().andStudioIdEqualTo(studioId);
		}else{
			throw new Exception("未获得摄影棚编号");
		}
		return studioSlotGateWay.selectByCriteria(criteria);
	}

	@Override
	public StudioSlotDefectiveSpuDto add(String slotNoSpuId) {
		try {
			StudioSlotSpuSendDetailDto detailDto = operationService.selectSlotSpuSendDetailOfRrrived(slotNoSpuId);
			String slotNo = detailDto.getSlotNo();
			String slotSpuNo = detailDto.getSlotSpuNo();
			StudioSlotDefectiveSpuDto spuDto = selectBySlot(slotNo,slotSpuNo);
			if(null != spuDto){
				return spuDto;
			}else{
				StudioSlotSpuSendDetailDto hubSpuPendingDto = getStudioSlotSpuSendDetailDto(slotNo,slotSpuNo);
				if(null != hubSpuPendingDto){
					StudioSlotDefectiveSpuDto defectiveSpuDto = new StudioSlotDefectiveSpuDto();
					defectiveSpuDto.setDetailId(hubSpuPendingDto.getStudioSlotSpuSendDetailId()); 
					defectiveSpuDto.setSlotNo(slotNo);
					defectiveSpuDto.setSlotSpuNo(slotSpuNo); 
					defectiveSpuDto.setSupplierNo(hubSpuPendingDto.getSupplierNo());
					defectiveSpuDto.setSupplierId(hubSpuPendingDto.getSupplierId());
					defectiveSpuDto.setSpuPendingId(hubSpuPendingDto.getSpuPendingId());
					defectiveSpuDto.setSupplierSpuId(hubSpuPendingDto.getSupplierSpuId());
					Date date = new Date();
					defectiveSpuDto.setCreateTime(date);
					defectiveSpuDto.setUpdateTime(date); 
					Long studioSlotDefectiveSpuId = defectiveSpuGateWay.insert(defectiveSpuDto );
					defectiveSpuDto.setStudioSlotDefectiveSpuId(studioSlotDefectiveSpuId); 
					return defectiveSpuDto;
				}else{
					log.info("添加残次品时未在slot明细表中发现该商品，slotNoSpuId==="+slotNoSpuId); 
				}
			}
		} catch (Exception e) {
			log.error("添加残次品时发生异常："+e.getMessage(),e); 
		}
		return null;
	}
	
	public StudioSlotDefectiveSpuDto selectBySlot(String slotNo, String slotSpuNo){
		StudioSlotDefectiveSpuCriteriaDto criteria = new StudioSlotDefectiveSpuCriteriaDto();
		criteria.createCriteria().andSlotNoEqualTo(slotNo).andSlotSpuNoEqualTo(slotSpuNo);
		List<StudioSlotDefectiveSpuDto>  list = defectiveSpuGateWay.selectByCriteria(criteria );
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	
	private StudioSlotSpuSendDetailDto getStudioSlotSpuSendDetailDto(String slotNo, String slotSpuNo){
		StudioSlotSpuSendDetailCriteriaDto criteria = new StudioSlotSpuSendDetailCriteriaDto();
		criteria.createCriteria().andSlotNoEqualTo(slotNo).andSlotSpuNoEqualTo(slotSpuNo);
		List<StudioSlotSpuSendDetailDto> list  = studioSlotSpuSendDetailGateWay.selectByCriteria(criteria);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public Long insert(StudioSlotDefectiveSpuDto defctiveSouDot, String spPicUrl, String extension) {
		StudioSlotDefectiveSpuPicDto spuPicDto = new StudioSlotDefectiveSpuPicDto();
		spuPicDto.setStudioSlotDefectiveSpuId(defctiveSouDot.getStudioSlotDefectiveSpuId());
		spuPicDto.setSpPicUrl(spPicUrl);
		spuPicDto.setDataState(DataState.NOT_DELETED.getIndex()); 
		spuPicDto.setSupplierNo(defctiveSouDot.getSupplierNo());
		spuPicDto.setSupplierId(defctiveSouDot.getSupplierId());
		spuPicDto.setCreateTime(new Date());
		spuPicDto.setPicExtension(extension); 
		return defectiveSpuPicGateWay.insert(spuPicDto);
	}

	@Override
	public List<StudioSlotDefectiveSpuPicDto> selectDefectivePic(String studioSlotDefectiveSpuId) {
		StudioSlotDefectiveSpuPicCriteriaDto criteria = new StudioSlotDefectiveSpuPicCriteriaDto();
		criteria.setOrderByClause("create_time"); 
		criteria.setFields("sp_pic_url"); 
		criteria.setPageNo(1);
		criteria.setPageSize(100); 
		criteria.createCriteria().andStudioSlotDefectiveSpuIdEqualTo(Long.valueOf(studioSlotDefectiveSpuId)).andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
		return defectiveSpuPicGateWay.selectByCriteria(criteria );
	}

	@Override
	public StudioSlotDefectiveSpuDto selectByPrimarykey(Long studioSlotDefectiveSpuId) {
		if(null != studioSlotDefectiveSpuId){
			return defectiveSpuGateWay.selectByPrimaryKey(studioSlotDefectiveSpuId);
		}else{
			return null;
		}
	}

	@Override
	public boolean hasDefectiveSpuPic(String spPicUrl) {
		StudioSlotDefectiveSpuPicCriteriaDto criteria = new StudioSlotDefectiveSpuPicCriteriaDto();
		criteria.createCriteria().andSpPicUrlEqualTo(spPicUrl);
		int size = defectiveSpuPicGateWay.countByCriteria(criteria );
		if(size > 0){
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteDefectivePic(String spPicUrl) {
		log.info("开始删除图片："+spPicUrl);
		if(StringUtils.isNotBlank(spPicUrl)){
			List<String> urls =  new ArrayList<String>();
			urls.add(spPicUrl);
			log.info("第一步：删除fsdfs "+spPicUrl);
			Map<String,Integer> map = pictureService.deletePics(urls);
			log.info("返回的结果是："+JsonUtil.serialize(map)); 
			if(0 == map.get(spPicUrl)){
				boolean hasThePic = hasDefectiveSpuPic(spPicUrl);
				log.info("第二步：判断mysql中是否存在该图片====> "+hasThePic+"====>"+spPicUrl);
				if(hasThePic){
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
		return false;
	}
	
	/**
	 * 删除图片
	 * @param spPicUrl
	 * @return
	 */
	public int delete(String spPicUrl){
		StudioSlotDefectiveSpuPicWithCriteriaDto withCriteria = new StudioSlotDefectiveSpuPicWithCriteriaDto();
		StudioSlotDefectiveSpuPicCriteriaDto criteria = new StudioSlotDefectiveSpuPicCriteriaDto();
		criteria.createCriteria().andSpPicUrlEqualTo(spPicUrl);
		withCriteria.setCriteria(criteria );
		StudioSlotDefectiveSpuPicDto studioSlotDefectiveSpuPicDto = new StudioSlotDefectiveSpuPicDto();
		studioSlotDefectiveSpuPicDto.setDataState(DataState.DELETED.getIndex()); 
		withCriteria.setStudioSlotDefectiveSpuPic(studioSlotDefectiveSpuPicDto );
		int result = defectiveSpuPicGateWay.updateByCriteriaSelective(withCriteria );
//		log.info("删除数据库结果=============="+result);
		return result;
	}
	@Override
	public int countDefectiveProduct(String slotNo) {
		StudioSlotDefectiveSpuCriteriaDto criteria = new StudioSlotDefectiveSpuCriteriaDto();
		criteria.createCriteria().andSlotNoEqualTo(slotNo);
		defectiveSpuGateWay.countByCriteria(criteria );
		return 0;
	}
	@Override
	public List<StudioSlotDefectiveSpuDto> selectDefectiveProduct(String slotNo) {
		StudioSlotDefectiveSpuCriteriaDto criteria = new StudioSlotDefectiveSpuCriteriaDto();
		criteria.setFields("slot_spu_no");
		criteria.setPageNo(1);
		criteria.setPageSize(1000); 
		criteria.createCriteria().andSlotNoEqualTo(slotNo);
		return defectiveSpuGateWay.selectByCriteria(criteria );
	}

}
