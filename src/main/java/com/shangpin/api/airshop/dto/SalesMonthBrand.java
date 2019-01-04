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
public class SalesMonthBrand implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @JSONField(name = "BrandName")
    private String brandName;
    @JSONField(name = "SalesAmount")
    private String salesAmount;
    @JSONField(name = "SalesQty")
    private String salesQty;
}
