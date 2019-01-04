package com.shangpin.api.airshop.dto.direct;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 18:54 2018/9/28
 * @Description:
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogisticsCompanyDto {

    /// <summary>
    /// 物流公司编号
    //@JSONField(name = "LogisticsCompanyNo")
    private String logisticsCompanyNo ;
    /// <summary>
    /// 物流公司名称
    //@JSONField(name = "LogisticsCompanyName")
    private String logisticsCompanyName ;
    /// <summary>
    /// 目前备用
    //@JSONField(name = "EstimateArrivedFlag")
    private byte estimateArrivedFlag ;


}
