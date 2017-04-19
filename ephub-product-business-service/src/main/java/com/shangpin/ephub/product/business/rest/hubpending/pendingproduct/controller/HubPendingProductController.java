package com.shangpin.ephub.product.business.rest.hubpending.pendingproduct.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleType;
import com.shangpin.ephub.client.data.mysql.enumeration.SupplierSelectState;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.product.business.rest.gms.dto.HubResponseDto;
import com.shangpin.ephub.product.business.rest.gms.service.SopSkuService;
import com.shangpin.ephub.product.business.rest.hubpending.pendingproduct.dto.SpSkuNoDto;
import com.shangpin.ephub.product.business.rest.price.service.PriceService;
import com.shangpin.ephub.product.business.service.ServiceConstant;
import com.shangpin.ephub.product.business.service.hub.dto.SopSkuDto;
import com.shangpin.ephub.product.business.service.hub.dto.SopSkuQueryDto;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * 外部调用 非页面
 */
@RestController
@RequestMapping(value = "/pendingproduct")
@Slf4j
public class HubPendingProductController {
	
    @Autowired
	HubSkuPendingGateWay skuPendingGateWay;
    
    @Autowired
    HubSupplierSkuGateWay supplierSkuGateWay;
    @Autowired
    HubSupplierSpuGateWay supplierSpuGateWay;

    @Autowired
	HubSpuPendingGateWay spuPendingGateWay;

	@Autowired
	HubSkuSupplierMappingGateWay skuSupplierMappingGateWay;

	@Autowired
	HubSkuGateWay hubSkuGateWay;

	@Autowired
	SopSkuService sopSkuService;
	@Autowired
	PriceService priceService;
	
	@RequestMapping(value = "/setspskuno")
	public HubResponse<?> checkSku(@RequestBody SpSkuNoDto dto){
		log.info("receive parameters :" + dto.toString());
		try {
			this.updatePendingSku(dto);

		} catch (Exception e) {
			log.error("update spsku error. reason :"+ e.getMessage(),e);
			return HubResponse.errorResp(e.getMessage());
		}
		return HubResponse.successResp(true);
	}

	private void updatePendingSku(SpSkuNoDto dto) throws Exception{
		//如果是因为SOP已存在的错误 需要调用接口获取到信息
		if(dto.getSign()!=1){
			getExistSpSkuNo(dto);
		}

		//写入尚品的SKUno
		HubSkuPendingDto searchSkuPending =null;
		searchSkuPending = updateSkuPendingSpSkuNo(dto);
		//更新SKUSUPPLIERMAPPING 的状态
		updateSkuSupplierMapping(dto);
		//修改hub_sku中的商品sku编号
		if(null!=searchSkuPending){
			updateHubSkuSpSkuNo(dto, searchSkuPending);
		}
		//更新尚品SKU到供货商原始SKU中
//		updateSkuSupplierSpSkuNo(dto);

	}

