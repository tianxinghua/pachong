package com.shangpin.ephub.product.business.service.pending.impl;

import com.shangpin.ephub.client.data.mysql.enumeration.CommonHandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.FilterFlag;
import com.shangpin.ephub.client.data.mysql.enumeration.SkuState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.product.business.common.enumeration.GlobalConstant;
import com.shangpin.ephub.product.business.common.enumeration.SpuStatus;
import com.shangpin.ephub.product.business.common.service.check.HubCheckService;
import com.shangpin.ephub.product.business.rest.size.dto.MatchSizeDto;
import com.shangpin.ephub.product.business.rest.size.service.MatchSizeService;
import com.shangpin.ephub.product.business.service.pending.SkuPendingService;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingSkuUpdatedVo;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingUpdatedVo;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhongren on 2017/11/23.
 */
@Service
@Slf4j
public class SkuPendingServiceImpl implements SkuPendingService {

    @Autowired
    HubSkuPendingGateWay skuPendingGateWay;

    @Autowired
    MatchSizeService matchSizeService;




    @Autowired
    private HubCheckService hubCheckService;

    @Override
    public boolean  setWaitHandleSkuPendingSize(Long spuPendingId,String hubBrand,String hubCategory) {
        boolean result  = true;
        List<HubSkuPendingDto> skuPendingDtos = this.getWaitHandleSkuPending(spuPendingId);
        if(null!=skuPendingDtos&&skuPendingDtos.size()>0){
            boolean isHandle = false;
            for(HubSkuPendingDto skuPendingDto:skuPendingDtos){
                if(null!=skuPendingDto.getSkuState()&&skuPendingDto.getSkuState().intValue()==SpuStatus.SPU_WAIT_AUDIT.getIndex()){
                    //如果存在待审核的 就认为可以处理
                    isHandle = true;
                    continue;
                }
                HubSkuPendingDto updateDto = new HubSkuPendingDto();
                updateDto.setSkuPendingId(skuPendingDto.getSkuPendingId());
                MatchSizeDto sizeDto = new MatchSizeDto();
                sizeDto.setSize(skuPendingDto.getHubSkuSize());
                sizeDto.setHubBrandNo(hubBrand);
                sizeDto.setHubCategoryNo(hubCategory);
                MatchSizeResult matchSizeResult = matchSizeService.matchSize(sizeDto);
                if(null!=matchSizeResult){

                    if(matchSizeResult.isPassing()){//通过
                        isHandle = true;
                        updateDto.setScreenSize(matchSizeResult.getSizeId());
                        updateDto.setHubSkuSizeType(matchSizeResult.getSizeType());
                        updateDto.setSkuState(SpuState.INFO_IMPECCABLE.getIndex());
                        updateDto.setSpSkuSizeState(SkuState.INFO_IMPECCABLE.getIndex());
                        updateDto.setFilterFlag(FilterFlag.EFFECTIVE.getIndex());
                        skuPendingGateWay.updateByPrimaryKeySelective(updateDto);
                    }else  if(matchSizeResult.isMultiSizeType()) {//多个匹配  失败 增加备注
                        updateDto.setMemo(matchSizeResult.getResult());
                        skuPendingGateWay.updateByPrimaryKeySelective(updateDto);
                    }else  if(matchSizeResult.isFilter()){//有模板没匹配上
                        isHandle = true;
                        updateDto.setSkuState(SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue());
                        updateDto.setHubSkuSizeType("排除");
                        updateDto.setFilterFlag(FilterFlag.INVALID.getIndex());
                        updateDto.setSkuState(SkuState.INFO_IMPECCABLE.getIndex());
                        skuPendingGateWay.updateByPrimaryKeySelective(updateDto);
                    }else {//不做处理


                    }


                }


            }
            if(!isHandle){
                result = false;
            }
        }else{
            result = false;
        }
        return result;


    }

