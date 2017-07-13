package com.shangpin.ephub.product.business.service.studio.studio.impl;

import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.client.data.studio.studio.gateway.StudioGateWay;
import com.shangpin.ephub.product.business.service.studio.studio.StudioService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by loyalty on 17/6/20.
 */
public class StudioServiceImpl implements StudioService {
    @Autowired
    StudioGateWay studioGateWay;

    @Override
    public boolean isOwnerStudio(String  supplierId) {
        StudioCriteriaDto criteria = new StudioCriteriaDto();
        criteria.createCriteria().andSupplierIdEqualTo(supplierId);
        List<StudioDto> studioDtos = studioGateWay.selectByCriteria(criteria);
        if(null!=studioDtos&&studioDtos.size()>0) return true;
        return false;
    }

    @Override
    public int getTimeLog(Long studioId) {
        StudioDto studioDto = studioGateWay.selectByPrimaryKey(studioId);
        if(null!=studioDto){
            if(null!=studioDto.getTimeLag()){
                return studioDto.getTimeLag();
            }
        }
        return 0;
    }


}
