package com.shangpin.ephub.product.business.service.pending.impl;

import com.shangpin.ephub.client.data.mysql.enumeration.FilterFlag;
import com.shangpin.ephub.client.data.mysql.enumeration.SkuState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.product.business.common.enumeration.SpuStatus;
import com.shangpin.ephub.product.business.rest.size.dto.MatchSizeDto;
import com.shangpin.ephub.product.business.rest.size.service.MatchSizeService;
import com.shangpin.ephub.product.business.service.pending.SkuPendingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public boolean  setWaitHandleSkuPendingSize(Long spuPendingId,String hubBrand,String hubCategory) {
        boolean result  = true;
        List<HubSkuPendingDto> skuPendingDtos = this.getWaitHandleSkuPending(spuPendingId);
        if(null!=skuPendingDtos&&skuPendingDtos.size()>0){
            boolean isHandle = false;
            for(HubSkuPendingDto skuPendingDto:skuPendingDtos){
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
    public List<HubSkuPendingDto> getWaitHandleSkuPending(Long spuPendingId) {
        HubSkuPendingCriteriaDto dto = new   HubSkuPendingCriteriaDto();
        dto.createCriteria().andSpuPendingIdEqualTo(spuPendingId).andSkuStateEqualTo(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());//SKU与SPU的状态保持一致
        return  skuPendingGateWay.selectByCriteria(dto);
    }

}
