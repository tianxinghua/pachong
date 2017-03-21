package com.shangpin.ephub.product.business.rest.hubpending.spu.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.FilterFlag;
import com.shangpin.ephub.client.data.mysql.enumeration.HandleFromState;
import com.shangpin.ephub.client.data.mysql.enumeration.HandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpSkuSizeState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuBrandState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuModelState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.enumeration.StockState;
import com.shangpin.ephub.client.data.mysql.product.dto.HubPendingDto;
import com.shangpin.ephub.client.data.mysql.product.gateway.PengdingToHubGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.common.pending.sku.HubPendingSkuService;
import com.shangpin.ephub.product.business.common.pending.spu.HubPendingSpuService;
import com.shangpin.ephub.product.business.common.service.check.CommonCheckBase;
import com.shangpin.ephub.product.business.common.service.check.PropertyCheck;
import com.shangpin.ephub.product.business.common.service.check.property.BrandCheck;
import com.shangpin.ephub.product.business.common.service.check.property.CategoryCheck;
import com.shangpin.ephub.product.business.common.service.check.property.ColorCheck;
import com.shangpin.ephub.product.business.common.service.check.property.GenderCheck;
import com.shangpin.ephub.product.business.common.service.check.property.MaterialCheck;
import com.shangpin.ephub.product.business.common.service.check.property.OriginCheck;
import com.shangpin.ephub.product.business.common.service.check.property.SeasonCheck;
import com.shangpin.ephub.product.business.common.service.check.property.SpuModelCheck;
import com.shangpin.ephub.product.business.rest.hub.spu.HubSpuService;

import lombok.extern.slf4j.Slf4j;

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
public class HubPendingSpuHandleService {

	@Autowired
	PropertyCheck commonCheckBase;
	@Autowired
	HubSpuService hubSpuService;
	@Autowired
	HubPendingSpuService hubPendingSpuService;
	@Autowired
	HubPendingSkuService hubPendingSkuService;
	@Autowired
	PengdingToHubGateWay pengdingToHubGateWay;
	@Autowired
	BrandCheck brandCheck;
	@Autowired
	CategoryCheck categoryCheck;
	@Autowired
	ColorCheck colorCheck;
	@Autowired
	GenderCheck genderCheck;
	@Autowired
	MaterialCheck materialCheck;
	@Autowired
	OriginCheck originCheck;
	@Autowired
	SeasonCheck seasonCheck;
	@Autowired
	SpuModelCheck spuModelCheck;

	@PostConstruct
	public void init() {
		List<CommonCheckBase> list = new ArrayList<CommonCheckBase>();
		list.add(brandCheck);
		list.add(categoryCheck);
		list.add(colorCheck);
		list.add(genderCheck);
		list.add(materialCheck);
		list.add(originCheck);
		list.add(seasonCheck);
		list.add(spuModelCheck);
		commonCheckBase.setAllPropertyCheck(list);
	}

	public HubSpuPendingDto handleHubPendingSpu(HubSpuPendingDto hubSpuPendingDto) throws Exception {

		HubSpuPendingDto hubSpuPendingIsExist = null;
		hubSpuPendingIsExist = hubPendingSpuService.findHubSpuPendingBySupplierIdAndSupplierSpuNo(
				hubSpuPendingDto.getSupplierId(), hubSpuPendingDto.getSupplierSpuNo());
		if (hubSpuPendingIsExist != null) {
			handleOldHubSpuPending(hubSpuPendingIsExist, hubSpuPendingDto);
		} else {
			hubSpuPendingIsExist = handleNewHubSpuPending(hubSpuPendingDto);
		}
		return hubSpuPendingIsExist;
	}