	private void updateSkuSupplierSpSkuNo(SpSkuNoDto dto) throws Exception{
		HubSupplierSkuCriteriaDto criteria = new HubSupplierSkuCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(dto.getSupplierId()).andSupplierSkuNoEqualTo(dto.getSupplierSkuNo());
		List<HubSupplierSkuDto> hubSkuPendingList = supplierSkuGateWay.selectByCriteria(criteria);
		if(hubSkuPendingList!=null&&hubSkuPendingList.size()>0){
			HubSupplierSkuDto hubSkuPendingOrigion = hubSkuPendingList.get(0);
			
			HubSupplierSkuDto hubSkuPending = new HubSupplierSkuDto();
			hubSkuPending.setSpSkuNo( dto.getSkuNo());
			hubSkuPending.setMemo(dto.getErrorReason());
			hubSkuPending.setSupplierSkuId(hubSkuPendingOrigion.getSupplierSkuId());
			supplierSkuGateWay.updateByPrimaryKeySelective(hubSkuPending);
			HubSupplierSpuDto hubSupplierSpuDto = supplierSpuGateWay.selectByPrimaryKey(hubSkuPendingOrigion.getSupplierSpuId());
			hubSkuPendingOrigion.setSpSkuNo(dto.getSkuNo());
			log.info("查询hubSupplierSpu:{}",hubSupplierSpuDto);
			priceService.savePriceRecordAndSendConsumer(hubSupplierSpuDto, dto.getSupplierNo(), hubSkuPendingOrigion, PriceHandleType.PRICE); 
		}
		
	}
	private void getExistSpSkuNo(SpSkuNoDto dto) {
		if(ServiceConstant.HUB_SEND_TO_SCM_EXIST_SCM_ERROR.equals(dto.getErrorReason())){
            //如果是已存在的错误，调用接口  组装
            SopSkuQueryDto queryDto = new SopSkuQueryDto();
            queryDto.setSopUserNo(dto.getSupplierId());
            List<String> supplierSkuNoList = new ArrayList<>();
             supplierSkuNoList.add(dto.getSupplierSkuNo());
            queryDto.setLstSupplierSkuNo(supplierSkuNoList);
            HubResponseDto<SopSkuDto> sopSkuResponseDto = null;
            try {
                if(supplierSkuNoList.size()>0){
                    sopSkuResponseDto = sopSkuService.querySpSkuNoFromScm(queryDto);
					if(null!=sopSkuResponseDto&&sopSkuResponseDto.getIsSuccess()){
						List<SopSkuDto> sopSkuDtos =  sopSkuResponseDto.getResDatas();
						if(null!=sopSkuDtos&&sopSkuDtos.size()>0){
							 SopSkuDto  sopSkuDto =  sopSkuDtos.get(0);
							dto.setSkuNo(sopSkuDto.getSkuNo());
						}
					}
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

	private void updateHubSkuSpSkuNo(SpSkuNoDto dto, HubSkuPendingDto searchSkuPending) {
		if(null!=searchSkuPending){
//			HubSkuCriteriaDto criteriaSku = new HubSkuCriteriaDto();
			HubSkuDto hubSku = new HubSkuDto();
			hubSku.setSpSkuNo(dto.getSkuNo());
			HubSkuCriteriaDto skuCriteria = new HubSkuCriteriaDto();
			skuCriteria.createCriteria().andSkuNoEqualTo(searchSkuPending.getHubSkuNo());
			HubSkuWithCriteriaDto criteriaWithSku = new HubSkuWithCriteriaDto(hubSku,skuCriteria);
			hubSkuGateWay.updateByCriteriaSelective(criteriaWithSku);
		}
	}

	private void updateSkuSupplierMapping(SpSkuNoDto dto) {
		HubSkuSupplierMappingCriteriaDto criteriaDto = new HubSkuSupplierMappingCriteriaDto();
		criteriaDto.createCriteria().andSupplierNoEqualTo(dto.getSupplierNo()).andSupplierSkuNoEqualTo(dto.getSupplierSkuNo());

		HubSkuSupplierMappingDto hubSkuSupplierMapping = new HubSkuSupplierMappingDto();
		if(dto.getSign()==1){
			if(StringUtils.isBlank(dto.getSkuNo())){
				hubSkuSupplierMapping.setSupplierSelectState(Integer.valueOf(SupplierSelectState.SELECTE_FAIL.getIndex()).byteValue());
				hubSkuSupplierMapping.setMemo("尚品SKU未生成");
			}else{
				hubSkuSupplierMapping.setSupplierSelectState(Integer.valueOf(SupplierSelectState.SELECTED.getIndex()).byteValue());
			}

		}else{
			if(ServiceConstant.HUB_SEND_TO_SCM_EXIST_SCM_ERROR.equals(dto.getErrorReason())){
				hubSkuSupplierMapping.setSupplierSelectState(Integer.valueOf(SupplierSelectState.EXIST.getIndex()).byteValue());
				hubSkuSupplierMapping.setMemo(ServiceConstant.HUB_SEND_TO_SCM_EXIST);

			}else{
				hubSkuSupplierMapping.setSupplierSelectState(Integer.valueOf(SupplierSelectState.SELECTE_FAIL.getIndex()).byteValue());
				hubSkuSupplierMapping.setMemo(dto.getErrorReason());
			}
		}
		hubSkuSupplierMapping.setUpdateTime(new Date());
		HubSkuSupplierMappingWithCriteriaDto skumappingCritria = new HubSkuSupplierMappingWithCriteriaDto(hubSkuSupplierMapping,criteriaDto);
		skuSupplierMappingGateWay.updateByCriteriaSelective(skumappingCritria);
	}

	private HubSkuPendingDto updateSkuPendingSpSkuNo(SpSkuNoDto dto) {
		HubSkuPendingDto hubSkuPending = new HubSkuPendingDto();
		hubSkuPending.setSpSkuNo( dto.getSkuNo());
		hubSkuPending.setMemo(dto.getErrorReason());
		HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
		criteria.createCriteria().andSupplierNoEqualTo(dto.getSupplierNo())
				.andSupplierSkuNoEqualTo(dto.getSupplierSkuNo());


		HubSkuPendingWithCriteriaDto skuCritria = new HubSkuPendingWithCriteriaDto(hubSkuPending,criteria);
		skuPendingGateWay.updateByCriteriaSelective(skuCritria);

		List<HubSkuPendingDto> hubSkuPendingDtos = null;
		if(dto.getSign()==1){
			////更新成功的才处理
			hubSkuPendingDtos = skuPendingGateWay.selectByCriteria(criteria);
		}
		HubSkuPendingDto  searchSkuPending  = null;

		if(null!=hubSkuPendingDtos&&hubSkuPendingDtos.size()>0){
			searchSkuPending = hubSkuPendingDtos.get(0);
		}
		return searchSkuPending;
	}



	@SuppressWarnings("unused")
	private void updateExistSkuSupplierMapping(SpSkuNoDto dto) {
		HubSkuSupplierMappingCriteriaDto criteriaDto = new HubSkuSupplierMappingCriteriaDto();
		criteriaDto.createCriteria().andSupplierNoEqualTo(dto.getSupplierNo()).andSupplierSkuNoEqualTo(dto.getSupplierSkuNo());

		HubSkuSupplierMappingDto hubSkuSupplierMapping = new HubSkuSupplierMappingDto();

		hubSkuSupplierMapping.setSupplierSelectState(Integer.valueOf(SupplierSelectState.EXIST.getIndex()).byteValue());
		hubSkuSupplierMapping.setMemo(dto.getErrorReason());

		hubSkuSupplierMapping.setUpdateTime(new Date());
		HubSkuSupplierMappingWithCriteriaDto skumappingCritria = new HubSkuSupplierMappingWithCriteriaDto(hubSkuSupplierMapping,criteriaDto);
		skuSupplierMappingGateWay.updateByCriteriaSelective(skumappingCritria);
	}


}
