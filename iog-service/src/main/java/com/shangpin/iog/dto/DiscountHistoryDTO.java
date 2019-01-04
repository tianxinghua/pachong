package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by huxia on 2015/9/6.
 */
@Getter
@Setter
public class DiscountHistoryDTO {
    private String id;
    private String supplierId;
    private String category;
    private String brand;
    private String discount;
    private Date createTime;

}
