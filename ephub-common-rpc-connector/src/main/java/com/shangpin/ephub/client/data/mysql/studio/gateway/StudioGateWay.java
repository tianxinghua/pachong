package com.shangpin.ephub.client.data.mysql.studio.gateway;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.dto.StudioQueryDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Administrator on 2017/6/8.
 */
@FeignClient("ephub-data-mysql-service")
public interface StudioGateWay {

    @RequestMapping(value = "/studio/get-pending-product-list", method = RequestMethod.POST,consumes = "application/json")
    public int getPendingProductList(@RequestBody StudioQueryDto queryDto);
}
