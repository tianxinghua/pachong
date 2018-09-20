package com.shangpin.ephub.product.business.service.pending;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.client.data.mysql.enumeration.SourceFromEnum;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.spu.dto.*;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;

import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingUpdatedVo;
import com.shangpin.ephub.response.HubResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by lizhongren
 *
 *  通用服务
 *
 */
@Service
public class PendingCommonService {

    @Autowired
    HubSpuPendingGateWay spuPendingGateWay;

    @Autowired
    private HubSupplierSpuGateWay hubSupplierSpuGateWay;


    public HubSpuPendingDto getHandleWebSpiderdSpuPending(String brandNo,String spuModel){
        HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
        criteria.createCriteria().andHubBrandNoEqualTo(brandNo).andSpuModelEqualTo(spuModel)
                .andSpuStateEqualTo(SpuState.HANDLED.getIndex()).andSourceFromEqualTo(SourceFromEnum.TYPE_WEBSPIDER.getIndex().byteValue());
        List<HubSpuPendingDto> hubSpuPendingDtos = spuPendingGateWay.selectByCriteria(criteria);
        if(null!=hubSpuPendingDtos&&hubSpuPendingDtos.size()>0) return hubSpuPendingDtos.get(0);
        return null;
    }


    public HubSupplierSpuDto getHubSupplierSpuPO(Long supplierSpuId){
        HubSupplierSpuCriteriaDto criteria = new HubSupplierSpuCriteriaDto();
        criteria.setFields("market_price , sale_price");
        criteria.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId);
        List<HubSupplierSpuDto> hubSupplierSpuDtos = hubSupplierSpuGateWay.selectByCriteria(criteria);
        if(null!=hubSupplierSpuDtos&&hubSupplierSpuDtos.size()>0) return hubSupplierSpuDtos.get(0);
        return null;
    }


    /**
     * 设置校验失败结果
     * @param response
     * @param spuPengdingId
     * @param errorMsg
     */
    protected PendingUpdatedVo setErrorMsg(HubResponse<PendingUpdatedVo> response, Long spuPengdingId, String errorMsg){
        response.setCode("1");
        PendingUpdatedVo updatedVo = new PendingUpdatedVo();
        updatedVo.setSpuPendingId(spuPengdingId);
        updatedVo.setSpuResult(errorMsg);
        response.setErrorMsg(updatedVo);
        return updatedVo;
    }


    /**
     * set hubSpuDto object value to   pendingProductDto object
     * @param hubSpuDto
     * @param hubPendingSpuDto
     */
    public  void convertHubSpuDtoToPendingSpu(HubSpuDto hubSpuDto, PendingProductDto hubPendingSpuDto){
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


    public  void convertWebSpiderSpuToPendingSpu( HubSpuPendingDto sourceDto, PendingProductDto targetPendingSpuDto){
        targetPendingSpuDto.setHubBrandNo(sourceDto.getHubBrandNo());
        targetPendingSpuDto.setHubCategoryNo(sourceDto.getHubCategoryNo());
        if(targetPendingSpuDto.getHubColor()!=null&&targetPendingSpuDto.getHubColor().equals(sourceDto.getHubColor())){
            targetPendingSpuDto.setHubColor(sourceDto.getHubColor());
        }
        targetPendingSpuDto.setHubColorNo(sourceDto.getHubColorNo());
        targetPendingSpuDto.setHubGender(sourceDto.getHubGender());
        targetPendingSpuDto.setHubMaterial(sourceDto.getHubMaterial());
        targetPendingSpuDto.setHubOrigin(sourceDto.getHubOrigin());
//		hubPendingSpuDto.setHubSeason(hubSpuDto.getMarketTime()+"_"+hubSpuDto.getSeason()); 季节可以修改 ，所以不赋值
        targetPendingSpuDto.setHubSpuNo(sourceDto.getHubSpuNo());
        targetPendingSpuDto.setSpuModel(sourceDto.getSpuModel());
        targetPendingSpuDto.setSpuName(sourceDto.getSpuName());
    }
    /**
     *
     * @param pendingProductDto
     */
    public  void setSpuPendingStateWhenHaveHubSpu(PendingProductDto pendingProductDto) {
        pendingProductDto.setCatgoryState((byte)1);
        pendingProductDto.setMaterialState((byte)1);
        pendingProductDto.setOriginState((byte)1);
        pendingProductDto.setSpuBrandState((byte)1);
        pendingProductDto.setSpuColorState((byte)1);
        pendingProductDto.setSpuGenderState((byte)1);
        pendingProductDto.setSpuModelState((byte)1);
        pendingProductDto.setSpuSeasonState((byte)1);

    }


    public  void checkSpuState(PendingProductDto hubPendingSpuDto, HubPendingSpuCheckResult hubPendingSpuCheckResult) {
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

    /**
     * 获取待处理的产品和库存
     * @param supplierId :供货尚ID 可以为空
     * @param page 页码
     * @param pageSize  每页的数量
     * @return
     */
     public PendingProducts getWaitHandleSpuPendingsWithPage(String  supplierId,Date startDate,Date endDate ,
                                                             Integer page, Integer pageSize){
         HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
         HubSpuPendingCriteriaDto.Criteria criterion = criteria.createCriteria();
         criterion.andSpuStateEqualTo(SpuState.INFO_PECCABLE.getIndex());
         if(StringUtils.isNotBlank(supplierId)){
             criterion.andSupplierIdEqualTo(supplierId);
         }
         if(null!=startDate){
             criterion.andCreateTimeGreaterThanOrEqualTo(startDate);
         }
         if(null!=endDate){
             criterion.andCreateTimeLessThan(endDate);
         }
         criteria.setPageNo(page);
         criteria.setPageSize(pageSize);
         List<HubSpuPendingDto> hubSpuPendingDtos = spuPendingGateWay.selectByCriteria(criteria);
         PendingProducts  pendingProducts = new PendingProducts();
         List<PendingProductDto> products = new ArrayList<>();
         if(null!=hubSpuPendingDtos&&hubSpuPendingDtos.size()>0){
              hubSpuPendingDtos.forEach(spuDto->{
                  PendingProductDto  spuPendingVO = new PendingProductDto();
                  BeanUtils.copyProperties(spuDto,spuPendingVO);
                  //获取需要处理的SKU

                  products.add(spuPendingVO);
              });
         }
         pendingProducts.setProduts(products);
         pendingProducts.setTotal(products.size());
         pendingProducts.setCreateUser("procedure");
         return pendingProducts;
     }
}
