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
public class SalesMonthWeek implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @JSONField(name = "weeked")
    private String weeked;
    @JSONField(name = "yearMonth")
    private String yearMonth;
    @JSONField(name = "OnSpuNum")
    private String onSpuNum;
    @JSONField(name = "SaleSpuNum")
    private String saleSpuNum;

}
