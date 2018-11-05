package com.shangpin.ephub.price.consumer.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
@Getter
@Setter
public class PriceParamDTO implements Serializable {
    /// 供应商门户编号
    public long SopUserNo;
    /// Sku编号
    public String SkuNo;
    /// 市场价
    public BigDecimal MarketPrice;
    /// 原始消息编号
    public String FormNo;
    /// 备注
    public String Memo;
    /// 市场价币种，无则为空
    public String Currency;
    /// 年份
    public String MarketYear;
    /// 季节
    public String MarketSeason ;
    /// 渠道名称，无则为空
    public String ChannelName;
    /// 特价，无则填零
    public String SpecialMarketPrice;



}