    @Override
    public String  setWaitHandleSkuPendingSize(Long spuPendingId, String hubBrand, String hubCategory, List<HubSkuPendingDto> skuPendings, List<PendingSkuUpdatedVo> skuVOs) {
        String  result = "0";
        String hubSkuSize ="";
        boolean isHaveMapping = false,isHaveNoHandle = false;
        for(HubSkuPendingDto hubSkuPendingDto : skuPendings){


            hubSkuSize = hubSkuPendingDto.getHubSkuSize();
            hubSkuSize = StringUtils.isEmpty(hubSkuSize) ? "" : hubSkuSize;
            log.info("从页面接收到的尺码信息===="+hubSkuSize);
            if(StringUtils.isEmpty(hubSkuSize)) continue;
            if(hubSkuSize.startsWith("排除")){
                hubSkuPendingDto.setHubSkuSizeType("排除");
                hubSkuPendingDto.setHubSkuSize(null);//目的是不更新尺码值
                hubSkuPendingDto.setFilterFlag(FilterFlag.INVALID.getIndex());
                hubSkuPendingDto.setSkuState(SkuState.INFO_IMPECCABLE.getIndex());
            }else if(hubSkuSize.startsWith("尺寸")){
                isHaveMapping = true;
                hubSkuPendingDto.setHubSkuSizeType("尺寸");
                hubSkuPendingDto.setHubSkuSize(hubSkuSize.substring(hubSkuSize.indexOf(":")+1));
                hubSkuPendingDto.setSkuState(SpuState.INFO_IMPECCABLE.getIndex());
                hubSkuPendingDto.setFilterFlag(FilterFlag.EFFECTIVE.getIndex());
                hubSkuPendingDto.setSpSkuSizeState(SkuState.INFO_IMPECCABLE.getIndex());
            }else{
                if(hubSkuSize.contains(":")){//尺码已经手动设置 ，需要校验

                    String [] arr = hubSkuSize.split(":",-1);
                    String sizeType = null;
                    String sizeValue = null;
                    if(arr.length==2){
                        sizeType = arr[0];
                        sizeValue = arr[1];
                    }else{
                        sizeValue = hubSkuSize;
                    }
                    if(setSkuPendingSize(hubCategory,hubBrand, skuVOs, hubSkuPendingDto, hubSkuSize, sizeType, sizeValue)){
                        isHaveMapping =true;
                    }

                }else{
                    //原始信息  需要调用查询是否有匹配的赋值

                    MatchSizeDto sizeDto = new MatchSizeDto();
                    sizeDto.setSize(hubSkuSize);
                    sizeDto.setHubBrandNo(hubBrand);
                    sizeDto.setHubCategoryNo(hubCategory);
                    MatchSizeResult matchSizeResult = matchSizeService.matchSize(sizeDto);
                    if(null!=matchSizeResult){

                        if(matchSizeResult.isPassing()){//通过

                            hubSkuPendingDto.setScreenSize(matchSizeResult.getSizeId());
                            hubSkuPendingDto.setHubSkuSizeType(matchSizeResult.getSizeType());
                            hubSkuPendingDto.setSkuState(SpuState.INFO_IMPECCABLE.getIndex());
                            hubSkuPendingDto.setSpSkuSizeState(SkuState.INFO_IMPECCABLE.getIndex());
                            hubSkuPendingDto.setFilterFlag(FilterFlag.EFFECTIVE.getIndex());

                            isHaveMapping = true;

                        }else  if(matchSizeResult.isMultiSizeType()) {//多个匹配  失败 增加备注
                            log.info("sku pending 含有多个匹配，不更新："+matchSizeResult.getResult()+"|原始数据："+hubSkuSize);
                            isHaveNoHandle = true;
                            PendingSkuUpdatedVo skuUpdatedVo = new PendingSkuUpdatedVo();
                            skuUpdatedVo.setSkuPendingId(hubSkuPendingDto.getSkuPendingId());
                            skuUpdatedVo.setSkuResult(matchSizeResult.getResult());
                            skuVOs.add(skuUpdatedVo);
                            hubSkuPendingDto.setMemo(matchSizeResult.getResult());

                        }else  if(matchSizeResult.isFilter()){//有模板没匹配上

                            hubSkuPendingDto.setSkuState(SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue());
                            hubSkuPendingDto.setHubSkuSizeType("排除");
                            hubSkuPendingDto.setFilterFlag(FilterFlag.INVALID.getIndex());
                            hubSkuPendingDto.setSkuState(SkuState.INFO_IMPECCABLE.getIndex());

                        }else {//不做处理
                            isHaveNoHandle = true;
                            PendingSkuUpdatedVo skuUpdatedVo = new PendingSkuUpdatedVo();
                            skuUpdatedVo.setSkuPendingId(hubSkuPendingDto.getSkuPendingId());
                            skuUpdatedVo.setSkuResult("没有模板，无法匹配，不做处理");
                            skuVOs.add(skuUpdatedVo);
                        }


                    }


                }
            }
            hubSkuPendingDto.setSupplyPrice(null);
            hubSkuPendingDto.setMarketPrice(null);
            hubSkuPendingDto.setSalesPrice(null);
            hubSkuPendingDto.setSupplierBarcode(null);
            skuPendingGateWay.updateByPrimaryKeySelective(hubSkuPendingDto);
        }

        if(isHaveMapping){//有映射的
            if(isHaveNoHandle){//也有无法处理的 此时不能进入待审核
                result = "2";
            }
        }else{
            if(isHaveNoHandle){//也有无法处理的 此时不能进入待审核
                result = "2";
            }else {
                result = "1";
            }
        }
        return result;
    }



