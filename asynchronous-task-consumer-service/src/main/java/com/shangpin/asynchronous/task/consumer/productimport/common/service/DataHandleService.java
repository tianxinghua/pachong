package com.shangpin.asynchronous.task.consumer.productimport.common.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.enumeration.SourceFromEnum;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.product.business.model.dto.BrandModelDto;
import com.shangpin.ephub.client.product.business.model.gateway.HubBrandModelRuleGateWay;
import com.shangpin.ephub.client.product.business.model.result.BrandModelResult;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Title:SupplierOrderService.java
 * </p>
 * <p>
 * Description: 任务队列消费
 * </p>
 * <p>
 * Company: www.shangpin.com
 * </p>	
 * 
 * @author zhaogenchun
 * @date 2016年11月23日 下午4:06:52
 */
@SuppressWarnings("unused")
@Service
@Slf4j
public class DataHandleService {
	
	@Autowired
	HubSkuGateWay hubSkuGateWay;
	@Autowired
	HubSkuPendingGateWay hubSkuPendingGateWay;
	@Autowired
	HubSpuGateWay hubSpuGateway;
	@Autowired
	HubBrandModelRuleGateWay hubBrandModelRuleGateWay;
	@Autowired
	HubSpuPendingGateWay hubSpuPendingGateWay;
	@Autowired
	HubSpuPendingPicGateWay picGateWay;
	
	public HubSpuDto  selectHubSpu(String spuModel,String hubBrandNo) {
		
		HubSpuDto spuDto = null;
		if(spuModel!=null&&hubBrandNo!=null){
			HubSpuCriteriaDto criteria = new HubSpuCriteriaDto();
			criteria.createCriteria().andSpuModelEqualTo(spuModel).andBrandNoEqualTo(hubBrandNo);
			List<HubSpuDto> listSpu = hubSpuGateway.selectByCriteria(criteria);	
			if(listSpu!=null&&listSpu.size()>0){
				spuDto = listSpu.get(0);	
			}
		}
		return spuDto;
	}

	public BrandModelResult checkSpuModel(HubSpuPendingDto hubPendingSpuDto) {
		BrandModelDto dto = new BrandModelDto();
		dto.setBrandMode(hubPendingSpuDto.getSpuModel());
		dto.setHubBrandNo(hubPendingSpuDto.getHubBrandNo());
		dto.setHubCategoryNo(hubPendingSpuDto.getHubCategoryNo());
		return hubBrandModelRuleGateWay.verifyWithCategory(dto);
	}

	public List<HubSpuPendingDto> selectPendingSpu(HubSpuPendingDto hubPendingSpuDto) {
		HubSpuPendingCriteriaDto hubSpuPendingCriteriaDto = new HubSpuPendingCriteriaDto();
		hubSpuPendingCriteriaDto.createCriteria().andSupplierIdEqualTo(hubPendingSpuDto.getSupplierId()).andSupplierSpuNoEqualTo(hubPendingSpuDto.getSupplierSpuNo());
		return hubSpuPendingGateWay.selectByCriteria(hubSpuPendingCriteriaDto);
		
	}

	public List<HubSkuPendingDto> selectHubSkuPendingBySpuPendingId(HubSpuPendingDto hubSpuPendingDro) {
		HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
		List<Byte> listSkuState = new ArrayList<Byte>();
        listSkuState.add(SpuState.HANDLED.getIndex());
        listSkuState.add(SpuState.HANDLING.getIndex());
        criteria.createCriteria().andSupplierIdEqualTo(hubSpuPendingDro.getSupplierId())
		.andSpuPendingIdEqualTo(hubSpuPendingDro.getSpuPendingId()).andSkuStateNotIn(listSkuState);
        
        criteria.or(criteria.createCriteria().andSupplierIdEqualTo(hubSpuPendingDro.getSupplierId())
        .andSpuPendingIdEqualTo(hubSpuPendingDro.getSpuPendingId()).andSkuStateIsNull());

		criteria.setPageNo(1);
		criteria.setPageSize(1000);
		 return hubSkuPendingGateWay.selectByCriteria(criteria);
	}

	public void insertHubSkuPendingDto(HubSkuPendingDto hubSkuPendingDto) {
		hubSkuPendingGateWay.insert(hubSkuPendingDto);		
	}

	public void updateHubSkuPendingByPrimaryKey(HubSkuPendingDto hubSkuPendingDto) {
		hubSkuPendingGateWay.updateByPrimaryKeySelective(hubSkuPendingDto);		
	}

