package com.shangpin.ephub.product.business.ui.studio.pending.service.impl;

import com.shangpin.ephub.client.data.mysql.enumeration.*;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
import com.shangpin.ephub.product.business.rest.gms.dto.BrandDom;
import com.shangpin.ephub.product.business.rest.gms.dto.FourLevelCategory;
import com.shangpin.ephub.product.business.rest.gms.dto.SupplierDTO;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.enumeration.ProductState;
import com.shangpin.ephub.product.business.ui.pending.util.JavaUtil;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;
import com.shangpin.ephub.product.business.ui.studio.pending.service.StudioPendingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 17/6/8.
 */
@Service
@Slf4j
public class StudioPendingServiceImpl implements StudioPendingService {

    @Autowired
    HubSpuPendingGateWay hubSpuPendingGateWay;


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
//                    Map<Long,List<HubSkuPendingDto>> pendingSkus = findPendingSku(spuPendingIds,flag);
//                    log.info("--->待处理查询sku耗时{}",System.currentTimeMillis()-start_sku);
//                    for(HubSpuPendingDto pendingSpu : pendingSpus){
//                        PendingProductDto pendingProduct = JavaUtil.convertHubSpuPendingDto2PendingProductDto(pendingSpu);
//                        SupplierDTO supplierDTO = supplierService.getSupplier(pendingSpu.getSupplierNo());
//                        pendingProduct.setSupplierName(null != supplierDTO ? supplierDTO.getSupplierName() : "");
//                        FourLevelCategory category = categoryService.getGmsCateGory(pendingProduct.getHubCategoryNo());
//                        pendingProduct.setHubCategoryName(null != category ? category.getFourthName() : pendingProduct.getHubCategoryNo());
//                        BrandDom brand = brandService.getGmsBrand(pendingProduct.getHubBrandNo());
//                        pendingProduct.setHubBrandName(null != brand ? brand.getBrandEnName() : pendingProduct.getHubBrandNo());
//                        List<HubSkuPendingDto> skus = pendingSkus.get(pendingSpu.getSpuPendingId());
//                        pendingProduct.setHubSkus(CollectionUtils.isNotEmpty(skus) ? skus : new ArrayList<HubSkuPendingDto>());
//                        List<HubSpuPendingPicDto> picurls = hubSpuPendingPicService.findSpPicUrl(pendingSpu.getSupplierId(),pendingSpu.getSupplierSpuNo());
//                        pendingProduct.setSpPicUrl(findMainUrl(picurls));
//                        pendingProduct.setPicUrls(findSpPicUrls(picurls));
//                        pendingProduct.setSupplierUrls(findSupplierUrls(picurls));
//                        pendingProduct.setPicReason(CollectionUtils.isNotEmpty(picurls) ? picurls.get(0).getMemo() : picReason);
//                        pendingProduct.setUpdateTimeStr(null != pendingSpu.getUpdateTime() ? DateTimeUtil.getTime(pendingSpu.getUpdateTime()) : "");
//                        pendingProduct.setCreatTimeStr(null != pendingSpu.getCreateTime() ? DateTimeUtil.getTime(pendingSpu.getCreateTime()) : "");
//                        pendingProduct.setAuditDateStr(null != pendingSpu.getAuditDate() ? DateTimeUtil.getTime(pendingSpu.getAuditDate()) : "");
//                        products.add(pendingProduct);
//                    }
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



