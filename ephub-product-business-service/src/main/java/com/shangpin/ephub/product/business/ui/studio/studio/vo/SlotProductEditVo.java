package com.shangpin.ephub.product.business.ui.studio.studio.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2017/6/14.
 */
@Getter
@Setter
public class SlotProductEditVo {

    private String supplierId ;
    private String slotNo;
    private String errorCode;
    private String errorMsg;
    private SlotInfo slotInfo;

}
