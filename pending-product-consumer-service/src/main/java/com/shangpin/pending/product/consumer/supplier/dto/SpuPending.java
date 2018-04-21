package com.shangpin.pending.product.consumer.supplier.dto;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 17/1/3.
 */
@SuppressWarnings("serial")
@Getter
@Setter
public class SpuPending extends HubSpuPendingDto {

    private String hubSpuNo;//hub spu 编号
    /**
     * 图片临时状态
     */
    private boolean  isHavePic;
    
    //待处理和已选品是否同品牌同货号同颜色
    private boolean hubSpuIsPassing;
}