	private String handleOldHubSpuPending(HubSpuPendingDto hubSpuPendingIsExist, HubSpuPendingDto hubSpuPendingDto)
			throws Exception {

		if (hubSpuPendingIsExist.getSpuState()!=null&&(SpuState.HANDLED.getIndex() ==  hubSpuPendingIsExist.getSpuState()
				||SpuState.HANDLING.getIndex()== hubSpuPendingIsExist.getSpuState()
				||SpuState.INFO_IMPECCABLE.getIndex() == hubSpuPendingIsExist.getSpuState())) {
			// 如果spustate状态为已处理、审核中或者已完善 ，则不更新
			BeanUtils.copyProperties(hubSpuPendingIsExist, hubSpuPendingDto);
			return null;
		}
		// 校验spu各属性
		String result =  commonCheckBase.handleconvertOrCheck(hubSpuPendingIsExist,hubSpuPendingDto);;
		checkHubSpuPendingIsExistHubSpu(hubSpuPendingIsExist);
		hubPendingSpuService.updateHubSpuPendingByPrimaryKey(hubSpuPendingIsExist);
		return result;
	}
	private HubSpuPendingDto handleNewHubSpuPending(HubSpuPendingDto hubSpuPendingDto) throws Exception{
		
		// 映射spu各属性
		HubSpuPendingDto hubSpuPendingIsExist = new HubSpuPendingDto();
		convertHubPendingSpuDto(hubSpuPendingDto,hubSpuPendingIsExist);
		commonCheckBase.handleconvertOrCheck(hubSpuPendingIsExist,hubSpuPendingDto);
		
		checkHubSpuPendingIsExistHubSpu(hubSpuPendingIsExist);
		try{
			Long spuPendingId = hubPendingSpuService.insertHubSpuPending(hubSpuPendingIsExist);
			hubSpuPendingIsExist.setSpuPendingId(spuPendingId);	
		}catch(Exception e){
			hubSpuPendingIsExist = new HubSpuPendingDto();
			setSpuPendingValueWhenDuplicateKeyException(hubSpuPendingIsExist,e,hubSpuPendingDto.getSupplierId(),hubSpuPendingDto.getSupplierSpuNo());
		}
		return hubSpuPendingIsExist;
	}
	
	private void setSpuPendingValueWhenDuplicateKeyException(HubSpuPendingDto hubSpuPendingIsExist, Exception e, String supplierId, String supplierSpuNo) throws Exception {
		if (e instanceof DuplicateKeyException) {
			HubSpuPendingDto spuDto = hubPendingSpuService.findHubSpuPendingBySupplierIdAndSupplierSpuNo(supplierId,supplierSpuNo);
			if (null != spuDto) {
				BeanUtils.copyProperties(spuDto, hubSpuPendingIsExist);
			}
		} else {
			throw e;
		}
	}
	
	private void convertHubPendingSpuDto(HubSpuPendingDto hubSpuPendingDto, HubSpuPendingDto hubSpuPendingIsExist) {
		BeanUtils.copyProperties(hubSpuPendingDto, hubSpuPendingIsExist);
		hubSpuPendingIsExist.setUpdateTime(new Date());
		hubSpuPendingIsExist.setUpdateTime(new Date());
		hubSpuPendingIsExist.setSpuState(SpuState.INFO_PECCABLE.getIndex());
	}

	private boolean checkHubSpuPendingIsExistHubSpu(HubSpuPendingDto hubSpuPendingIsExist) throws Exception {
		
		if (hubSpuPendingIsExist.getSpuModelState() !=null&&hubSpuPendingIsExist.getSpuBrandState()!=null&&hubSpuPendingIsExist.getSpuModelState() == SpuModelState.VERIFY_PASSED.getIndex()&&
				hubSpuPendingIsExist.getSpuBrandState()==SpuBrandState.HANDLED.getIndex()) {
			// 查询是否已存在hubSpu
			HubSpuDto hubSpuDto = hubSpuService.findHubSpuByBrandNoAndSpuModel(hubSpuPendingIsExist.getHubBrandNo(),
					hubSpuPendingIsExist.getSpuModel());
			if (hubSpuDto != null) {
				convertHubSpuToPendingSpu(hubSpuDto,hubSpuPendingIsExist);
				return true;
			}else{
				checkIsExistPendingHanding(hubSpuPendingIsExist);
			}
		}
		return false;
	}

