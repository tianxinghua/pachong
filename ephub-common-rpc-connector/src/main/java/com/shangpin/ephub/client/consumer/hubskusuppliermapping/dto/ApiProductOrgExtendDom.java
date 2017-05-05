package com.shangpin.ephub.client.consumer.hubskusuppliermapping.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by lizhongren on 2016/12/30.
 */
@Setter
@Getter
@ToString
public   class ApiProductOrgExtendDom implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2216591715913051494L;
	private  String Heigth;
    /// <summary>
    /// 高-单位
    /// </summary>
    private  String  HeigthUnit;
    /// <summary>
    /// 上市年份
    /// </summary>
    private  String  MarketTime;
    /// <summary>
    /// 包装清单
    /// </summary>
    private  String  PackingList;
    /// <summary>
    /// 保质期
    /// </summary>
    private  String  ShelfLife;
    /// <summary>
    /// 宽
    /// </summary>
    private  String  Width;
    /// <summary>
    /// 宽-单位
    /// </summary>
    private  String  WidthUnit;
    /// <summary>
    /// 重量
    /// </summary>
    private  String  Weight;
    /// <summary>
    /// 重量-单位
    /// </summary>
    private  String  WeightUnit;
    /// <summary>
    /// 易碎品
    /// </summary>
    private  String  Fragile;
    /// <summary>
    /// 长
    /// </summary>
    private  String  Length;
    /// <summary>
    /// 长-单位
    /// </summary>
    private  String  LengthUnit;
    /// <summary>
    /// 可用周期
    /// </summary>
    private  String  UsefulTime;
    /// <summary>
    /// 上市季节
    /// </summary>
    private  String  MarketSeason;
    /// <summary>
    /// 航空运输
    /// </summary>
    private  String  AirTransport;

    private  long ProductOrgInfoId;
}
