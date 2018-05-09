package com.shangpin.ephub.product.business.rest.hubpending.pendingproduct.service;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.AuditState;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.product.business.common.enumeration.SpuStatus;
import com.shangpin.ephub.product.business.service.pending.PendingService;
import com.shangpin.ephub.product.business.ui.pending.service.impl.HubSpuPendingPicService;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingAuditVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingPicVO;

/**
 * <p>
 * Title:HubCheckRuleService.java
 * </p>
 * <p>
 * Description: hua商品校验实现
 * </p>
 * <p>
 * Company: www.shangpin.com
 * </p>
 * 
 * @author zhaogenchun
 * @date 2016年12月23日 下午4:15:16
 */
@SuppressWarnings("unused")
@Service
@Slf4j
public class PendingProductCommonService {

	@Autowired
	PendingService pendingService;
	@Autowired
	HubSpuPendingGateWay spuPendingGateWay;
	@Autowired
	HubSpuPendingPicService hubSpuPendingPicService;

	public String audit(Long spuPendingId) {
		try {
			if (spuPendingId != null) {
				HubSpuPendingDto spuPendingDto = spuPendingGateWay
						.selectByPrimaryKey(spuPendingId);
				if (spuPendingDto != null
						&& spuPendingDto.getSpuState() == SpuStatus.SPU_WAIT_AUDIT
								.getIndex().byteValue()) {

					SpuPendingAuditVO auditVo = new SpuPendingAuditVO();
					BeanUtils.copyProperties(spuPendingDto, auditVo);

					auditVo.setBrandNo(spuPendingDto.getHubBrandNo());
					auditVo.setCategoryNo(spuPendingDto.getHubCategoryNo());
					auditVo.setOrigin(spuPendingDto.getHubOrigin());
					auditVo.setMaterial(spuPendingDto.getHubMaterial());
					List<HubSpuPendingPicDto> picurls = hubSpuPendingPicService
							.findSpPicUrl(spuPendingDto.getSupplierId(),
									spuPendingDto.getSupplierSpuNo());
					
					if(picurls!=null&&!picurls.isEmpty()){
						List<SpuPendingPicVO> picVOs = new ArrayList<>();
						for (HubSpuPendingPicDto picDto : picurls) {
							SpuPendingPicVO picVO = new SpuPendingPicVO();
							picVO.setSpuPendingId(null == picDto.getSpuPendingId() ? 0
									: picDto.getSpuPendingId());
							picVO.setPicId(picDto.getSpuPendingPicId());
							picVO.setSpPicUrl(picDto.getSpPicUrl());
							picVOs.add(picVO);
						}
						auditVo.setPicVOs(picVOs);
					}
					
					auditVo.setAuditUser(spuPendingDto.getUpdateUser());
					auditVo.setAuditStatus((int) 2);
					boolean flag = pendingService.audit(auditVo);
					if(!flag){
						log.info(spuPendingDto.getSpuModel()+"审核失败："+auditVo.getMemo());
						return "审核失败："+auditVo.getMemo();
					}
				}
			}
		} catch (Exception e) {
			log.error("自动审核提交异常：{}", e);
		}
		return null;
	}
}