	private void checkIsExistPendingHanding(HubSpuPendingDto hubSpuPendingDto ){
		HubSpuPendingDto hubSpuPendingDtoTemp = hubPendingSpuService.findHubSpuPendingBySpuModelAndBrandNoAndSpuState(hubSpuPendingDto.getSpuModel(),
				hubSpuPendingDto.getHubBrandNo(),SpuState.INFO_IMPECCABLE.getIndex());	
		if(hubSpuPendingDtoTemp!=null&&hubSpuPendingDtoTemp.getSpuState()!=null&&(hubSpuPendingDtoTemp.getSpuState()==SpuState.INFO_IMPECCABLE.getIndex()||hubSpuPendingDtoTemp.getSpuState()==SpuState.HANDLED.getIndex()||hubSpuPendingDtoTemp.getSpuState()==SpuState.HANDLING.getIndex())){
			convertExistHubSpuPendingToNewHubSpuPending(hubSpuPendingDto,hubSpuPendingDtoTemp);
		}
	}
	private void convertExistHubSpuPendingToNewHubSpuPending(HubSpuPendingDto hubPendingSpuDto,
			HubSpuPendingDto hubSpuPendingDtoTemp) {
		hubPendingSpuDto.setHubBrandNo(hubSpuPendingDtoTemp.getHubBrandNo());
		hubPendingSpuDto.setHubCategoryNo(hubSpuPendingDtoTemp.getHubCategoryNo());
		hubPendingSpuDto.setHubColor(hubSpuPendingDtoTemp.getHubColor());
		hubPendingSpuDto.setHubColorNo(hubSpuPendingDtoTemp.getHubColorNo());
		hubPendingSpuDto.setHubGender(hubSpuPendingDtoTemp.getHubGender());
		hubPendingSpuDto.setHubMaterial(hubSpuPendingDtoTemp.getHubMaterial());
		hubPendingSpuDto.setHubOrigin(hubSpuPendingDtoTemp.getHubOrigin());
		hubPendingSpuDto.setHubSeason(hubSpuPendingDtoTemp.getHubSeason());
		hubPendingSpuDto.setHubSpuNo(hubSpuPendingDtoTemp.getHubSpuNo());
		hubPendingSpuDto.setSpuModel(hubSpuPendingDtoTemp.getSpuModel());
		hubPendingSpuDto.setSpuName(hubSpuPendingDtoTemp.getSpuName());
		hubPendingSpuDto.setSpuSeasonState((byte) 1);
		hubPendingSpuDto.setCatgoryState((byte) 1);
		hubPendingSpuDto.setMaterialState((byte) 1);
		hubPendingSpuDto.setOriginState((byte) 1);
		hubPendingSpuDto.setSpuBrandState((byte) 1);
		hubPendingSpuDto.setSpuColorState((byte) 1);
		hubPendingSpuDto.setSpuGenderState((byte) 1);
		hubPendingSpuDto.setSpuModelState((byte) 1);
		hubPendingSpuDto.setHandleState(HandleState.PENDING_HANDING_EXIST.getIndex());
	}

