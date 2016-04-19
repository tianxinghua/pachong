package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by lizhongren on 2015/9/2.
 */
@Getter
@Setter
public class DiscountDTO {
    private String supplierId;
    private String category;
    private String brand;
    private String discount;
    private Date createTime;
    private Date updateTime;

}
