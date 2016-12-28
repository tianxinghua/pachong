package com.shangpin.ephub.product.business.common.util;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.common.ConstantProperty;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by loyalty on 16/12/20.
 */
@Component
@Slf4j
public class HubSpuUtil {

    @Autowired
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

    /**
     * 或许SPU下的SKU编号
     * @param spuNo  ：spu编号
     * @param total  ：需要增加几个值

     * @return
     */
    public String createHubSkuNo(String spuNo,int total){
        Long tmpSpuNo = 0L;
        String result = "";
        StringBuffer buffer = new StringBuffer();
        try {
            String tmpSku="";
            for(int i=0;i<total;i++){
                tmpSpuNo= shangpinRedis.incr(ConstantProperty.EP_HUB_SPU_NO_CREATE_KEY+"_"+ spuNo);
                tmpSku = "000" + String.valueOf(tmpSpuNo);
                tmpSku = tmpSku.substring(tmpSku.length()-3,tmpSku.length());
                buffer.append(tmpSku).append(",");
            }
            result = buffer.toString().substring(0,buffer.toString().length()-1);


        } catch (Exception e) {

            e.printStackTrace();

            log.error("redis 出错，无法获取sku编号");
            return  "";
            //TODO ADD EMAIL
        }

        return result;
    }
}
