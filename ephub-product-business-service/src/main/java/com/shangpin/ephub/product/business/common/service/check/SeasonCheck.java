package com.shangpin.ephub.product.business.common.service.check;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import org.springframework.stereotype.Component;

/**
 * Created by lizhongren on 2017/3/3.
 * 单个具体的实现类
 */
@Component
public class SeasonCheck extends CommonCheckBase {



    @Override
    protected String convertValue(HubSpuPendingDto spuPendingDto) {
          return "";
    }
}
