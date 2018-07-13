package com.shangpin.ephub.product.business.ui.pending.service;

import com.shangpin.ephub.client.business.supplier.dto.SupplierInHubDto;
import com.shangpin.ephub.client.data.mysql.enumeration.*;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.*;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.client.util.DateTimeUtil;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.service.check.HubCheckService;
import com.shangpin.ephub.product.business.rest.gms.dto.BrandDom;
import com.shangpin.ephub.product.business.rest.gms.dto.FourLevelCategory;
import com.shangpin.ephub.product.business.rest.gms.dto.SupplierDTO;
import com.shangpin.ephub.product.business.rest.hubpending.pendingproduct.service.PendingProductCommonService;
import com.shangpin.ephub.product.business.rest.hubpending.spu.service.HubPendingSpuCheckService;
import com.shangpin.ephub.product.business.rest.model.controller.HubBrandModelRuleController;
import com.shangpin.ephub.product.business.rest.model.dto.BrandModelDto;
import com.shangpin.ephub.product.business.rest.model.result.BrandModelResult;
import com.shangpin.ephub.product.business.service.pending.CheckService;
import com.shangpin.ephub.product.business.service.pending.PendingService;
import com.shangpin.ephub.product.business.service.pending.SkuPendingService;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.service.supplier.SupplierInHubService;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.service.impl.PendingSkuService;
import com.shangpin.ephub.product.business.ui.pending.util.JavaUtil;
import com.shangpin.ephub.product.business.ui.pending.vo.*;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**

 *
 */
@Service
@Slf4j
public class WebSpiderService {
	@Autowired
	PendingProductCommonService pendingProductCommonService;
	@Autowired
	private HubCheckService hubCheckService;
	@Autowired
	private HubPendingSpuCheckService hubPendingSpuCheckService;
	@Autowired
	private HubBrandModelRuleController hubBrandModelRule;
	@Autowired
	private HubSpuGateWay hubSpuGateway;
	@Autowired
	private HubSupplierSkuGateWay hubSupplierSkuGateWay;


	@Autowired
	private HubSlotSpuService slotSpuService;

	@Autowired
	private SupplierInHubService supplierInHubService;

	@Autowired
	private CheckService checkService;
	@Autowired
	private SkuPendingService skuPendingService;


	@Autowired
	PendingService pendingService;
	@Autowired
	HubSkuPendingGateWay hubSkuPendingGateWay;
    @Autowired
	HubSpuPendingGateWay hubSpuPendingGateWay;




