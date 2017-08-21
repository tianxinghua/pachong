package com.shangpin.ephub.product.business.ui.pending.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.enumeration.ErrorReason;
import com.shangpin.ephub.client.data.mysql.enumeration.MsgMissHandleState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingNohandleReasonCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingNohandleReasonDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingNohandleReasonGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.dto.NohandleReason;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.pending.dto.Page;
import com.shangpin.ephub.product.business.ui.pending.dto.Reason;
import com.shangpin.ephub.product.business.ui.pending.dto.ReasonRequestDto;
import com.shangpin.ephub.product.business.ui.pending.dto.ReasonResponseDto;
import com.shangpin.ephub.product.business.ui.pending.dto.Reasons;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: HubSpuPendingNohandleReasonService</p>
 * <p>Description: 无法处理原因表的业务逻辑 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年8月2日 下午12:25:10
 *
 */
@Service
@Slf4j
public class HubSpuPendingNohandleReasonService {
	@Autowired
	private HubSupplierSpuGateWay hubSupplierSpuGateWay;
	
	@Autowired
	private HubSpuPendingGateWay hubSpuPendingGateWay;
	@Autowired
	private HubSpuPendingNohandleReasonGateWay reasonGateWay;
	
	/**
	 * 添加无法处理原因，任务调用
	 * @param nohandleReason
	 * @return
	 */
	public boolean insertNohandleReason(NohandleReason nohandleReason){
		try {
			log.info("接收到的无法处理实体===="+JsonUtil.serialize(nohandleReason)); 
			List<HubSpuPendingDto> list = findPendingSpus(nohandleReason.getSupplierId(),nohandleReason.getSupplierSpuNo());
			if(CollectionUtils.isNotEmpty(list)){
				nohandleReason.getReasons().forEach(reason -> insert(nohandleReason.getCreateUser(), reason , list.get(0)));
				return true;
			}
		} catch (Exception e) {
			log.error("插入无法处理原因异常："+e.getMessage(),e);
		}
		return false;
	}

	/**
	 * 添加无法处理原因，页面调用
	 * @param reasons
	 */
	public void addUnableReason(Reasons reasons){
		try {
			log.info("添加无法处理原因接受到的参数："+JsonUtil.serialize(reasons));
			if(CollectionUtils.isNotEmpty(reasons.getSpuPeningIds())){
				List<HubSpuPendingDto> list = findPendingSpus(reasons.getSpuPeningIds());
				if(CollectionUtils.isNotEmpty(list) && CollectionUtils.isNotEmpty(reasons.getReasons())){
					reasons.getReasons().forEach(reason -> list.forEach(dto -> insert(reasons.getCreateUser(), reason,dto))); 
				}
			}
		} catch (Exception e) {
			log.error("添加无法处理原因异常："+e.getMessage(),e);
		}
	}
	
	public Map<Long,String> findAllErrorReason(List<Long> spuPendingIds){
		if(CollectionUtils.isNotEmpty(spuPendingIds)){
			Map<Long,String> map = Maps.newHashMap();
			List<HubSpuPendingNohandleReasonDto> list = selectErrorReason(spuPendingIds);
			if(CollectionUtils.isNotEmpty(list)){
				list.forEach(dto -> push(dto,map));
			}
			return map;
		}else{
			return null;
		}
	}
	
	//==========================================================================================================================
	
	private List<HubSpuPendingNohandleReasonDto> selectErrorReason(List<Long> spuPendingIds){
		HubSpuPendingNohandleReasonCriteriaDto criteria = new HubSpuPendingNohandleReasonCriteriaDto();
		criteria.setPageNo(1);criteria.setPageSize(100); 
		criteria.createCriteria().andSpuPendingIdIn(spuPendingIds).andDataStateEqualTo(DataState.NOT_DELETED.getIndex()); 
		return reasonGateWay.selectByCriteria(criteria);
	}
	
	private void push(HubSpuPendingNohandleReasonDto dto , Map<Long,String> map){
		Long spuPendingId = dto.getSpuPendingId();
		if(map.containsKey(spuPendingId)){
			String desCh = map.get(spuPendingId) + "," + getDesCh(dto.getErrorReason());
			map.put(spuPendingId, desCh);
		}else{
			map.put(spuPendingId, getDesCh(dto.getErrorReason()));
		}
	}
	
	private List<HubSpuPendingDto>  findPendingSpus (List<Long> values){
		HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
		criteria.setFields("spu_pending_id,supplier_id,supplier_no,supplier_spu_id");
		criteria.setPageNo(1);
		criteria.setPageSize(20); 
		criteria.createCriteria().andSpuPendingIdIn(values);
		return hubSpuPendingGateWay.selectByCriteria(criteria);
	}
	