    private boolean setSkuPendingSize(String hubCategoryNo,String hubBrandNo,   List<PendingSkuUpdatedVo> skus, HubSkuPendingDto hubSkuPendingDto, String hubSkuSize, String sizeType, String sizeValue) {
        boolean pass =true;
        HubPendingSkuCheckResult result = hubCheckService.hubSizeExist(hubCategoryNo, hubBrandNo, sizeType,sizeValue);
        if(result.isPassing()){

                hubSkuPendingDto.setScreenSize(result.getSizeId());
                hubSkuPendingDto.setSkuState(SpuState.INFO_IMPECCABLE.getIndex());
                hubSkuPendingDto.setSpSkuSizeState(SkuState.INFO_IMPECCABLE.getIndex());
                hubSkuPendingDto.setFilterFlag(FilterFlag.EFFECTIVE.getIndex());

        }else{
            pass = false ;
            log.info("pending sku校验失败，不更新："+result.getMessage()+"|原始数据："+hubSkuSize);

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

    @Override
    public List<HubSkuPendingDto> getWaitHandleSkuPending(Long spuPendingId) {
        HubSkuPendingCriteriaDto dto = new   HubSkuPendingCriteriaDto();
        List<Byte> stateList = new ArrayList<>();
        //SKU与SPU的状态保持一致
        stateList.add(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());
        stateList.add(SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue());
        dto.createCriteria().andSpuPendingIdEqualTo(spuPendingId).andSkuStateIn(stateList)
              ;
        return  skuPendingGateWay.selectByCriteria(dto);
    }



    @Override
    public  void judgeSizeBeforeAudit( HubSpuPendingDto hubSpuPending) {

        //获取未完成的所有SKU数量
        HubSkuPendingCriteriaDto criteriaSku = new HubSkuPendingCriteriaDto();
        criteriaSku.createCriteria().andSpuPendingIdEqualTo(hubSpuPending.getSpuPendingId())
                .andSkuStateNotEqualTo(SpuStatus.SPU_HANDLED.getIndex().byteValue());
        criteriaSku.setPageNo(1);
        criteriaSku.setPageSize(1000);
        List<HubSkuPendingDto> hubSkuPendingDtos = skuPendingGateWay.selectByCriteria(criteriaSku);
        if(null!=hubSkuPendingDtos&&hubSkuPendingDtos.size()>0){
            int total = hubSkuPendingDtos.size();
            int excludeNum = 0;
            Map<String,String> sizeType = new HashMap<>();
            for(int i=0;i<total;i++){
                HubSkuPendingDto dto = hubSkuPendingDtos.get(i);
                if(GlobalConstant.HUB_SKE_SIZE_TYPE_EXCLUDE.equals(dto.getHubSkuSizeType())){
                    excludeNum++;
                }
                if(SpuStatus.SPU_WAIT_HANDLE.getIndex()==dto.getSkuState().intValue()){
                    hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());
                    hubSpuPending.setMemo("同品牌同货号的产品，尺码有未匹配的,整体不能审核通过");
                    return;
                }
                sizeType.put(dto.getHubSkuSizeType(),"");
            }
            if(total==excludeNum){//全部为排除
                hubSpuPending.setMemo("SPU下未审核成功的SKU全部为排除,不需要审核");
                hubSpuPending.setSpuState(SpuStatus.SPU_HANDLED.getIndex().byteValue());
                return;
            }
            if(sizeType.size()>1){
                if(sizeType.containsKey(GlobalConstant.REDIS_HUB_MEASURE_SIGN_KEY)){
                    hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());
                    hubSpuPending.setMemo("SKU尺码类型包含尺寸以及非尺寸,请重新处理");
                    hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());
                    return;
                }
            }
            
            hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue());

        }else{
            hubSpuPending.setMemo("SPU下没有可处理的SKU,不能审核");
            hubSpuPending.setSpuState(SpuStatus.SPU_HANDLED.getIndex().byteValue());
        }
    }







}
