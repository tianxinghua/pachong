package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by huxia on 2015/9/10.
 */
@Getter
@Setter
public class OrderDTO {
    private BigInteger id;
    private String supplierId;
    private String uuId;  //和供货商公用的订单唯一标识
    private String spOrderId;
    private String detail;//
    private String status;//订单状态
    private Date createTime;
    private Date updateTime;
}
