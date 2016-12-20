package com.shangpin.ephub.product.business.common.util;

import com.shangpin.commons.redis.IShangpinRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by loyalty on 16/12/20.
 */
@Component
public class HubSpuUtil {

    @Autowired
    IShangpinRedis shangpinRedis;

    /**
     * 创建SPU编号
     * @return
     */
    public String createHubSpuNo(Long num){
        if(num==0){

            Long tmpSpuNo= shangpinRedis.incr(ConstantProperty.EP_HUB_SPU_NO_CREATE_KEY);

        }else{
            Long tmpSpuNo= shangpinRedis.incrBy(ConstantProperty.EP_HUB_SPU_NO_CREATE_KEY,num);
        }


       return "";
    }
}