	public HubResponse<PendingUpdatedVo> saveWebSpider(PendingProductDto pendingProductDto) {
		log.info("接收到的爬虫保存的数据：{}"+JsonUtil.serialize(pendingProductDto));
		HubResponse<PendingUpdatedVo> response = new HubResponse<PendingUpdatedVo>();
		response.setCode("0"); //初始设置为成功
		PendingUpdatedVo updatedVo = null;
		boolean pass = true; //全局用来判断整条数据是否校验通过
		boolean isSkuPass = false;
		HubSpuDto hubSpuDto = null;
		try {
			if(null != pendingProductDto){

				List<HubSpuDto>  hubSpuDtos = selectHubSpu(pendingProductDto.getSpuModel(),pendingProductDto.getHubBrandNo());
				if(null!=hubSpuDtos&&hubSpuDtos.size()>0){
					hubSpuDto = hubSpuDtos.get(0);
					convertHubSpuDtoToPendingSpu(hubSpuDto,pendingProductDto);
					if(!hubCheckService.checkHubSeason(pendingProductDto.getHubSeason())){
						pendingProductDto.setHubSeason(hubSpuDto.getMarketTime()+"_"+hubSpuDto.getSeason());
					}
					setSpuState(pendingProductDto);

				}
				/**
				 * 校验货号
				 */
				BrandModelResult brandModelResult = verifyProductModle(pendingProductDto);

				boolean isHaveHubSpu = false;
				//返回信息中的SKU的信息状态
				List<PendingSkuUpdatedVo> skuMsgReturn = new ArrayList<PendingSkuUpdatedVo>();

				//如果货号校验通过  则先查询是否与HUB_SPU 如果有 直接赋值 并自动过滤尺码
				if(null!=brandModelResult&&brandModelResult.isPassing()){
					//检验品牌是否
					if(null==hubSpuDto) hubSpuDto = findAndUpdatedFromHubSpu(brandModelResult.getBrandMode(),pendingProductDto);
					if(null!=hubSpuDto){
						setSpuState(pendingProductDto);
						//尺码处理  ,从页面上获取的
						isHaveHubSpu = true;
						String  skuHandleReuslt = skuPendingService.setWaitHandleSkuPendingSize(pendingProductDto.getSpuPendingId(),pendingProductDto.getHubBrandNo(),pendingProductDto.getHubCategoryNo(),pendingProductDto.getHubSkus(),skuMsgReturn);
						if("0".equals(skuHandleReuslt)){//全部通过
							isSkuPass = true;
						}else if("1".equals(skuHandleReuslt)){//全部没有映射上
							isSkuPass = false ;
						}else if("2".equals(skuHandleReuslt)){//有无法处理的SKU，需要维护品牌品类尺码的对应关系
							isSkuPass = false ;
						}

					}
					//
				}
				if(!isHaveHubSpu){//无HubSpu的处理逻辑
					/**
					 * 校验品类和性别是否一致
					 */
					boolean checkCategoryAndGender = checkService.checkCategoryAndGender(pendingProductDto.getHubGender(), pendingProductDto.getHubCategoryNo());
					if(brandModelResult.isPassing()){
						if(checkCategoryAndGender) {

							//	hubSpuDto = findAndUpdatedFromHubSpu(brandModelResult.getBrandMode(),pendingProductDto);
							if (null == hubSpuDto) {
								HubPendingSpuCheckResult spuResult = hubPendingSpuCheckService.checkHubPendingSpu(pendingProductDto);
								if (spuResult.isPassing()) {
									setSpuState(pendingProductDto);

								} else {
									checkSpuState(pendingProductDto, spuResult);
									pass = false;
									log.info("pending spu校验失败，不更新：" + spuResult.getResult());
									updatedVo = setErrorMsg(response, pendingProductDto.getSpuPendingId(), spuResult.getResult());
								}
							}
						}else{
							pass = false ;
							log.info("pending spu校验失败，不更新：性别与品类不符。");
							updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),"性别与品类不符");
						}
					}else{
						pass = false ;
						log.info("pending spu校验失败，不更新：货号校验不通过。");
						updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),"货号校验不通过");
					}
					//开始校验sku
					List<HubSkuPendingDto> pengdingSkus = pendingProductDto.getHubSkus();
					if(null == updatedVo){
						updatedVo = new PendingUpdatedVo();
						updatedVo.setSpuResult("");
						updatedVo.setSpuPendingId(pendingProductDto.getSpuPendingId());
					}


