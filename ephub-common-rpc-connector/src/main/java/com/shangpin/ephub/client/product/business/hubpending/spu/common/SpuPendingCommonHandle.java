package com.shangpin.ephub.client.product.business.hubpending.spu.common;

import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by lizhongren on 2017/2/13.
 */
@Component
public class SpuPendingCommonHandle {
    /**
     * 判断关注的属性是否都已填写
     * @param spuPending
     */
     public void setSpuPendingInfoState(HubSpuPendingDto spuPending){
         if(StringUtils.isNotBlank(spuPending.getHubBrandNo())&&
                 StringUtils.isNotBlank(spuPending.getSpuModel())&&
                 StringUtils.isNotBlank(spuPending.getHubColor())&&
                 StringUtils.isNotBlank(spuPending.getHubCategoryNo())){
             //品牌/货号/颜色/材质 不为空就设置为1，其他为0
             spuPending.setInfoState(InfoState.PERFECT.getIndex());
         }else{
             spuPending.setInfoState(InfoState.IMPERFECT.getIndex());
         }
     }
}
