package com.shangpin.api.airshop.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 销售日报表
 * Created by ZRS on 2016/1/28.
 */
@Getter
@Setter
public class SalesDay  implements Serializable {

    @JSONField(name = "TotalSaleAmount")
    private String totalSaleAmount;
    @JSONField(name = "OrderQty")
    private String orderQty;
    @JSONField(name = "ProductQty")
    private String productQty;
    @JSONField(name = "OutStockQty")
    private String outStockQty;
    @JSONField(name = "CancelQty")
    private String cancelQty;
    @JSONField(name = "DeliveryQty")
    private String deliveryQty;
    @JSONField(name = "AvgeAmount")
    private String avgeAmount;
    @JSONField(name = "DaySalesAmounts")
    private List<DaySalesAmounts> daySalesAmounts;

    //DaySalesAmounts汇总
    private List<String> X;
    private List<String> Y;

    //比率
    private String outStockQtyRate;
    private String cancelQtyRate;
    private String deliveryQtyRate;
    
    //报表步长和增长的基数
    private String scaleSteps;
    private String scaleStepWidth;

}
