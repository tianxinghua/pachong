package com.shangpin.ephub.product.business.service.hub;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lizhongren
 *
 *  通用服务
 *
 */
@Component
public class HubSpuCommonService {



    @Autowired
    HubSpuGateWay hubSpuGateway;
    /**
     * 根据品牌和货号查找hub_spu表中的记录
     * @param spuModle
     * @param hubBrandNo
     * @return
     */
    public List<HubSpuDto> selectHubSpu(String spuModle, String hubBrandNo) {
        HubSpuCriteriaDto criteria = new HubSpuCriteriaDto();
        criteria.createCriteria().andSpuModelEqualTo(spuModle).andBrandNoEqualTo(hubBrandNo);
        return  hubSpuGateway.selectByCriteria(criteria);
    }
}