    /**
     * 将UI查询条件转换成数据库查询条件对象
     * @param pendingQuryDto UI查询条件对象
     * @return
     */
    protected HubSpuPendingCriteriaDto findhubSpuPendingCriteriaFromPendingQury(PendingQuryDto pendingQuryDto){
        HubSpuPendingCriteriaDto hubSpuPendingCriteriaDto = new HubSpuPendingCriteriaDto();
        if(null != pendingQuryDto.getSpuPendingId()){
            hubSpuPendingCriteriaDto.createCriteria().andSpuPendingIdEqualTo(pendingQuryDto.getSpuPendingId());
            return hubSpuPendingCriteriaDto;
        }
        hubSpuPendingCriteriaDto.setOrderByClause("update_time desc");
        if(!StringUtils.isEmpty(pendingQuryDto.getPageIndex()) && !StringUtils.isEmpty(pendingQuryDto.getPageSize())){
            hubSpuPendingCriteriaDto.setPageNo(pendingQuryDto.getPageIndex());
            hubSpuPendingCriteriaDto.setPageSize(pendingQuryDto.getPageSize());
        }
        List<Integer> inconformities = pendingQuryDto.getInconformities();
        if(CollectionUtils.isNotEmpty(inconformities)){
            for (int i=0;i<inconformities.size();i++) {
                HubSpuPendingCriteriaDto.Criteria criteria = getCriteria(pendingQuryDto, hubSpuPendingCriteriaDto);
                if (ProductState.SPU_GENDER_STATE.getIndex() == inconformities.get(i)) {
                    criteria.andSpuGenderStateEqualTo(SpuGenderState.UNHANDLED.getIndex());
                } else if (ProductState.SPU_BRAND_STATE.getIndex() == inconformities.get(i)) {
                    criteria.andSpuBrandStateEqualTo(SpuBrandState.UNHANDLED.getIndex());
                } else if(ProductState.CATGORY_STATE.getIndex() == inconformities.get(i)){
                    criteria.andCatgoryStateNotEqualTo(CatgoryState.PERFECT_MATCHED.getIndex());
                } else if(ProductState.SPU_MODEL_STATE.getIndex() == inconformities.get(i)){
                    criteria.andSpuModelStateEqualTo(SpuModelState.VERIFY_FAILED.getIndex());
                } else if(ProductState.MATERIAL_STATE.getIndex() == inconformities.get(i)){
                    criteria.andMaterialStateEqualTo(MaterialState.UNHANDLED.getIndex());
                } else if(ProductState.SPU_COLOR_STATE.getIndex() == inconformities.get(i)){
                    criteria.andSpuColorStateEqualTo(SpuColorState.UNHANDLED.getIndex());
                } else if(ProductState.ORIGIN_STATE.getIndex() == inconformities.get(i)){
                    criteria.andOriginStateEqualTo(OriginState.UNHANDLED.getIndex());
                } else if(ProductState.SPU_SEASON_STATE.getIndex() == inconformities.get(i)){
                    criteria.andSpuSeasonStateEqualTo(SpuSeasonState.UNHANDLED.getIndex());
                } else if(ProductState.SIZE_STATE.getIndex() == inconformities.get(i)){
                    criteria.andSpSkuSizeStateEqualTo(SpSkuSizeState.UNHANDLED.getIndex());
                } else if(ProductState.HAVEOPERATOR.getIndex() == inconformities.get(i)){
                    criteria.andUpdateUserIsNull();
                }
                if(i != 0){
                    hubSpuPendingCriteriaDto.or(criteria);
                }
            }
            return hubSpuPendingCriteriaDto;
        }else{
            getCriteria(pendingQuryDto, hubSpuPendingCriteriaDto);
            return hubSpuPendingCriteriaDto;
        }
    }


