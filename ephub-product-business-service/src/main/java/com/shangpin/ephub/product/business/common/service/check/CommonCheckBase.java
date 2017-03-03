package com.shangpin.ephub.product.business.common.service.check;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import org.apache.commons.lang.StringUtils;

/**
 * Created by lizhongren on 2017/3/3.
 * 抽象类 总控的入口
 */
public abstract  class CommonCheckBase {

    private    boolean isNeedConvert(HubSpuPendingDto spuPendingDto) throws Exception{
        if(StringUtils.isNotBlank(spuPendingDto.getUpdateUser())){
            return false;
        }else{

            return true;
        }
    }

    /**
     * 赋值 总入口
     * @param spuPendingDto
     * @return
     * @throws Exception
     */
    public String  setPropertyValue(HubSpuPendingDto spuPendingDto) throws Exception {

        if(isNeedConvert(spuPendingDto)){
           return  convertValue(spuPendingDto);
        }else{
            return "";
        }


    }

    /**
     * 具体的转化
     * @param spuPendingDto
     * @return
     */
    protected abstract String convertValue(HubSpuPendingDto spuPendingDto);


}
