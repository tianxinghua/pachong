package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;
import sun.security.util.BigInt;

import java.util.Date;

/**
 * Created by huxia on 2015/9/10.
 */
@Getter
@Setter
public class OrderDTO {
    private BigInt id;
    private String supplierId;
    private String uuId;
    private String spOrderId;
    private String detail;
    private Date createTime;
}
