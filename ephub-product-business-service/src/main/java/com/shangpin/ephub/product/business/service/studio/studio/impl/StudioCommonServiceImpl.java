package com.shangpin.ephub.product.business.service.studio.studio.impl;

import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.client.data.studio.studio.gateway.StudioGateWay;
import com.shangpin.ephub.product.business.service.studio.studio.StudioCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by loyalty on 17/6/20.
 */
@Service
public class StudioCommonServiceImpl implements StudioCommonService {
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

    @Override
    public String getTimeLogTime(Long studioId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String returnTime = format.format(new Date());
        StudioDto studioDto = studioGateWay.selectByPrimaryKey(studioId);
        if(null!=studioDto){
           if(null!=studioDto.getTimeLag()){
               Calendar calendar = Calendar.getInstance();
               calendar.add(Calendar.HOUR_OF_DAY,studioDto.getTimeLag()*-1);
               return  format.format(calendar.getTime());
           }
        }
        return  returnTime;

    }


}
