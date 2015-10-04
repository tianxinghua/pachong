package com.shangpin.iog.ice.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lizhongren on 2015/10/3.
 * 订单返回
 */
@Getter
@Setter
public class ICEWMSOrderDTO {

    private String  FormNo;//订单编号
    private String SupplierNo;
    private String SkuNo;
    private int ChangeForOrderQuantity;
    private String CreateTime;


}
