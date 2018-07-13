package com.shangpin.ephub.product.business.service.pending;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lizhongren
 *
 *  通用服务
 *
 */
@Service
public class PendingCommonService {



    @Autowired
    HubSpuGateWay hubSpuGateway;


    public boolean handlePendingProduct(PendingSpu pendingSpu) throws Exception {
        return false;
    }


    /**
     * 根据品牌和货号查找hub_spu表中的记录
     * @param spuModle
     * @param hubBrandNo
     * @return
     */
    protected List<HubSpuDto> selectHubSpu(String spuModle, String hubBrandNo) {
        HubSpuCriteriaDto criteria = new HubSpuCriteriaDto();
        criteria.createCriteria().andSpuModelEqualTo(spuModle).andBrandNoEqualTo(hubBrandNo);
        return  hubSpuGateway.selectByCriteria(criteria);
    }
}
