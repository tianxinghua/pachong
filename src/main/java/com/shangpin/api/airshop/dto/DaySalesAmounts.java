package com.shangpin.api.airshop.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by ZRS on 2016/1/28.
 */
@Getter
@Setter
public class DaySalesAmounts {

    @JSONField(name = "StatisticDate")
    private String statisticDate;
    @JSONField(name = "TotalDaySaleAmount")
    private String totalDaySaleAmount;
}
