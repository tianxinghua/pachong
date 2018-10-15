package com.shangpin.supplier.product.consumer.supplier.vipgroup.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhaowenjun on 2018/10/13.
 */
@Getter
@Setter
public class TokenResp {
    private String token;
    private String errorCode;
    private String errorMessage;
}
