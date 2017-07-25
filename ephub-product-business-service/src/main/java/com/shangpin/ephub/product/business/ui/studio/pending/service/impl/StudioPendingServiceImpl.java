package com.shangpin.ephub.product.business.ui.studio.pending.service.impl;

import com.shangpin.ephub.client.data.mysql.enumeration.*;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.product.dto.HubPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.service.check.CommonCheckBase;
import com.shangpin.ephub.product.business.common.service.check.HubCheckService;
import com.shangpin.ephub.product.business.common.service.check.PropertyCheck;
import com.shangpin.ephub.product.business.common.service.check.property.BrandCheck;
import com.shangpin.ephub.product.business.common.service.check.property.CategoryCheck;
import com.shangpin.ephub.product.business.common.service.check.property.SpuModelCheck;
import com.shangpin.ephub.product.business.rest.gms.dto.BrandDom;
import com.shangpin.ephub.product.business.rest.gms.dto.FourLevelCategory;
import com.shangpin.ephub.product.business.rest.gms.dto.SupplierDTO;
import com.shangpin.ephub.product.business.rest.gms.service.BrandService;
import com.shangpin.ephub.product.business.rest.gms.service.CategoryService;
import com.shangpin.ephub.product.business.rest.gms.service.SupplierService;
import com.shangpin.ephub.product.business.rest.hubpending.spu.service.HubPendingSpuCheckService;
import com.shangpin.ephub.product.business.rest.model.controller.HubBrandModelRuleController;
import com.shangpin.ephub.product.business.rest.model.result.BrandModelResult;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.enumeration.ProductState;
import com.shangpin.ephub.product.business.ui.pending.service.impl.PendingProductService;
import com.shangpin.ephub.product.business.ui.pending.util.JavaUtil;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingSkuUpdatedVo;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingUpdatedVo;
import com.shangpin.ephub.product.business.ui.studio.pending.service.StudioPendingService;
import com.shangpin.ephub.product.business.utils.time.DateTimeUtil;

import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 17/6/8.
 */
@Service
@Slf4j
public class StudioPendingServiceImpl extends PendingProductService implements StudioPendingService {

    @Autowired
    HubSpuPendingGateWay hubSpuPendingGateWay;

    @Autowired
    PropertyCheck propertyCheck;

    @Autowired
    CategoryCheck categoryCheck;

    @Autowired
    SpuModelCheck spuModelCheck;

    @Autowired
    BrandCheck brandCheck;

    @Autowired
    HubSlotSpuService hubSlotSpuService;


