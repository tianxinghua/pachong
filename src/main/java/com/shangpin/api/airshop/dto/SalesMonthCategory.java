package com.shangpin.api.airshop.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by ZRS on 2016/1/30.
 */
@Getter
@Setter
public class SalesMonthCategory implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @JSONField(name = "SecondaryCategory")
    private String secondaryCategory;
    @JSONField(name = "SalesAmount")
    private String salesAmount;
    @JSONField(name = "SalesQty")
    private String salesQty;
    @JSONField(name = "BuyAmount")
    private String buyAmount;
    @JSONField(name = "SalesItems")
    private String salesItems;

    private String price;
    private String salesRate;

}