	private void convertHubSpuToPendingSpu(HubSpuDto hubSpuDto,HubSpuPendingDto hubPendingSpuDto) {
		hubPendingSpuDto.setHubBrandNo(hubSpuDto.getBrandNo());
		hubPendingSpuDto.setHubCategoryNo(hubSpuDto.getCategoryNo());
		hubPendingSpuDto.setHubColor(hubSpuDto.getHubColor());
		hubPendingSpuDto.setHubColorNo(hubSpuDto.getHubColorNo());
		hubPendingSpuDto.setHubGender(hubSpuDto.getGender());
		hubPendingSpuDto.setHubMaterial(hubSpuDto.getMaterial());
		hubPendingSpuDto.setHubOrigin(hubSpuDto.getOrigin());
		hubPendingSpuDto.setHubSeason(hubSpuDto.getMarketTime() + "_" + hubSpuDto.getSeason());
		hubPendingSpuDto.setHubSpuNo(hubSpuDto.getSpuNo());
		hubPendingSpuDto.setSpuModel(hubSpuDto.getSpuModel());
		hubPendingSpuDto.setSpuName(hubSpuDto.getSpuName());
		hubPendingSpuDto.setSpuSeasonState((byte) 1);
		hubPendingSpuDto.setCatgoryState((byte) 1);
		hubPendingSpuDto.setMaterialState((byte) 1);
		hubPendingSpuDto.setOriginState((byte) 1);
		hubPendingSpuDto.setSpuBrandState((byte) 1);
		hubPendingSpuDto.setSpuColorState((byte) 1);
		hubPendingSpuDto.setSpuGenderState((byte) 1);
		hubPendingSpuDto.setSpuModelState((byte) 1);
		hubPendingSpuDto.setUpdateTime(new Date());
		hubPendingSpuDto.setHandleState(HandleState.HUB_EXIST.getIndex());
	}

	public String updateSpuPendingState(Long spuPendingId) {
		
		HubSpuPendingDto hubSpuPendingDto = new HubSpuPendingDto();
		HubSpuPendingDto spuPending = hubPendingSpuService.findHubSpuPendingByPrimary(spuPendingId);
		if(spuPending!=null){
			String hubSpuNo = spuPending.getHubSpuNo();
			boolean isSkuPassing = true;
			boolean isAllSkuFilter = true;
			boolean isSendToHub = false;
			int totalStock = 0;
			boolean isExistSku = false;
			List<HubSkuPendingDto> listSku = hubPendingSkuService.findHubSkuPendingBySpuPendingId(spuPendingId);
			if(listSku!=null&&listSku.size()>0){
				isExistSku = true;
				for(HubSkuPendingDto sku:listSku){
					totalStock += sku.getStock();
					if(sku.getSpSkuSizeState()!=null&&sku.getFilterFlag()!=null&&sku.getSpSkuSizeState()==SpSkuSizeState.UNHANDLED.getIndex()&&sku.getFilterFlag()==FilterFlag.EFFECTIVE.getIndex()){
						isSkuPassing = false;
					}
					if(sku.getFilterFlag()!=null&&sku.getFilterFlag()==FilterFlag.EFFECTIVE.getIndex()){
						isAllSkuFilter = false;
					}
					//如果含有待处理并
					if(sku.getSkuState()!=null&&sku.getSkuState()==SpuState.HANDLING.getIndex()){
						isSendToHub = true;
					}
				}
			}else{
				hubSpuPendingDto.setMemo("无sku信息");
				log.info("******spuPendingId:"+spuPendingId+"下无sku信息");
			}
			//sku不存在或者所有sku已被过滤
			if(isAllSkuFilter){
				hubSpuPendingDto.setMemo("sku都被过滤了");
				isSkuPassing = false;
			}
		
			if(hubSpuNo!=null){
				if(isSkuPassing){
					if(isSendToHub){
						hubSpuPendingDto.setMemo("自动进入待选品");
						hubSpuPendingDto.setSpuState(SpuState.HANDLED.getIndex());
						hubSpuPendingDto.setHandleFrom(HandleFromState.AUTOMATIC_HANDLE.getIndex());
						if(!sendToHub(spuPendingId,hubSpuNo)){
							return "sendToHub失败";
						}
						log.info("*****"+spuPending.getSupplierId()+":"+spuPending.getSpuModel()+"自动进入待选品");
					}else{
						log.info("*****"+spuPending.getSupplierId()+":"+spuPending.getSpuModel()+"spu下sku都已处理，不再推送");
						return null;
					}
					
				}else{
					hubSpuPendingDto.setHandleFrom(HandleFromState.HAND_HANDLE.getIndex());
					hubSpuPendingDto.setSpuState(SpuState.INFO_PECCABLE.getIndex());
				}
			}else{
				if(isSpuPass(spuPending)){
					if(isSkuPassing){
						log.info("*****"+spuPending.getSupplierId()+":"+spuPending.getSpuModel()+"自动进入待复合");
						hubSpuPendingDto.setMemo("自动进入待复合");
						hubSpuPendingDto.setHandleFrom(HandleFromState.AUTOMATIC_HANDLE.getIndex());
						hubSpuPendingDto.setSpuState(SpuState.INFO_IMPECCABLE.getIndex());
					}else{
						hubSpuPendingDto.setMemo("sku下有校验失败的尺码");
						hubSpuPendingDto.setHandleFrom(HandleFromState.HAND_HANDLE.getIndex());
						hubSpuPendingDto.setSpuState(SpuState.INFO_PECCABLE.getIndex());
					}
				}else{
					hubSpuPendingDto.setMemo("spu校验失败");
					hubSpuPendingDto.setSpuState(SpuState.INFO_PECCABLE.getIndex());
				}
			}
			if(isExistSku&&totalStock>=0){
				hubSpuPendingDto.setStockState(StockState.HANDLED.getIndex());
			}else if(isExistSku&&totalStock<=0){
				hubSpuPendingDto.setStockState(StockState.NOSTOCK.getIndex());
			}else{
				hubSpuPendingDto.setStockState(StockState.NOSKU.getIndex());
			}
			hubSpuPendingDto.setSpuPendingId(spuPendingId);
			hubSpuPendingDto.setUpdateTime(new Date());
			hubPendingSpuService.updateHubSpuPendingByPrimaryKey(hubSpuPendingDto);
		}else{
			log.info("spuPendingId为空");
		}
		
		return null;
	}
	