    @Override
    public PendingProducts findPendingProducts(PendingQuryDto pendingQuryDto, boolean flag) {
        log.info("接收到的查询条件："+ JsonUtil.serialize(pendingQuryDto));
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
                    log.info("--->待处理查询sku耗时{}",System.currentTimeMillis()-start_sku);
                    for(HubSpuPendingDto pendingSpu : pendingSpus){
                        PendingProductDto pendingProduct = JavaUtil.convertHubSpuPendingDto2PendingProductDto(pendingSpu);
                        SupplierDTO supplierDTO = supplierService.getSupplier(pendingSpu.getSupplierNo());
                        pendingProduct.setSupplierName(null != supplierDTO ? supplierDTO.getSupplierName() : "");
                        FourLevelCategory category = categoryService.getGmsCateGory(pendingProduct.getHubCategoryNo());
                        pendingProduct.setHubCategoryName(null != category ? category.getFourthName() : pendingProduct.getHubCategoryNo());
                        BrandDom brand = brandService.getGmsBrand(pendingProduct.getHubBrandNo());
                        pendingProduct.setHubBrandName(null != brand ? brand.getBrandEnName() : pendingProduct.getHubBrandNo());


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
    public HubResponse<PendingUpdatedVo> updatePendingProduct(PendingProductDto pendingDto){

        PendingProductDto pendingProductDto = new PendingProductDto();
        initPendingDto(pendingDto,pendingProductDto);

        log.info("接收到的待校验的数据：{}"+JsonUtil.serialize(pendingProductDto));
        HubResponse<PendingUpdatedVo> response = new HubResponse<PendingUpdatedVo>();
        response.setCode("0"); //初始设置为成功
        PendingUpdatedVo updatedVo = null;
        boolean pass = true; //全局用来判断整条数据是否校验通过
        boolean isSkuPass = false;
        HubSpuDto hubSpuDto = null;
        try {
            if(null != pendingProductDto){
                //开始校验spu

                //先校验品类  单独处理

                if(!isSpCategoryNo(pendingProductDto.getHubCategoryNo())){
                    log.info("pending spu校验失败，不更新："+ "品类必须是尚品的品类");
                    updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),"品类必须是尚品的品类");
                    response.setErrorMsg(updatedVo);
                    return response;
                }

                BrandModelResult brandModelResult = verifyProductModle(pendingProductDto);
                if(brandModelResult.isPassing()){



                    HubPendingSpuCheckResult spuResult = this.checkHubPendingSpu(pendingProductDto);
                    if(spuResult.isPassing()){
//                        pendingProductDto.setCatgoryState(CatgoryState.PERFECT_MATCHED.getIndex());
                        pendingProductDto.setSpuBrandState(SpuBrandState.HANDLED.getIndex());
                        pendingProductDto.setSpuModelState(SpuModelState.VERIFY_PASSED.getIndex());
                    }else{
                        checkSpuState(pendingProductDto,spuResult);
                        pass = false ;
                        log.info("pending spu校验失败，不更新："+spuResult.getResult());
                        updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),spuResult.getResult());
                    }




                }else{
                    pass = false ;
                    log.info("pending spu校验失败，不更新：货号校验不通过。");
                    updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),"货号校验不通过");
                }

                if(null!=updatedVo){
                    response.setCode("1");
                    response.setErrorMsg(updatedVo);
                    return response;
                }


            }
            if(null!=pendingProductDto.getSupplierSpuId()&&0==pendingProductDto.getSupplierSpuId()){
                pendingProductDto.setSupplierSpuId(null);
            }

            pendingProductDto.setCreateTime(null);

            hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingProductDto);

            hubSlotSpuService.addSlotSpuAndSupplier(pendingProductDto);

        } catch (Exception e) {
            log.error("供应商："+pendingProductDto.getSupplierNo()+"产品："+pendingProductDto.getSpuPendingId()+"更新时发生异常："+e.getMessage());
            updatedVo =  setErrorMsg(response,pendingProductDto.getSpuPendingId(),"服务器错误");
            response.setErrorMsg(updatedVo);
        }
        log.info("返回的校验结果：+"+JsonUtil.serialize(response));
        return response;
    }


    private  boolean isSpCategoryNo(String hubCategoryNo){
        boolean result = true;
        if(StringUtils.isNotBlank(hubCategoryNo)){

            if(hubCategoryNo.matches("A[0-9]{2}B[0-9]{2}C[0-9]{2}D[0-9]{2}")){

            }else if(hubCategoryNo.matches("A[0-9]{2}B[0-9]{2}C[0-9]{2}")){

            }else if(hubCategoryNo.matches("A[0-9]{2}B[0-9]{2}")){

            }else if(hubCategoryNo.matches("A[0-9]{2}")){

            }else{
                result =  false;
            }
        }
        return result;
    }


    @Override
    public HubResponse<List<PendingUpdatedVo>> batchUpdatePendingProduct(PendingProducts pendingProducts){
        HubResponse<List<PendingUpdatedVo>> response = new HubResponse<>();
        response.setCode("0"); //初始设置为成功
        if(null != pendingProducts && null != pendingProducts.getProduts() && pendingProducts.getProduts().size()>0){
            List<PendingUpdatedVo> updatedVos = new ArrayList<PendingUpdatedVo>();
            for(PendingProductDto pendingProductDto : pendingProducts.getProduts()){
                HubResponse<PendingUpdatedVo> everyResponse = this.updatePendingProduct(pendingProductDto);
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

    private void checkSpuState(PendingProductDto hubPendingSpuDto, HubPendingSpuCheckResult hubPendingSpuCheckResult) {
        if(hubPendingSpuCheckResult.isSpuModel()){
            hubPendingSpuDto.setSpuModelState(SpuModelState.VERIFY_PASSED.getIndex());
        }else{
            hubPendingSpuDto.setSpuModelState(SpuModelState.VERIFY_FAILED.getIndex());
        }

//        if(hubPendingSpuCheckResult.isCategory()){
//            hubPendingSpuDto.setCatgoryState((byte)1);
//        }else{
//            hubPendingSpuDto.setCatgoryState((byte)0);
//        }





        if(hubPendingSpuCheckResult.isBrand()){
            hubPendingSpuDto.setSpuBrandState(SpuBrandState.HANDLED.getIndex());
        }else{
            hubPendingSpuDto.setSpuBrandState(SpuBrandState.UNHANDLED.getIndex());
        }


    }

    private HubPendingSpuCheckResult  checkHubPendingSpu(PendingProductDto pendingDto){

        HubPendingSpuCheckResult result = new HubPendingSpuCheckResult();
        result.setPassing(true);

        List<CommonCheckBase> allPropertyCheck = new ArrayList<>();
        allPropertyCheck.add(brandCheck);
//        allPropertyCheck.add(categoryCheck);
//        allPropertyCheck.add(spuModelCheck);

        propertyCheck.setAllPropertyCheck(allPropertyCheck);
        HubSpuPendingDto spuPendingDto = hubSpuPendingGateWay.selectByPrimaryKey(pendingDto.getSpuPendingId());
        try {
            String  checkResult  = propertyCheck.handleConvertOrCheck(spuPendingDto,pendingDto);
            if(StringUtils.isNotBlank(checkResult)){
                result.setPassing(false);
                result.setResult(checkResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;

    }

    private void  initPendingDto(PendingProductDto resourceDto,PendingProductDto targetDto){
        if(null!=resourceDto.getSpuPendingId()){
            HubSpuPendingDto hubSpuPendingDto = hubSpuPendingGateWay.selectByPrimaryKey(resourceDto.getSpuPendingId());
            targetDto.setSupplierSpuId(hubSpuPendingDto.getSupplierSpuId());
        }


        targetDto.setSpuPendingId(resourceDto.getSpuPendingId());
        targetDto.setSpuModel(resourceDto.getSpuModel());
        targetDto.setHubBrandNo(resourceDto.getHubBrandNo());
        targetDto.setHubCategoryNo(resourceDto.getHubCategoryNo());
        targetDto.setSupplierNo(resourceDto.getSupplierNo());
        targetDto.setSupplierId(resourceDto.getSupplierId());
        targetDto.setUpdateUser(resourceDto.getUpdateUser());

    }


}
