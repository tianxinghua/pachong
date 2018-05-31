package com.shangpin.ephub.product.business.ui.pending.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.shangpin.ephub.product.business.common.enumeration.DataBusinessStatus;
import com.shangpin.ephub.product.business.common.enumeration.SpuStatus;
import com.shangpin.ephub.product.business.service.pending.PendingService;
import com.shangpin.ephub.product.business.service.pending.SkuPendingService;
import com.shangpin.ephub.product.business.ui.pending.vo.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.business.supplier.dto.SupplierInHubDto;
import com.shangpin.ephub.client.data.mysql.enumeration.AuditState;
import com.shangpin.ephub.client.data.mysql.enumeration.CatgoryState;
import com.shangpin.ephub.client.data.mysql.enumeration.FilterFlag;
import com.shangpin.ephub.client.data.mysql.enumeration.PicHandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.enumeration.SkuState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuBrandState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuModelState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuPendingStudioState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.enumeration.StockState;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
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
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.service.supplier.SupplierInHubService;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.service.HubSpuPendingNohandleReasonService;
import com.shangpin.ephub.product.business.ui.pending.service.IHubSpuPendingPicService;
import com.shangpin.ephub.product.business.ui.pending.util.JavaUtil;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:PendingProductService </p>
 * <p>Description: 待处理页面Service实现类</p>
 * <p>Company: www.shangpin.com</p>
 * @author lubaijiang
 * @date 2016年12月21日 下午5:17:57
 *
 */
@Service
@Slf4j
public class PendingProductService extends PendingSkuService{
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
	//    @Autowired
//    private PengdingToHubGateWay pendingToHubGateWay;
	@Autowired
	private IHubSpuPendingPicService  hubSpuPendingPicService;

	@Autowired
	private HubSlotSpuService slotSpuService;

	@Autowired
	private SupplierInHubService supplierInHubService;
	@Autowired
	private HubSpuPendingNohandleReasonService reasonService;
	@Autowired
	private CheckService checkService;
	@Autowired
	private SkuPendingService skuPendingService;


	@Autowired
	PendingService pendingService;

	@Override
	public PendingProducts findPendingProducts(PendingQuryDto pendingQuryDto,boolean flag){
		log.info("findPendingProducts接收到的查询条件："+JsonUtil.serialize(pendingQuryDto));
		long start = System.currentTimeMillis();
		PendingProducts pendingProducts = new PendingProducts();
		List<PendingProductDto> products = new ArrayList<PendingProductDto>();
		try {
			if(null !=pendingQuryDto){
				HubSpuPendingCriteriaDto criteriaDto = findhubSpuPendingCriteriaFromPendingQury(pendingQuryDto);
				int total = hubSpuPendingGateWay.countByCriteria(criteriaDto);
				log.info("待处理页面查询返回数据个数================"+total);
				if(total>0){
					List<HubSpuPendingDto> pendingSpus = hubSpuPendingGateWay.selectByCriteria(criteriaDto);
					List<Long> spuPendingIds = new ArrayList<Long>();
//                    List<Long> spuPendingIds2 = new ArrayList<Long>();
					for(HubSpuPendingDto pendingSpu : pendingSpus){
						spuPendingIds.add(pendingSpu.getSpuPendingId());
//                    	if(null != pendingSpu.getMsgMissHandleState() && MsgMissHandleState.HAVE_HANDLED.getIndex() == pendingSpu.getMsgMissHandleState()){
//                    		spuPendingIds2.add(pendingSpu.getSpuPendingId());
//                    	}
					}
					long start_sku = System.currentTimeMillis();
					/**
					 * 查找sku信息
					 */
					Map<Long,List<HubSkuPendingDto>> pendingSkus = findPendingSku(spuPendingIds,flag);
					/**
					 * 查找错误信息
					 */
					Map<Long,String> errorReasons = reasonService.findAllErrorReason(spuPendingIds);

					log.info("--->待处理查询sku耗时{}",System.currentTimeMillis()-start_sku);
					for(HubSpuPendingDto pendingSpu : pendingSpus){
						PendingProductDto pendingProduct = JavaUtil.convertHubSpuPendingDto2PendingProductDto(pendingSpu);
						SupplierDTO supplierDTO = supplierService.getSupplier(pendingSpu.getSupplierNo());
						pendingProduct.setSupplierName(null != supplierDTO ? supplierDTO.getSupplierName() : "");
						FourLevelCategory category = categoryService.getGmsCateGory(pendingProduct.getHubCategoryNo());
						String hubCategoryName = categoryService.getHubCategoryNameByHubCategory(pendingProduct.getHubCategoryNo(), category);
						pendingProduct.setHubCategoryName(null != hubCategoryName ? hubCategoryName : pendingProduct.getHubCategoryNo());
						BrandDom brand = brandService.getGmsBrand(pendingProduct.getHubBrandNo());
						pendingProduct.setHubBrandName(null != brand ? brand.getBrandEnName() : pendingProduct.getHubBrandNo());
						List<HubSkuPendingDto> skus = pendingSkus.get(pendingSpu.getSpuPendingId());
						pendingProduct.setHubSkus(CollectionUtils.isNotEmpty(skus) ? skus : new ArrayList<HubSkuPendingDto>());
						List<HubSpuPendingPicDto> picurls = hubSpuPendingPicService.findSpPicUrl(pendingSpu.getSupplierId(),pendingSpu.getSupplierSpuNo());
						pendingProduct.setSpPicUrl(findMainUrl(picurls));
						pendingProduct.setPicUrls(findSpPicUrls(picurls));
						pendingProduct.setSupplierUrls(findSupplierUrls(picurls));
						pendingProduct.setPicReason(CollectionUtils.isNotEmpty(picurls) ? picurls.get(0).getMemo() : picReason);
						pendingProduct.setUpdateTimeStr(null != pendingSpu.getUpdateTime() ? DateTimeUtil.getTime(pendingSpu.getUpdateTime()) : "");
						pendingProduct.setCreatTimeStr(null != pendingSpu.getCreateTime() ? DateTimeUtil.getTime(pendingSpu.getCreateTime()) : "");
						pendingProduct.setAuditDateStr(null != pendingSpu.getAuditDate() ? DateTimeUtil.getTime(pendingSpu.getAuditDate()) : "");
						pendingProduct.setErrorReason(null != errorReasons ? errorReasons.get(pendingSpu.getSpuPendingId()) : "");
						pendingProduct.setSupplierSpuDesc(null != pendingSpu.getSpuDesc() ? pendingSpu.getSpuDesc() : "");
						products.add(pendingProduct);
					}
					pendingProducts.setProduts(products);
				}
				pendingProducts.setTotal(total);
			}
		} catch (Exception e) {
			log.error("待处理页面查询异常："+e.getMessage(),e);
		}
		log.info("--->待处理查询总耗时{}",System.currentTimeMillis()-start);
		return pendingProducts;
	}