	private List<HubSpuPendingDto>  findPendingSpus (String supplierId, String supplierSpuNo){
		HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
		criteria.setFields("spu_pending_id,supplier_id,supplier_no,supplier_spu_id");
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo);
		return hubSpuPendingGateWay.selectByCriteria(criteria);
	}
 	
	private void insert(String createUser, Reason reason, HubSpuPendingDto dto){
		/**
		 * 先插入数据
		 */
		HubSpuPendingNohandleReasonDto reasonDto = convertFromSpuPendingDto(dto);
		reasonDto.setCreateUser(createUser);
		reasonDto.setErrorType(Byte.valueOf(reason.getErrorType()));
		reasonDto.setErrorReason(Byte.valueOf(reason.getErrorReason())); 
		reasonGateWay.insert(reasonDto);
		/**
		 * pending数据打个标
		 */
		HubSpuPendingDto hubPendingSpuDto = new HubSpuPendingDto();
		hubPendingSpuDto.setSpuPendingId(dto.getSpuPendingId());
		hubPendingSpuDto.setMsgMissHandleState(MsgMissHandleState.HAVE_HANDLED.getIndex());
		hubPendingSpuDto.setUpdateUser(createUser);
		hubPendingSpuDto.setUpdateTime(new Date());
		hubSpuPendingGateWay.updateByPrimaryKeySelective(hubPendingSpuDto);
	}
	
	private void insert(String createUser, String reason, HubSpuPendingDto dto){
		HubSpuPendingNohandleReasonDto reasonDto = convertFromSpuPendingDto(dto);
		reasonDto.setCreateUser(createUser); 
		ErrorReason errorReason = getErrorReason(reason);
		if(null != errorReason){
			reasonDto.setErrorType(errorReason.getErrorType().getIndex());
			reasonDto.setErrorReason(errorReason.getIndex());
			reasonGateWay.insert(reasonDto);
		}else{
			log.error("没有找到 "+reason+" 对应的错误类型");
		}
		
	}

	/**
	 * 2个insert方法公共的部分
	 * @param dto
	 * @return
	 */
	private HubSpuPendingNohandleReasonDto convertFromSpuPendingDto(HubSpuPendingDto dto) {
		HubSpuPendingNohandleReasonDto reasonDto = new HubSpuPendingNohandleReasonDto();
		reasonDto.setSpuPendingId(dto.getSpuPendingId());
		reasonDto.setSupplierSpuId(dto.getSupplierSpuId());
		reasonDto.setSupplierId(dto.getSupplierId());
		reasonDto.setSupplierNo(dto.getSupplierNo());
		reasonDto.setDataState(DataState.NOT_DELETED.getIndex());
		reasonDto.setCreateTime(new Date());
		return reasonDto;
	}

	public Page findOnShelfList(ReasonRequestDto reasons) {
		
		List<ReasonResponseDto> listReturn = new ArrayList<ReasonResponseDto>();
		HubSpuPendingNohandleReasonCriteriaDto hubSpuPendingNohandleReasonCriteriaDto = new HubSpuPendingNohandleReasonCriteriaDto();
		HubSpuPendingNohandleReasonCriteriaDto.Criteria criteria= hubSpuPendingNohandleReasonCriteriaDto.createCriteria();
		if(reasons.getSupplierId()==null){
			return null;
		}
		criteria.andSupplierIdEqualTo(reasons.getSupplierId());
		if(reasons.getErrorReason()!=null){
			criteria.andErrorReasonEqualTo(reasons.getErrorReason());
		}
		if(reasons.getErrorType()!=null){
			criteria.andErrorTypeEqualTo(reasons.getErrorType());
		}
		Page page = null;
		int count = reasonGateWay.countByCriteria(hubSpuPendingNohandleReasonCriteriaDto);
		log.info("count:"+count);
		if(count>0){
			page = new Page();
			hubSpuPendingNohandleReasonCriteriaDto.setPageNo(reasons.getPageIndex());
			hubSpuPendingNohandleReasonCriteriaDto.setPageSize(reasons.getPageSize());
			
			List<HubSpuPendingNohandleReasonDto> list = reasonGateWay.selectByCriteria(hubSpuPendingNohandleReasonCriteriaDto);
			if(list!=null){
				for(HubSpuPendingNohandleReasonDto dto:list){
					Long spuPendingId = dto.getSupplierSpuId();
					if(spuPendingId!=null){
						HubSupplierSpuDto spuPendingDto = hubSupplierSpuGateWay.selectByPrimaryKey(spuPendingId);
						if(spuPendingDto!=null){
							ReasonResponseDto response = new ReasonResponseDto();
							response.setBrandName(spuPendingDto.getSupplierBrandname());
							response.setErrorReason(dto.getErrorReason());
							response.setErrorType(dto.getErrorType());
							response.setSpuModel(spuPendingDto.getSupplierSpuModel());
							response.setSpuName(spuPendingDto.getSupplierSpuName());
							response.setSupplierSpuNo(spuPendingDto.getSupplierSpuNo());	
							listReturn.add(response);
						}
					}
				}
			}
			page.setTotal(count);
			page.setList(listReturn);
		}
		
		return page;
	}
	
	private ErrorReason getErrorReason(String desCh){
		for(ErrorReason errorReason : ErrorReason.values()){
			if(errorReason.getDesCh().equals(desCh)){
				return errorReason;
			}
		}
		return null;
	}
	
	private String getDesCh(byte index){
		for(ErrorReason errorReason : ErrorReason.values()){
			if(errorReason.getIndex() == index){
				return errorReason.getDesCh();
			}
		}
		return "";
	}
	
}