	public HubSkuDto findHubSkuBySpuNoAndSize(String hubSpuNo,String size,String sizeType) {
		HubSkuCriteriaDto sku = new HubSkuCriteriaDto();
		if (size != null && sizeType != null) {
			sku.createCriteria().andSpuNoEqualTo(hubSpuNo).andSkuSizeEqualTo(size)
					.andSkuSizeTypeEqualTo(sizeType);
		} else {
			sku.createCriteria().andSpuNoEqualTo(hubSpuNo);
		}
		List<HubSkuDto> skuList = hubSkuGateWay.selectByCriteria(sku);
		if(skuList!=null&&skuList.size()>0){
			return skuList.get(0);
		}
		return null; 
	}

	public void updateHubSpuPending(HubSpuPendingDto hubPendingSpuDto) {
		HubSpuPendingWithCriteriaDto hubSpuPendingWithCriteriaDto = new HubSpuPendingWithCriteriaDto();
		HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(hubPendingSpuDto.getSupplierId()).andSupplierSpuNoEqualTo(hubPendingSpuDto.getSupplierSpuNo());
		hubSpuPendingWithCriteriaDto.setCriteria(criteria);
		HubSpuPendingDto dto = new HubSpuPendingDto();
		dto.setUpdateTime(new Date());
		dto.setAuditState((byte)0);
		dto.setAuditOpinion("再处理：同品牌同货号颜色不一样");
		dto.setMemo("再处理：同品牌同货号颜色不一样");
		dto.setAuditDate(new Date());
		dto.setAuditUser("chenxu");
		dto.setSpuState((byte)0);
		hubSpuPendingWithCriteriaDto.setHubSpuPending(dto);
		hubSpuPendingGateWay.updateByCriteriaSelective(hubSpuPendingWithCriteriaDto);
	}

	public List<String> getSpAvailablePicList(Long supplierSpuId ){
		HubSpuPendingPicCriteriaDto criteria = new HubSpuPendingPicCriteriaDto();
		criteria.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId).andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
		List<HubSpuPendingPicDto> picDtoList = picGateWay.selectByCriteria(criteria);
		List<String> urlList = new ArrayList<>();
		if(null!=picDtoList&&picDtoList.size()>0){
			picDtoList.forEach(pic->{
				if(StringUtils.isNotBlank(pic.getSpPicUrl())){

					urlList.add(pic.getSpPicUrl());
				}
			});
		}
		return urlList;
	}

	public  void convertWebSpiderSpuToPendingSpu( HubSpuPendingDto sourceDto, HubSpuPendingDto targetPendingSpuDto,HubPendingSpuCheckResult hubPendingSpuCheckResult){
		targetPendingSpuDto.setHubBrandNo(sourceDto.getHubBrandNo());
		targetPendingSpuDto.setHubCategoryNo(sourceDto.getHubCategoryNo());
		if(targetPendingSpuDto.getHubColor()!=null&&targetPendingSpuDto.getHubColor().equals(sourceDto.getHubColor())){
			targetPendingSpuDto.setHubColor(sourceDto.getHubColor());
		}
		targetPendingSpuDto.setHubColorNo(sourceDto.getHubColorNo());
		targetPendingSpuDto.setHubGender(sourceDto.getHubGender());
		targetPendingSpuDto.setHubMaterial(sourceDto.getHubMaterial());
		targetPendingSpuDto.setHubOrigin(sourceDto.getHubOrigin());
		if(StringUtils.isBlank(targetPendingSpuDto.getHubSeason())){
			targetPendingSpuDto.setHubSeason(sourceDto.getHubSeason());
		}
//		hubPendingSpuDto.setHubSeason(hubSpuDto.getMarketTime()+"_"+hubSpuDto.getSeason()); 季节可以修改 ，所以不赋值

		targetPendingSpuDto.setHubSpuNo(sourceDto.getHubSpuNo());
		targetPendingSpuDto.setSpuModel(sourceDto.getSpuModel());
		targetPendingSpuDto.setSpuName(sourceDto.getSpuName());
		targetPendingSpuDto.setOriginSource(SourceFromEnum.TYPE_WEBSPIDER.getIndex().byteValue());

		hubPendingSpuCheckResult.setMaterial(true);
		hubPendingSpuCheckResult.setBrand(true);
		hubPendingSpuCheckResult.setCategory(true);
		hubPendingSpuCheckResult.setColor(true);
		hubPendingSpuCheckResult.setGender(true);
		hubPendingSpuCheckResult.setOriginal(true);
		hubPendingSpuCheckResult.setSeasonName(true);
		hubPendingSpuCheckResult.setSpuModel(true);


	}
	
}
