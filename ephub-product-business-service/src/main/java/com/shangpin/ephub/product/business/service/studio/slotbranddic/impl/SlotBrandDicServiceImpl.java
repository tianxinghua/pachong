package com.shangpin.ephub.product.business.service.studio.slotbranddic.impl;

import com.shangpin.ephub.client.data.mysql.studio.dic.dto.HubDicStudioBrandCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.dic.dto.HubDicStudioBrandDto;
import com.shangpin.ephub.client.data.mysql.studio.dic.gateway.HubDicStudioBrandGateway;
import com.shangpin.ephub.product.business.service.studio.slotbranddic.SlotBrandDicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by loyalty on 17/6/19.
 *
 */
@Service
@Slf4j
public class SlotBrandDicServiceImpl implements SlotBrandDicService {

    @Autowired
    HubDicStudioBrandGateway studioBrandGateway;

    @Override
    public boolean isNeedShoot(String spBrandNo) {
        HubDicStudioBrandCriteriaDto criteria= new HubDicStudioBrandCriteriaDto();
        criteria.createCriteria().andSpBrandNoEqualTo(spBrandNo);
        List<HubDicStudioBrandDto> hubDicStudioBrandDtos = studioBrandGateway.selectByCriteria(criteria);
        if(null!=hubDicStudioBrandDtos&&hubDicStudioBrandDtos.size()>0){
            return true;
        }
        return false;
    }



}
