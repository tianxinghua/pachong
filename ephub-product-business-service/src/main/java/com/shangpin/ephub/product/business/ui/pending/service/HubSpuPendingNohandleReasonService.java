package com.shangpin.ephub.product.business.ui.pending.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingNohandleReasonCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingNohandleReasonDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingNohandleReasonGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
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
	 * 添加无法处理原因
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
	
	private List<HubSpuPendingDto>  findPendingSpus (List<Long> values){
		HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
		criteria.setFields("spu_pending_id,supplier_id,supplier_no,supplier_spu_id");
		criteria.setPageNo(1);
		criteria.setPageSize(20); 
		criteria.createCriteria().andSpuPendingIdIn(values);
		return hubSpuPendingGateWay.selectByCriteria(criteria);
	}
	
	private void insert(Reason reason, HubSpuPendingDto dto){
		HubSpuPendingNohandleReasonDto reasonDto = new HubSpuPendingNohandleReasonDto();
		reasonDto.setSpuPendingId(dto.getSpuPendingId());
		reasonDto.setSupplierSpuId(dto.getSupplierSpuId());
		reasonDto.setSupplierId(dto.getSupplierId());
		reasonDto.setSupplierNo(dto.getSupplierNo());
		reasonDto.setErrorType(Byte.valueOf(reason.getErrorType()));
		reasonDto.setErrorReason(Byte.valueOf(reason.getErrorReason())); 
		reasonGateWay.insert(reasonDto);
	}

	public Page findOnShelfList(ReasonRequestDto reasons) {
		
		List<ReasonResponseDto> listReturn = new ArrayList<ReasonResponseDto>();
		HubSpuPendingNohandleReasonCriteriaDto hubSpuPendingNohandleReasonCriteriaDto = new HubSpuPendingNohandleReasonCriteriaDto();
		HubSpuPendingNohandleReasonCriteriaDto.Criteria criteria= hubSpuPendingNohandleReasonCriteriaDto.createCriteria();
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
}
