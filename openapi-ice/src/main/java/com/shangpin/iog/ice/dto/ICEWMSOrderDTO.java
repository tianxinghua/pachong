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
    private String SupplierNo;//供货商编号
    private String SkuNo;  //尚品sku编号
    private int ChangeForOrderQuantity;
    private String CreateTime;


    @Override
    public String toString() {
        return "ICEWMSOrderDTO{" +
                "FormNo='" + FormNo + '\'' +
                ", SupplierNo='" + SupplierNo + '\'' +
                ", SkuNo='" + SkuNo + '\'' +
                ", ChangeForOrderQuantity=" + ChangeForOrderQuantity +
                ", CreateTime='" + CreateTime + '\'' +
                '}';
    }
}