    private HubSpuPendingCriteriaDto.Criteria getCriteria(PendingQuryDto pendingQuryDto, HubSpuPendingCriteriaDto hubSpuPendingCriteriaDto) {
        HubSpuPendingCriteriaDto.Criteria criteria = hubSpuPendingCriteriaDto.createCriteria();
        if(StringUtils.isEmpty(pendingQuryDto.getSpuState()) || "0".equals(pendingQuryDto.getSpuState())){
            criteria.andSpuStateEqualTo(SpuState.INFO_PECCABLE.getIndex());
        }else if("1".equals(pendingQuryDto.getSpuState())){
            criteria.andSpuStateEqualTo(SpuState.INFO_IMPECCABLE.getIndex());
        }else if("2".equals(pendingQuryDto.getSpuState())){
            criteria.andSpuStateEqualTo(SpuState.HANDLED.getIndex());
        }else if("3".equals(pendingQuryDto.getSpuState())){
            criteria.andSpuStateEqualTo(SpuState.UNABLE_TO_PROCESS.getIndex());
        }else if("4".equals(pendingQuryDto.getSpuState())){
            criteria.andSpuStateEqualTo(SpuState.FILTER.getIndex());
        }else if("5".equals(pendingQuryDto.getSpuState())){
            criteria.andSpuStateEqualTo(SpuState.HANDLING.getIndex());
        }else if("6".equals(pendingQuryDto.getSpuState())){
            criteria.andSpuStateEqualTo(SpuState.EXISTED_IN_HUB.getIndex());
        }else if("16".equals(pendingQuryDto.getSpuState())){
//			criteria.andSpuStateEqualTo(SpuState.ALL_EXISTED_IN_HUB.getIndex());
        }
        if("0".equals(pendingQuryDto.getAuditState())){
            //再处理
            criteria.andAuditStateEqualTo(AuditState.DISAGREE.getIndex());
            if(!StringUtils.isEmpty(pendingQuryDto.getOperator())){
                criteria.andAuditUserLike(pendingQuryDto.getOperator()+"%");
            }
        }else if("1".equals(pendingQuryDto.getAuditState())){
            criteria.andAuditStateEqualTo(AuditState.AGREE.getIndex());
        }else{
            //待处理
            if(!StringUtils.isEmpty(pendingQuryDto.getOperator())){
                criteria.andUpdateUserLike(pendingQuryDto.getOperator()+"%");
            }
        }
        if(!StringUtils.isEmpty(pendingQuryDto.getSupplierNo())){
            criteria.andSupplierNoEqualTo(pendingQuryDto.getSupplierNo());
        }
        if(!StringUtils.isEmpty(pendingQuryDto.getSpuModel())){
            criteria.andSpuModelLike("%"+pendingQuryDto.getSpuModel()+"%");
        }
        String hubCategoryNo = pendingQuryDto.getHubCategoryNo();
        if(!StringUtils.isEmpty(hubCategoryNo)){
            if(hubCategoryNo.length() < 12){
                criteria.andHubCategoryNoLike(hubCategoryNo+"%");
            }else{
                criteria.andHubCategoryNoEqualTo(hubCategoryNo);
            }
        }
        if(!StringUtils.isEmpty(pendingQuryDto.getHubBrandNo())){
            criteria.andHubBrandNoEqualTo(pendingQuryDto.getHubBrandNo());
        }
        if(!StringUtils.isEmpty(pendingQuryDto.getHubSeason())){
            criteria.andHubSeasonLike("%"+pendingQuryDto.getHubSeason()+"%");
        }
        if(!StringUtils.isEmpty(pendingQuryDto.getHubYear())){
            criteria.andHubSeasonLike(pendingQuryDto.getHubYear()+"%");
        }
//        if(!StringUtils.isEmpty(pendingQuryDto.getStatTime())){
//            Date startTime = DateTimeUtil.convertFormat(pendingQuryDto.getStatTime(), dateFormat);
//            criteria.andUpdateTimeGreaterThanOrEqualTo(startTime);
//        }
//        if(!StringUtils.isEmpty(pendingQuryDto.getEndTime())){
//            Date endTime = DateTimeUtil.convertFormat(pendingQuryDto.getEndTime(),dateFormat);
//            criteria.andUpdateTimeLessThan(endTime);
//        }
//        if(!StringUtils.isEmpty(pendingQuryDto.getCreateTimeStart())){
//            Date startTime = DateTimeUtil.convertFormat(pendingQuryDto.getCreateTimeStart(), dateFormat);
//            criteria.andCreateTimeGreaterThanOrEqualTo(startTime);
//        }
//        if(!StringUtils.isEmpty(pendingQuryDto.getCreateTimeEnd())){
//            Date endTime = DateTimeUtil.convertFormat(pendingQuryDto.getCreateTimeEnd(),dateFormat);
//            criteria.andCreateTimeLessThan(endTime);
//        }
        if(!StringUtils.isEmpty(pendingQuryDto.getBrandName())){
            criteria.andHubBrandNoLike("%"+pendingQuryDto.getBrandName()+"%");
        }
        if(!StringUtils.isEmpty(pendingQuryDto.getCategoryName())){
            criteria.andHubCategoryNoLike("%"+pendingQuryDto.getCategoryName()+"%");
        }
        if(-1 != pendingQuryDto.getPicState()){
            if(0 == pendingQuryDto.getPicState()){
                criteria.andPicStateEqualTo(PicState.UNHANDLED.getIndex());
            }else if(1 == pendingQuryDto.getPicState()){
                criteria.andPicStateEqualTo(PicState.HANDLE_ERROR.getIndex());
            }else if(2 == pendingQuryDto.getPicState()){
                criteria.andPicStateEqualTo(PicState.HANDLED.getIndex());
            }
        }
        List<Integer> conformities = pendingQuryDto.getConformities();
        if(CollectionUtils.isNotEmpty(conformities)){
            for(int i = 0;i<conformities.size();i++){
                if (ProductState.SPU_GENDER_STATE.getIndex() == conformities.get(i)) {
                    criteria.andSpuGenderStateEqualTo(SpuGenderState.HANDLED.getIndex());
                } else if (ProductState.SPU_BRAND_STATE.getIndex() == conformities.get(i)) {
                    criteria.andSpuBrandStateEqualTo(SpuBrandState.HANDLED.getIndex());
                } else if(ProductState.CATGORY_STATE.getIndex() == conformities.get(i)){
                    criteria.andCatgoryStateEqualTo(CatgoryState.PERFECT_MATCHED.getIndex());
                } else if(ProductState.SPU_MODEL_STATE.getIndex() == conformities.get(i)){
                    criteria.andSpuModelStateEqualTo(SpuModelState.VERIFY_PASSED.getIndex());
                } else if(ProductState.MATERIAL_STATE.getIndex() == conformities.get(i)){
                    criteria.andMaterialStateEqualTo(MaterialState.HANDLED.getIndex());
                } else if(ProductState.SPU_COLOR_STATE.getIndex() == conformities.get(i)){
                    criteria.andSpuColorStateEqualTo(SpuColorState.HANDLED.getIndex());
                } else if(ProductState.ORIGIN_STATE.getIndex() == conformities.get(i)){
                    criteria.andOriginStateEqualTo(OriginState.HANDLED.getIndex());
                } else if(ProductState.SPU_SEASON_STATE.getIndex() == conformities.get(i)){
                    criteria.andSpuSeasonStateEqualTo(SpuSeasonState.HANDLED.getIndex());
                } else if(ProductState.SIZE_STATE.getIndex() == conformities.get(i)){
                    criteria.andSpSkuSizeStateEqualTo(SpSkuSizeState.HANDLED.getIndex());
                }else if(ProductState.INFOCOMPLETE.getIndex() == conformities.get(i)){
                    criteria.andInfoStateEqualTo(InfoState.PERFECT.getIndex());
                }else if(ProductState.HAVESTOCK.getIndex() == conformities.get(i)){
                    criteria.andStockStateEqualTo(StockState.HANDLED.getIndex());
                }else if(ProductState.HAVEOPERATOR.getIndex() == conformities.get(i)){
                    criteria.andUpdateUserIsNotNull();
                }
            }
        }
        return criteria;
    }
}
