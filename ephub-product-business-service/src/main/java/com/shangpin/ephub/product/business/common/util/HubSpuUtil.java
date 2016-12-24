package com.shangpin.ephub.product.business.common.util;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by loyalty on 16/12/20.
 */
@Component
@Slf4j
public class HubSpuUtil {

//    @Autowired
    IShangpinRedis shangpinRedis;



    /**
     * 创建SPU编号
     *
     * @return
     */
    public String createHubSpuNo(Long num){
        Long tmpSpuNo = 0L;
        try {
            if(num==0){
                 tmpSpuNo= shangpinRedis.incr(ConstantProperty.EP_HUB_SPU_NO_CREATE_KEY);

            }else{
                 tmpSpuNo= shangpinRedis.incrBy(ConstantProperty.EP_HUB_SPU_NO_CREATE_KEY,num);
            }
        } catch (Exception e) {

            e.printStackTrace();

            log.error("redis 出错，无法获取spu编号");
            return  "";
            //TODO ADD EMAIL
        }
        String result = "";
        result = "00000000" + String.valueOf(tmpSpuNo);
        result = "9"+result.substring(result.length()-9,result.length());


       return result;
    }
}
