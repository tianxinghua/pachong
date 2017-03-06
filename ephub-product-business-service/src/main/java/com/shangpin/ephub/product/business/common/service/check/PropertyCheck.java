package com.shangpin.ephub.product.business.common.service.check;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lizhongren on 2017/3/3.
 *所有的验证汇总 组合模式
 */
@Component
public class PropertyCheck extends CommonCheckBase {

    private  List<CommonCheckBase> allPropertyCheck ;

    public PropertyCheck(){}

    public PropertyCheck(List<CommonCheckBase> allPropertyCheck){
        this.allPropertyCheck = allPropertyCheck;
    }

    public List<CommonCheckBase> getAllPropertyCheck() {
        return allPropertyCheck;
    }

    public void setAllPropertyCheck(List<CommonCheckBase> allPropertyCheck) {
        this.allPropertyCheck = allPropertyCheck;
    }

    @Override
    protected String convertValue(HubSpuPendingDto spuPendingDto) {
          StringBuffer stringBuffer =new StringBuffer();
          for(CommonCheckBase base:allPropertyCheck){
              stringBuffer.append(base.convertValue(spuPendingDto)).append(",");
          }
          return stringBuffer.toString();
    }
}
