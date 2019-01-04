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
public class SalesMonthPerformance implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @JSONField(name="yearMonth")
    private String yearMonth;
    @JSONField(name="SalesRanking")
    private String salesRanking;
    @JSONField(name="ValidItemsRanking")
    private String validItemsRanking;
    @JSONField(name="FulfilmentRateRanking")
    private String fulfilmentRateRanking;
    @JSONField(name="ReturnRateRanking")
    private String returnRateRanking;
    @JSONField(name="TotalSalesRanking")
    private String totalSalesRanking;
    @JSONField(name="TotalValidItemsRanking")
    private String totalValidItemsRanking;
    @JSONField(name="TotalFulfilmentRateRanking")
    private String totalFulfilmentRateRanking;
    @JSONField(name="TotalReturnRateRanking")
    private String totalReturnRateRanking;

    private String salesRate;
    private String validItemsRate;
    private String fulfilmentRate;
    private String returnRate;


}