	@Override
	public HubResponse<PendingUpdatedVo> updatePendingProduct(PendingProductDto pendingProductDto){
		log.info("接收到的待校验的数据：{}"+JsonUtil.serialize(pendingProductDto));
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
							updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),"全部排除了");
						}else if("2".equals(skuHandleReuslt)){
							isSkuPass = false ;
							updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),"有无法处理的SKU，需要维护品牌品类尺码的对应关系");
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
					}else{
						updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),"无sku信息");
					}

					updatedVo.setSkus(skuMsgReturn);
					response.setErrorMsg(updatedVo);
				}
			}
			if(0==pendingProductDto.getSupplierSpuId()){
				pendingProductDto.setSupplierSpuId(null);
			}
			//颜色暂不处理 让审核人员 自己判断（20171127）
//            if(null != hubSpuDto&&pendingProductDto!=null){
//            	if(pendingProductDto.getHubColor()!=null&&pendingProductDto.getHubColor().equals(hubSpuDto.getHubColor())){
//
//            	}else{
//            		pass = false;
//            		pendingProductDto.setUpdateTime(new Date());
//            		pendingProductDto.setAuditState((byte)0);
//            		pendingProductDto.setMemo("再处理：同品牌同货号颜色不一样,hub颜色："+hubSpuDto.getHubColor());
//            		pendingProductDto.setAuditOpinion("再处理：同品牌同货号颜色不一样,hub颜色："+hubSpuDto.getHubColor());
//            		pendingProductDto.setAuditDate(new Date());
//            		pendingProductDto.setAuditUser("chenxu");
//            		PendingUpdatedVo updatedVo1 = new PendingUpdatedVo();
//            		updatedVo1.setSpuPendingId(pendingProductDto.getSpuPendingId());
//            		updatedVo1.setSpuResult("同品牌同货号颜色不一样,hub颜色："+hubSpuDto.getHubColor());
//            		response.setCode("1");
//                    response.setErrorMsg(updatedVo1);
//            	}
//            }

			if(pass &&isSkuPass&& null != hubSpuDto){
//            	  HubPendingDto hubPendingDto = new HubPendingDto();
//                hubPendingDto.setHubSpuId(hubSpuDto.getSpuId());
//                hubPendingDto.setHubSpuPendingId(pendingProductDto.getSpuPendingId());
//                pendingToHubGateWay.addSkuOrSkuSupplierMapping(hubPendingDto);
				pendingProductDto.setSpuState(SpuState.INFO_IMPECCABLE.getIndex());
			}else if(pass&&isSkuPass){
				pendingProductDto.setSpuState(SpuState.INFO_IMPECCABLE.getIndex());
			}
			pendingProductDto.setCreateTime(null);
			pendingProductDto.setPicState(null);
			log.info("更新参数："+JsonUtil.serialize(pendingProductDto));

			//摄影棚处理
			setHubSlotSpu(pendingProductDto);

			hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingProductDto);

			//如果处于审核成功 需要直接进入待选品 而不是待审核
			if(hubSpuDto!=null&&pendingProductDto.getHubColor().equals(hubSpuDto.getHubColor())){

				//审核前整体判断尺码状态
				skuPendingService.judgeSizeBeforeAudit(pendingProductDto);
				if(pendingProductDto.getSpuState()==SpuState.INFO_IMPECCABLE.getIndex()){
					String result = pendingProductCommonService.audit(pendingProductDto.getSpuPendingId());
//						SpuPendingAuditVO auditVO = this.getAuditProduct(pendingProductDto);
					if(null!=result){
						return HubResponse.errorResp(result);
					}
				}else{//审核状态发生变化 重新赋值
					hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingProductDto);
                    updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),pendingProductDto.getMemo());
                    response.setErrorMsg(updatedVo);

				}


			}
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

	private void setHubSlotSpu(PendingProductDto pendingProductDto) throws Exception {



		HubSpuPendingDto spuPendingDto = hubSpuPendingGateWay.selectByPrimaryKey(pendingProductDto.getSpuPendingId());
		if(null!=spuPendingDto){
			//获取是否是需要处理的供货商
			SupplierInHubDto supplier = supplierInHubService.getSupplierInHubBySupplierId(spuPendingDto.getSupplierId());
			if(supplier.isNeedShootSupplier()){

				pendingProductDto.setSupplierId(spuPendingDto.getSupplierId());
				pendingProductDto.setSupplierNo(spuPendingDto.getSupplierNo());
				pendingProductDto.setSupplierSpuId(spuPendingDto.getSupplierSpuId());
				//查询原始数据的状态
				if(null!=spuPendingDto.getSlotState()&&spuPendingDto.getSlotState()==SpuPendingStudioState.WAIT_HANDLED.getIndex().byteValue()) {
					if(supplier.isStudio()){//不需要寄送的 直接处理
						//第一次插入  检查修改后的数据状态
						handleSlotSpuAndSupplier(pendingProductDto);
					}else{
						//需要寄送的 需要判断图片及库存状态
						if(supplier.isNeedShootSupplier()){

							if(spuPendingDto.getPicState()==PicState.UNHANDLED.getIndex() &&spuPendingDto.getStockState()== StockState.HANDLED.getIndex()){
								handleSlotSpuAndSupplier(pendingProductDto);
							}
						}
					}

				}else if(null!=spuPendingDto.getSlotState()&&spuPendingDto.getSlotState()==SpuPendingStudioState.HANDLED.getIndex().byteValue()

						){
					//修改状态
					if(pendingProductDto.getSpuModelState()== SpuModelState.VERIFY_PASSED.getIndex()&&pendingProductDto.getSpuBrandState()== SpuBrandState.HANDLED.getIndex()) {
						if (pendingProductDto.getCatgoryState() == CatgoryState.PERFECT_MATCHED.getIndex() ||
								pendingProductDto.getCatgoryState() == CatgoryState.MISMATCHING.getIndex()) {
							slotSpuService.updateSlotSpu(pendingProductDto);
						}
					}
				}
			}

		}
	}



	@Override
	public HubResponse<List<PendingUpdatedVo>> batchUpdatePendingProduct(PendingProducts pendingProducts){
		HubResponse<List<PendingUpdatedVo>> response = new HubResponse<>();
		response.setCode("0"); //初始设置为成功
		if(null != pendingProducts && null != pendingProducts.getProduts() && pendingProducts.getProduts().size()>0){
			List<PendingUpdatedVo> updatedVos = new ArrayList<PendingUpdatedVo>();
			for(PendingProductDto pendingProductDto : pendingProducts.getProduts()){
				HubResponse<PendingUpdatedVo> everyResponse = updatePendingProduct(pendingProductDto);
				if("1".equals(everyResponse.getCode())){
					updatedVos.add(everyResponse.getErrorMsg());
				}
			}
			if(updatedVos.size() > 0){
				response.setCode("1");
				response.setErrorMsg(updatedVos);
			}
		}
		return response;
	}
	@Override
	public boolean updatePendingProductToUnableToProcess(String updateUser,String spuPendingId) throws Exception{
		try {
			if(!StringUtils.isEmpty(spuPendingId)){
				log.info("无法处理接口操作人=========="+updateUser);
				HubSpuPendingDto hubSpuPendingDto = new HubSpuPendingDto();
				hubSpuPendingDto.setSpuPendingId(Long.parseLong(spuPendingId));
				hubSpuPendingDto.setSpuState(SpuState.UNABLE_TO_PROCESS.getIndex());
				hubSpuPendingDto.setUpdateUser(updateUser);
				hubSpuPendingDto.setAuditState((AuditState.DISAGREE.getIndex()));
				hubSpuPendingGateWay.updateByPrimaryKeySelective(hubSpuPendingDto);
			}
			return true;
		} catch (Exception e) {
			log.error("单个产品更新无法处理时异常："+e.getMessage(),e);
			throw new Exception("单个产品更新无法处理时异常："+e.getMessage(),e);
		}

	}
	@Override
	public boolean batchUpdatePendingProductToUnableToProcess(String updateUser,List<String> spuPendingIds){
		try {
			if(null != spuPendingIds && spuPendingIds.size()>0){
				for(String spuPendingId : spuPendingIds){
					updatePendingProductToUnableToProcess(updateUser,spuPendingId);
				}
			}
			return true;
		} catch (Exception e) {
			log.error("批量更新无法处理时异常："+e.getMessage(),e);
			return false;
		}

	}
	@Override
	public SupplierProductVo findSupplierProduct(Long supplierSpuId) {
		long start = System.currentTimeMillis();
		SupplierProductVo supplierProductVo = new SupplierProductVo();
		try {
			HubSupplierSpuDto spuDto = hubSupplierSpuGateWay.selectByPrimaryKey(supplierSpuId);
			if(null != spuDto){
				JavaUtil.fatherToChild(spuDto,supplierProductVo);
				long start_sku = System.currentTimeMillis();
				List<HubSupplierSkuDto> supplierSku = findHubSupplierSku(supplierSpuId);
				log.info("--->原始信息查询sku耗时{}",System.currentTimeMillis()-start_sku);
				if(CollectionUtils.isNotEmpty(supplierSku)){
					supplierProductVo.setSupplierSku(supplierSku);
				}
				supplierProductVo.setUpdateTimeStr(null != spuDto.getUpdateTime() ? DateTimeUtil.getTime(spuDto.getUpdateTime()) : "");
				supplierProductVo.setCreatTimeStr(null != spuDto.getCreateTime() ? DateTimeUtil.getTime(spuDto.getCreateTime()) : "");
			}
		} catch (Exception e) {
			log.error("查询原始信息时异常："+e.getMessage(),e);
		}
		log.info("--->原始信息查询总耗时{}",System.currentTimeMillis()-start);
		return supplierProductVo;
	}
	@Override
	public boolean updateProductToInfoPeccable(String updateUser,List<String> ids){
		try {
			log.info("改变状态到待处理接口操作人=========="+updateUser);
			if(CollectionUtils.isNotEmpty(ids)){
				List<Long> list = new ArrayList<Long>();
				for(String id : ids){
					list.add(Long.valueOf(id));
				}
				HubSpuPendingDto hubSpuPendingDto = new HubSpuPendingDto();
				hubSpuPendingDto.setSpuState(SpuState.INFO_PECCABLE.getIndex());
				hubSpuPendingDto.setUpdateUser(updateUser);
				HubSpuPendingCriteriaDto whereCriteria = new HubSpuPendingCriteriaDto();
				whereCriteria.createCriteria().andSpuPendingIdIn(list);
				HubSpuPendingWithCriteriaDto criteria = new HubSpuPendingWithCriteriaDto();
				criteria.setHubSpuPending(hubSpuPendingDto);
				criteria.setCriteria(whereCriteria);
				hubSpuPendingGateWay.updateByCriteriaSelective(criteria);
				return true;
			}
		} catch (Exception e) {
			log.error("改变状态到待处理接口异常："+e.getMessage(),e);
		}
		return false;
	}

	@Override
	public int countByPendingQury(PendingQuryDto pendingQuryDto) {
		HubSpuPendingCriteriaDto criteriaDto = findhubSpuPendingCriteriaFromPendingQury(pendingQuryDto);
		return hubSpuPendingGateWay.countByCriteria(criteriaDto);
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


//	private SpuPendingAuditVO getAuditProduct(PendingProductDto pendingProductDto){
//		SpuPendingAuditVO auditVO = null;
//		if(pendingProductDto.getSpuState()== SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue()){
//			Long spuPendingId = pendingProductDto.getSpuPendingId();
//			HubSpuPendingDto spuPending = hubSpuPendingGateWay.selectByPrimaryKey(spuPendingId);
//			BeanUtils.copyProperties(spuPending, auditVO);
//			
//			auditVO = new SpuPendingAuditVO();
//			auditVO.setAuditUser(pendingProductDto.getUpdateUser());
//			auditVO.setAuditStatus((int)AuditState.AGREE.getIndex());
//			
//		}
//		return auditVO;
//
//	}

}
