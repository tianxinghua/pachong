package com.shangpin.ephub.product.business.ui.pending.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.ErrorReason;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingNohandleReasonDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingNohandleReasonGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.dto.NohandleReason;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.pending.dto.Reason;
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
			List<HubSpuPendingDto> list = findPendingSpus(nohandleReason.getSupplierId(),nohandleReason.getSupplierSpuNo());
			if(CollectionUtils.isNotEmpty(list)){
				nohandleReason.getReasons().forEach(reason -> insert(reason , list.get(0)));
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
					reasons.getReasons().forEach(reason -> list.forEach(dto -> insert(reason,dto))); 
				}
			}
		} catch (Exception e) {
			log.error("添加无法处理原因异常："+e.getMessage(),e);
		}
	}
	
	//==========================================================================================================================
	
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
	
	private void insert(Reason reason, HubSpuPendingDto dto){
		HubSpuPendingNohandleReasonDto reasonDto = convertFromSpuPendingDto(dto);
		reasonDto.setErrorType(Byte.valueOf(reason.getErrorType()));
		reasonDto.setErrorReason(Byte.valueOf(reason.getErrorReason())); 
		reasonGateWay.insert(reasonDto);
	}
	
	private void insert(String reason, HubSpuPendingDto dto){
		HubSpuPendingNohandleReasonDto reasonDto = convertFromSpuPendingDto(dto);
		ErrorReason errorReason = getErrorReason(reason);
		if(null != errorReason){
			reasonDto.setErrorType(errorReason.getErrorType().getIndex());
			reasonDto.setErrorReason(errorReason.getIndex());
			reasonGateWay.insert(reasonDto);
		}else{
			log.error("没有找到 "+reason+" 对应的错误类型");
		}
		
	}

	private HubSpuPendingNohandleReasonDto convertFromSpuPendingDto(HubSpuPendingDto dto) {
		HubSpuPendingNohandleReasonDto reasonDto = new HubSpuPendingNohandleReasonDto();
		reasonDto.setSpuPendingId(dto.getSpuPendingId());
		reasonDto.setSupplierSpuId(dto.getSupplierSpuId());
		reasonDto.setSupplierId(dto.getSupplierId());
		reasonDto.setSupplierNo(dto.getSupplierNo());
		return reasonDto;
	}
	
	private ErrorReason getErrorReason(String desCh){
		for(ErrorReason errorReason : ErrorReason.values()){
			if(errorReason.getDesCh().equals(desCh)){
				return errorReason;
			}
		}
		return null;
	}
	
}
