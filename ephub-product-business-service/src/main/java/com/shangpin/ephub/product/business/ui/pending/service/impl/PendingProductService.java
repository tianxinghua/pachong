package com.shangpin.ephub.product.business.ui.pending.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.enumeration.FilterFlag;
import com.shangpin.ephub.client.data.mysql.enumeration.SkuState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.product.dto.HubPendingDto;
import com.shangpin.ephub.client.data.mysql.product.gateway.PengdingToHubGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.service.check.HubCheckService;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
import com.shangpin.ephub.product.business.rest.gms.dto.BrandDom;
import com.shangpin.ephub.product.business.rest.gms.dto.FourLevelCategory;
import com.shangpin.ephub.product.business.rest.gms.dto.SupplierDTO;
import com.shangpin.ephub.product.business.rest.hubpending.spu.service.HubPendingSpuCheckService;
import com.shangpin.ephub.product.business.rest.model.controller.HubBrandModelRuleController;
import com.shangpin.ephub.product.business.rest.model.dto.BrandModelDto;
import com.shangpin.ephub.product.business.rest.model.result.BrandModelResult;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.util.JavaUtil;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingSkuUpdatedVo;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingUpdatedVo;
import com.shangpin.ephub.product.business.ui.pending.vo.SupplierProductVo;
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
    private PengdingToHubGateWay pendingToHubGateWay;

    @Override
    public PendingProducts findPendingProducts(PendingQuryDto pendingQuryDto){
    	log.info("接收到的查询条件："+JsonUtil.serialize(pendingQuryDto)); 
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
                    for(HubSpuPendingDto pendingSpu : pendingSpus){
                    	spuPendingIds.add(pendingSpu.getSpuPendingId());
                    }
                    long start_sku = System.currentTimeMillis();
                    Map<Long,List<HubSkuPendingDto>> pendingSkus = findPendingSku(spuPendingIds);
                    log.info("--->待处理查询sku耗时{}",System.currentTimeMillis()-start_sku); 
                    for(HubSpuPendingDto pendingSpu : pendingSpus){
                        PendingProductDto pendingProduct = JavaUtil.convertHubSpuPendingDto2PendingProductDto(pendingSpu);
                        SupplierDTO supplierDTO = supplierService.getSupplier(pendingSpu.getSupplierNo());
                        pendingProduct.setSupplierName(null != supplierDTO ? supplierDTO.getSupplierName() : "");
                        FourLevelCategory category = categoryService.getGmsCateGory(pendingProduct.getHubCategoryNo());
                        pendingProduct.setHubCategoryName(null != category ? category.getFourthName() : pendingProduct.getHubCategoryNo());
                        BrandDom brand = brandService.getGmsBrand(pendingProduct.getHubBrandNo());
                        pendingProduct.setHubBrandName(null != brand ? brand.getBrandEnName() : pendingProduct.getHubBrandNo());
                        List<HubSkuPendingDto> skus = pendingSkus.get(pendingSpu.getSpuPendingId());
                        pendingProduct.setHubSkus(CollectionUtils.isNotEmpty(skus) ? skus : new ArrayList<HubSkuPendingDto>());
                        List<HubSpuPendingPicDto> picurls = findSpPicUrl(pendingSpu.getSupplierId(),pendingSpu.getSupplierSpuNo());
                        pendingProduct.setSpPicUrl(CollectionUtils.isNotEmpty(picurls) ? picurls.get(0).getSpPicUrl() : ""); 
                        pendingProduct.setPicUrls(findSpPicUrls(picurls)); 
                        pendingProduct.setSupplierUrls(findSupplierUrls(picurls)); 
                        pendingProduct.setPicReason(CollectionUtils.isNotEmpty(picurls) ? picurls.get(0).getMemo() : picReason); 
                        pendingProduct.setUpdateTimeStr(null != pendingSpu.getUpdateTime() ? DateTimeUtil.getTime(pendingSpu.getUpdateTime()) : "");
                        pendingProduct.setCreatTimeStr(null != pendingSpu.getCreateTime() ? DateTimeUtil.getTime(pendingSpu.getCreateTime()) : ""); 
                        pendingProduct.setAuditDateStr(null != pendingSpu.getAuditDate() ? DateTimeUtil.getTime(pendingSpu.getAuditDate()) : ""); 
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
    	log.info("接收到的待校验的数据：{}",pendingProductDto);
    	HubResponse<PendingUpdatedVo> response = new HubResponse<PendingUpdatedVo>();
    	response.setCode("0"); //初始设置为成功
    	PendingUpdatedVo updatedVo = null;
    	boolean pass = true; //全局用来判断整条数据是否校验通过
    	boolean isSkuPass = false;
    	HubSpuDto hubSpuDto = null;
    	try {
            if(null != pendingProductDto){
            	//开始校验spu
            	BrandModelResult brandModelResult = verifyProductModle(pendingProductDto);
            	if(brandModelResult.isPassing()){
            		hubSpuDto = findAndUpdatedFromHubSpu(brandModelResult.getBrandMode(),pendingProductDto);
            		if(null == hubSpuDto){
            			HubPendingSpuCheckResult spuResult = hubPendingSpuCheckService.checkHubPendingSpu(pendingProductDto);
            			if(spuResult.isPassing()){
            				pendingProductDto.setCatgoryState((byte)1);
            				pendingProductDto.setMaterialState((byte)1);
            				pendingProductDto.setOriginState((byte)1);
            				pendingProductDto.setSpuBrandState((byte)1);
            				pendingProductDto.setSpuColorState((byte)1);
            				pendingProductDto.setSpuGenderState((byte)1);
            				pendingProductDto.setSpuModelState((byte)1);
            				pendingProductDto.setSpuSeasonState((byte)1);
            			}else{
            				checkSpuState(pendingProductDto,spuResult);
            				pass = false ;
            				log.info("pending spu校验失败，不更新："+spuResult.getResult());
            				updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),spuResult.getResult());
            			}
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
            	List<PendingSkuUpdatedVo> skus = new ArrayList<PendingSkuUpdatedVo>();
            
            	if(pengdingSkus!=null&&pengdingSkus.size()>0){
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
                     		isSkuPass = true;
                     		hubSkuPendingDto.setHubSkuSizeType("尺寸"); 
                     		hubSkuPendingDto.setHubSkuSize(hubSkuSize.substring(hubSkuSize.indexOf(":")+1));
                     		if(null != hubSpuDto){
                             	hubSkuPendingDto.setSkuState(SpuState.HANDLING.getIndex());
                     		}else{
                     			hubSkuPendingDto.setSkuState(SpuState.INFO_IMPECCABLE.getIndex());	
                     		}
                     		hubSkuPendingDto.setFilterFlag(FilterFlag.EFFECTIVE.getIndex());
                     		hubSkuPendingDto.setSpSkuSizeState(SkuState.INFO_IMPECCABLE.getIndex());
                     	}else{
                     		isSkuPass = true;
                     		String [] arr = hubSkuSize.split(":",-1);
                     		String sizeType = null;
                     		String sizeValue = null;
                     		if(arr.length==2){
                     			sizeType = arr[0];
                     			sizeValue = arr[1];
                     		}else{
                     			sizeValue = hubSkuSize;
                     		}
                     		HubPendingSkuCheckResult result = hubCheckService.hubSizeExist(pendingProductDto.getHubCategoryNo(), pendingProductDto.getHubBrandNo(), sizeType,sizeValue);
         					if(result.isPassing()){
         						if(null != hubSpuDto){
         							hubSkuPendingDto.setScreenSize(result.getSizeId()); 
                                 	hubSkuPendingDto.setSkuState(SpuState.HANDLING.getIndex());
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
                     	}
                         hubSkuPendingGateWay.updateByPrimaryKeySelective(hubSkuPendingDto);
                     }
            		 
            		 if(!isSkuPass){
                      	updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),"尺码都被排除");
             		 }
            	}else{
            		updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),"无sku信息");
            	}
               
                updatedVo.setSkus(skus); 
                response.setErrorMsg(updatedVo);
            }
            if(0==pendingProductDto.getSupplierSpuId()){
                pendingProductDto.setSupplierSpuId(null);
            }
            if(pass &&isSkuPass&& null != hubSpuDto){
            	HubPendingDto hubPendingDto = new HubPendingDto();
                hubPendingDto.setHubSpuId(hubSpuDto.getSpuId());
                hubPendingDto.setHubSpuPendingId(pendingProductDto.getSpuPendingId());
                pendingToHubGateWay.addSkuOrSkuSupplierMapping(hubPendingDto);
                pendingProductDto.setSpuState(SpuState.HANDLED.getIndex());
            }else if(pass&&isSkuPass){
            	pendingProductDto.setSpuState(SpuState.INFO_IMPECCABLE.getIndex());
            }
            hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingProductDto);
        } catch (Exception e) {
            log.error("供应商："+pendingProductDto.getSupplierNo()+"产品："+pendingProductDto.getSpuPendingId()+"更新时发生异常："+e.getMessage());
            setErrorMsg(response,pendingProductDto.getSpuPendingId(),"服务器错误");
        }
    	log.info("返回的校验结果：+"+JsonUtil.serialize(response)); 
    	return response;
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
    	SupplierProductVo supplierProductVo = new SupplierProductVo();
    	try {
        	HubSupplierSpuDto spuDto = hubSupplierSpuGateWay.selectByPrimaryKey(supplierSpuId);
        	if(null != spuDto){
        		JavaUtil.fatherToChild(spuDto,supplierProductVo);
            	List<HubSupplierSkuDto> supplierSku = findHubSupplierSku(supplierSpuId);
            	if(CollectionUtils.isNotEmpty(supplierSku)){
            		supplierProductVo.setSupplierSku(supplierSku);
            	}
            	supplierProductVo.setUpdateTimeStr(null != spuDto.getUpdateTime() ? DateTimeUtil.getTime(spuDto.getUpdateTime()) : ""); 
            	supplierProductVo.setCreatTimeStr(null != spuDto.getCreateTime() ? DateTimeUtil.getTime(spuDto.getCreateTime()) : "");
        	}
		} catch (Exception e) {
			log.error("查询原始信息时异常："+e.getMessage(),e); 
		}
		return supplierProductVo;
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
    			spPicUrls.add(dto.getSpPicUrl());
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
    private PendingUpdatedVo setErrorMsg(HubResponse<PendingUpdatedVo> response,Long spuPengdingId,String errorMsg){
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
    private HubSpuDto findAndUpdatedFromHubSpu(String spuModel,PendingProductDto pendingProductDto){
    	pendingProductDto.setSpuModel(spuModel);
		List<HubSpuDto> hubSpus = selectHubSpu(pendingProductDto.getSpuModel(),pendingProductDto.getHubBrandNo());
		if(null != hubSpus && hubSpus.size()>0){
			convertHubSpuDtoToPendingSpu(hubSpus.get(0),pendingProductDto);
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
    private BrandModelResult verifyProductModle(PendingProductDto pendingProductDto) {
		BrandModelDto BrandModelDto = new BrandModelDto();
		BrandModelDto.setBrandMode(pendingProductDto.getSpuModel());
		BrandModelDto.setHubBrandNo(pendingProductDto.getHubBrandNo());
		BrandModelDto.setHubCategoryNo(pendingProductDto.getHubCategoryNo());
		BrandModelResult brandModelResult=  hubBrandModelRule.verify(BrandModelDto);
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
		hubPendingSpuDto.setHubColor(hubSpuDto.getHubColor());
		hubPendingSpuDto.setHubColorNo(hubSpuDto.getHubColorNo());
		hubPendingSpuDto.setHubGender(hubSpuDto.getGender());
		hubPendingSpuDto.setHubMaterial(hubSpuDto.getMaterial());
		hubPendingSpuDto.setHubOrigin(hubSpuDto.getOrigin());
		hubPendingSpuDto.setHubSeason(hubSpuDto.getSeason());
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
    private List<HubSpuDto> selectHubSpu(String spuModle,String hubBrandNo) {
		HubSpuCriteriaDto criteria = new HubSpuCriteriaDto();
		criteria.createCriteria().andSpuModelEqualTo(spuModle).andBrandNoEqualTo(hubBrandNo);
		return  hubSpuGateway.selectByCriteria(criteria);
	}

}