	private boolean isSpuPass(HubSpuPendingDto spuPending){
		if(spuPending.getSpuBrandState()!=null&&spuPending.getSpuBrandState()==SpuBrandState.HANDLED.getIndex()&&
				spuPending.getCatgoryState()!=null&&spuPending.getCatgoryState()==SpuBrandState.HANDLED.getIndex()&&
						spuPending.getSpuColorState()!=null&&spuPending.getSpuColorState()==SpuBrandState.HANDLED.getIndex()&&
								spuPending.getSpuGenderState()!=null&&spuPending.getSpuGenderState()==SpuBrandState.HANDLED.getIndex()&&
										spuPending.getMaterialState()!=null&&spuPending.getMaterialState()==SpuBrandState.HANDLED.getIndex()&&
												spuPending.getOriginState()!=null&&spuPending.getOriginState()==SpuBrandState.HANDLED.getIndex()&&
														spuPending.getSpuSeasonState()!=null&&spuPending.getSpuSeasonState()==SpuBrandState.HANDLED.getIndex()&&
																spuPending.getSpuModelState()!=null&&spuPending.getSpuModelState()==SpuBrandState.HANDLED.getIndex()){
			return true;	
		}else{
			return false;
		}
		
	}
	
	public boolean sendToHub(Long spuPendingId,String hubSpuNo) {
		
		HubSpuDto hubSpu = hubSpuService.findHubSpuByHubSpuNo(hubSpuNo);
		Long hubSpuId = null;
		if(hubSpu==null){
			return false;
		}
		hubSpuId = hubSpu.getSpuId();
		HubPendingDto hubPendingDto = new HubPendingDto();
		hubPendingDto.setHubSpuId(hubSpuId);	
		hubPendingDto.setHubSpuPendingId(spuPendingId);
		log.info("pendingToHub.addSkuOrSkuSupplierMapping推送参数:{}", hubPendingDto);
		return pengdingToHubGateWay.addSkuOrSkuSupplierMapping(hubPendingDto);
	}
}