					if(pengdingSkus!=null&&pengdingSkus.size()>0){

						pass = updateSkuPendingValue(pendingProductDto, response, pass, hubSpuDto, pengdingSkus, skuMsgReturn);
						//判断全局尺码是否都是排除
						for(HubSkuPendingDto hubSkuPendingDto : pengdingSkus) {
							String hubSkuSize = hubSkuPendingDto.getHubSkuSize();
							hubSkuSize = StringUtils.isEmpty(hubSkuSize) ? "" : hubSkuSize;
							if (hubSkuSize.startsWith("排除")) {

							} else {
								isSkuPass = true;
							}
						}

						if(!isSkuPass){
							updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),"尺码都被排除");
						}
					}

					updatedVo.setSkus(skuMsgReturn);
					response.setErrorMsg(updatedVo);
				}
				if(0==pendingProductDto.getSupplierSpuId()){
					pendingProductDto.setSupplierSpuId(null);
				}

				if(pass&&isSkuPass){
					pendingProductDto.setSpuState(SpuState.HANDLED.getIndex());
				}
				pendingProductDto.setCreateTime(null);
				pendingProductDto.setPicState(null);
				log.info("更新参数："+JsonUtil.serialize(pendingProductDto));



				hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingProductDto);
			}


			response.setCode("0");
		} catch (Exception e) {
			log.error("供应商："+pendingProductDto.getSupplierNo()+"产品："+pendingProductDto.getSpuPendingId()+"更新时发生异常："+e.getMessage());
			setErrorMsg(response,pendingProductDto.getSpuPendingId(),"服务器错误");
		}
		log.info("返回的校验结果：+"+JsonUtil.serialize(response));
		return response;
	}


	private boolean updateSkuPendingValue(PendingProductDto pendingProductDto, HubResponse<PendingUpdatedVo> response, boolean pass, HubSpuDto hubSpuDto, List<HubSkuPendingDto> pengdingSkus, List<PendingSkuUpdatedVo> skus) {
		for(HubSkuPendingDto hubSkuPendingDto : pengdingSkus){


			String hubSkuSize = hubSkuPendingDto.getHubSkuSize();
			hubSkuSize = StringUtils.isEmpty(hubSkuSize) ? "" : hubSkuSize;
			log.info("从页面接收到的尺码信息===="+hubSkuSize);
			if(hubSkuSize.startsWith("排除")){
				hubSkuPendingDto.setHubSkuSizeType("排除");
				hubSkuPendingDto.setHubSkuSize(null);//目的是不更新尺码值
				hubSkuPendingDto.setFilterFlag(FilterFlag.INVALID.getIndex());
				hubSkuPendingDto.setSkuState(SkuState.INFO_IMPECCABLE.getIndex());
			}else if(hubSkuSize.startsWith("尺寸")){

				hubSkuPendingDto.setHubSkuSizeType("尺寸");
				hubSkuPendingDto.setHubSkuSize(hubSkuSize.substring(hubSkuSize.indexOf(":")+1));
				if(null != hubSpuDto){
					hubSkuPendingDto.setSkuState(SpuState.INFO_IMPECCABLE.getIndex());
				}else{
					hubSkuPendingDto.setSkuState(SpuState.INFO_IMPECCABLE.getIndex());
				}
				hubSkuPendingDto.setFilterFlag(FilterFlag.EFFECTIVE.getIndex());
				hubSkuPendingDto.setSpSkuSizeState(SkuState.INFO_IMPECCABLE.getIndex());
			}else{

				String [] arr = hubSkuSize.split(":",-1);
				String sizeType = null;
				String sizeValue = null;
				if(arr.length==2){
					sizeType = arr[0];
					sizeValue = arr[1];
				}else{
					sizeValue = hubSkuSize;
				}
				pass = setSkuPendingSize(pendingProductDto, response, pass, hubSpuDto, skus, hubSkuPendingDto, hubSkuSize, sizeType, sizeValue);
			}
			hubSkuPendingDto.setSupplyPrice(null);
			hubSkuPendingDto.setMarketPrice(null);
			hubSkuPendingDto.setSalesPrice(null);
			hubSkuPendingGateWay.updateByPrimaryKeySelective(hubSkuPendingDto);
		}
		return pass;
	}

	private boolean setSkuPendingSize(PendingProductDto pendingProductDto, HubResponse<PendingUpdatedVo> response, boolean pass, HubSpuDto hubSpuDto, List<PendingSkuUpdatedVo> skus, HubSkuPendingDto hubSkuPendingDto, String hubSkuSize, String sizeType, String sizeValue) {
		HubPendingSkuCheckResult result = hubCheckService.hubSizeExist(pendingProductDto.getHubCategoryNo(), pendingProductDto.getHubBrandNo(), sizeType,sizeValue);
		if(result.isPassing()){
			if(null != hubSpuDto){
				hubSkuPendingDto.setScreenSize(result.getSizeId());
				hubSkuPendingDto.setSkuState(SpuState.INFO_IMPECCABLE.getIndex());
				hubSkuPendingDto.setSpSkuSizeState(SkuState.INFO_IMPECCABLE.getIndex());
				hubSkuPendingDto.setFilterFlag(FilterFlag.EFFECTIVE.getIndex());
			}else{
				hubSkuPendingDto.setScreenSize(result.getSizeId());
				hubSkuPendingDto.setSkuState(SkuState.INFO_IMPECCABLE.getIndex());
				hubSkuPendingDto.setSpSkuSizeState(SkuState.INFO_IMPECCABLE.getIndex());
				hubSkuPendingDto.setFilterFlag(FilterFlag.EFFECTIVE.getIndex());
			}
		}else{
			pass = false ;
			log.info("pending sku校验失败，不更新："+result.getMessage()+"|原始数据："+hubSkuSize);
			response.setCode("1");
			PendingSkuUpdatedVo skuUpdatedVo = new PendingSkuUpdatedVo();
			skuUpdatedVo.setSkuPendingId(hubSkuPendingDto.getSkuPendingId());
			skuUpdatedVo.setSkuResult(result.getMessage());
			skus.add(skuUpdatedVo);
		}
		if(hubSkuSize.contains(":")){
			hubSkuPendingDto.setHubSkuSizeType(hubSkuSize.substring(0,hubSkuSize.indexOf(":")));
			hubSkuPendingDto.setHubSkuSize(hubSkuSize.substring(hubSkuSize.indexOf(":")+1));
		}
		return pass;
	}

	private void setSpuState(PendingProductDto pendingProductDto) {
		pendingProductDto.setCatgoryState((byte)1);
		pendingProductDto.setMaterialState((byte)1);
		pendingProductDto.setOriginState((byte)1);
		pendingProductDto.setSpuBrandState((byte)1);
		pendingProductDto.setSpuColorState((byte)1);
		pendingProductDto.setSpuGenderState((byte)1);
		pendingProductDto.setSpuModelState((byte)1);
		pendingProductDto.setSpuSeasonState((byte)1);

	}






    public HubResponse<List<PendingUpdatedVo>> batchSaveWebSpider(PendingProducts pendingProducts) {
        HubResponse<List<PendingUpdatedVo>> response = new HubResponse<>();
        response.setCode("0"); //初始设置为成功
        if(null != pendingProducts && null != pendingProducts.getProduts() && pendingProducts.getProduts().size()>0){


//            StringBuilder errorMsg = new StringBuilder();
			List<PendingUpdatedVo> updatedVos = new ArrayList<PendingUpdatedVo>();
            for(PendingProductDto pendingProductDto : pendingProducts.getProduts()){
                HubResponse<PendingUpdatedVo> everyResponse = saveWebSpider(pendingProductDto);
                if("1".equals(everyResponse.getCode())){
					updatedVos.add(everyResponse.getErrorMsg());
//                    errorMsg.append("产品货号："+ pendingProductDto.getSpuModel()+" 选品失败.原因："+everyResponse.getErrorMsg()).append(",");
                }
            }
            if(updatedVos.size() > 0){

                response.setCode("1");
                response.setErrorMsg(updatedVos);
            }


        }
        return response;
    }





	/***************************************************************************************************************************
	 *       以下为内部调用私有方法
	 /**************************************************************************************************************************/
	/**
	 *
	 * @param lists
	 * @return
	 */
	private List<String> findSpPicUrls(List<HubSpuPendingPicDto> lists){
		if(CollectionUtils.isNotEmpty(lists)){
			List<String> spPicUrls = new ArrayList<String>();
			for(HubSpuPendingPicDto dto : lists){
				if(PicHandleState.HANDLED.getIndex() == dto.getPicHandleState()){
					spPicUrls.add(dto.getSpPicUrl());
				}
			}
			return spPicUrls;
		}else{
			return null;
		}
	}
	/**
	 *
	 * @param lists
	 * @return
	 */
	private List<String> findSupplierUrls(List<HubSpuPendingPicDto> lists){
		if(CollectionUtils.isNotEmpty(lists)){
			List<String> spPicUrls = new ArrayList<String>();
			for(HubSpuPendingPicDto dto : lists){
				spPicUrls.add(dto.getPicUrl());
			}
			return spPicUrls;
		}else{
			return null;
		}
	}

	/**
	 * 查找supplier sku
	 * @param supplierSpuId
	 * @return
	 */
	private List<HubSupplierSkuDto> findHubSupplierSku(Long supplierSpuId) {
		HubSupplierSkuCriteriaDto skuCriteria = new HubSupplierSkuCriteriaDto();
		skuCriteria.setPageNo(1);
		skuCriteria.setPageSize(100);
		skuCriteria.setOrderByClause("supplier_sku_size");
		skuCriteria.setFields("supplier_sku_size,stock,supply_price,supply_price_currency,market_price,market_price_currencyOrg");
		skuCriteria.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId);
		return hubSupplierSkuGateWay.selectByCriteria(skuCriteria);
	}
	/**
	 * 设置校验失败结果
	 * @param response
	 * @param spuPengdingId
	 * @param errorMsg
	 */
	protected PendingUpdatedVo setErrorMsg(HubResponse<PendingUpdatedVo> response,Long spuPengdingId,String errorMsg){
		response.setCode("1");
		PendingUpdatedVo updatedVo = new PendingUpdatedVo();
		updatedVo.setSpuPendingId(spuPengdingId);
		updatedVo.setSpuResult(errorMsg);
		response.setErrorMsg(updatedVo);
		return updatedVo;
	}
	/**
	 * 能根据品牌货号从hub标准库中找到记录，则直接用标准库中的属性更新pending库
	 * @param spuModel 标准货号
	 * @param pendingProductDto 待更新的pending spu
	 * @return
	 */
	protected HubSpuDto findAndUpdatedFromHubSpu(String spuModel,PendingProductDto pendingProductDto){
		pendingProductDto.setSpuModel(spuModel);
		List<HubSpuDto> hubSpus = selectHubSpu(pendingProductDto.getSpuModel(),pendingProductDto.getHubBrandNo());
		if(null != hubSpus && hubSpus.size()>0){
			convertHubSpuDtoToPendingSpu(hubSpus.get(0),pendingProductDto);
			if(!hubCheckService.checkHubSeason(pendingProductDto.getHubSeason())){
				pendingProductDto.setHubSeason(hubSpus.get(0).getMarketTime()+"_"+hubSpus.get(0).getSeason());
			}
			return hubSpus.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 验证货号
	 * @param pendingProductDto
	 * @return
	 */
	protected BrandModelResult verifyProductModle(PendingProductDto pendingProductDto) {
		BrandModelDto brandModelDto = new BrandModelDto();
		brandModelDto.setBrandMode(pendingProductDto.getSpuModel());
		brandModelDto.setHubBrandNo(pendingProductDto.getHubBrandNo());
		brandModelDto.setHubCategoryNo(pendingProductDto.getHubCategoryNo());
		BrandModelResult brandModelResult=  hubBrandModelRule.verifyWithCategory(brandModelDto);
		if(null!=brandModelResult&&!brandModelResult.isPassing()){
			//未通过替换 查找货号规则 若存在 替换代码
			pendingProductDto.setSpuModel(hubBrandModelRule.replaceSymbol(brandModelDto));

		}
		return brandModelResult;
	}
	/**
	 * 将hub_spu中的信息付给pending_spu
	 * @param hubSpuDto
	 * @param
	 */
	private void convertHubSpuDtoToPendingSpu(HubSpuDto hubSpuDto,PendingProductDto hubPendingSpuDto){
		hubPendingSpuDto.setHubBrandNo(hubSpuDto.getBrandNo());
		hubPendingSpuDto.setHubCategoryNo(hubSpuDto.getCategoryNo());
		if(hubPendingSpuDto.getHubColor()!=null&&hubPendingSpuDto.getHubColor().equals(hubSpuDto.getHubColor())){
			hubPendingSpuDto.setHubColor(hubSpuDto.getHubColor());
		}
		hubPendingSpuDto.setHubColorNo(hubSpuDto.getHubColorNo());
		hubPendingSpuDto.setHubGender(hubSpuDto.getGender());
		hubPendingSpuDto.setHubMaterial(hubSpuDto.getMaterial());
		hubPendingSpuDto.setHubOrigin(hubSpuDto.getOrigin());
//		hubPendingSpuDto.setHubSeason(hubSpuDto.getMarketTime()+"_"+hubSpuDto.getSeason()); 季节可以修改 ，所以不赋值
		hubPendingSpuDto.setHubSpuNo(hubSpuDto.getSpuNo());
		hubPendingSpuDto.setSpuModel(hubSpuDto.getSpuModel());
		hubPendingSpuDto.setSpuName(hubSpuDto.getSpuName());
	}
	/**
	 * 根据品牌和货号查找hub_spu表中的记录
	 * @param spuModle
	 * @param hubBrandNo
	 * @return
	 */
	protected List<HubSpuDto> selectHubSpu(String spuModle,String hubBrandNo) {
		HubSpuCriteriaDto criteria = new HubSpuCriteriaDto();
		criteria.createCriteria().andSpuModelEqualTo(spuModle).andBrandNoEqualTo(hubBrandNo);
		return  hubSpuGateway.selectByCriteria(criteria);
	}




	private void handleSlotSpuAndSupplier(PendingProductDto pendingProductDto) throws Exception {
		if(pendingProductDto.getSpuModelState()== SpuModelState.VERIFY_PASSED.getIndex()&&pendingProductDto.getSpuBrandState()== SpuBrandState.HANDLED.getIndex()){
			if(pendingProductDto.getCatgoryState()== CatgoryState.PERFECT_MATCHED.getIndex()||
					pendingProductDto.getCatgoryState()==CatgoryState.MISMATCHING.getIndex()) {

				slotSpuService.addSlotSpuAndSupplier(pendingProductDto);
			}
		}
	}

	private void checkSpuState(PendingProductDto hubPendingSpuDto, HubPendingSpuCheckResult hubPendingSpuCheckResult) {
		if(hubPendingSpuCheckResult.isSpuModel()){
			hubPendingSpuDto.setSpuModelState((byte)1);
		}else{
			hubPendingSpuDto.setSpuModelState((byte)0);
		}

		if(hubPendingSpuCheckResult.isCategory()){
			hubPendingSpuDto.setCatgoryState((byte)1);
		}else{
			hubPendingSpuDto.setCatgoryState((byte)0);
		}

		if(hubPendingSpuCheckResult.isMaterial()){
			hubPendingSpuDto.setMaterialState((byte)1);
		}else{
			hubPendingSpuDto.setMaterialState((byte)0);
		}

		if(hubPendingSpuCheckResult.isOriginal()){
			hubPendingSpuDto.setOriginState((byte)1);
		}else{
			hubPendingSpuDto.setOriginState((byte)0);
		}

		if(hubPendingSpuCheckResult.isBrand()){
			hubPendingSpuDto.setSpuBrandState((byte)1);
		}else{
			hubPendingSpuDto.setSpuBrandState((byte)0);
		}

		if(hubPendingSpuCheckResult.isColor()){
			hubPendingSpuDto.setSpuColorState((byte)1);
		}else{
			hubPendingSpuDto.setSpuColorState((byte)0);
		}

		if(hubPendingSpuCheckResult.isGender()){
			hubPendingSpuDto.setSpuGenderState((byte)1);
		}else{
			hubPendingSpuDto.setSpuGenderState((byte)0);
		}

		if(hubPendingSpuCheckResult.isSeasonName()){
			hubPendingSpuDto.setSpuSeasonState((byte)1);
		}else{
			hubPendingSpuDto.setSpuSeasonState((byte)0);
		}
	}




}
