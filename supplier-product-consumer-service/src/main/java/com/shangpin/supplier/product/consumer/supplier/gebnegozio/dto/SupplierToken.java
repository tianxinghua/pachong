package com.shangpin.supplier.product.consumer.supplier.gebnegozio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Created by zhaowenjun on 2018/10/24.
 */
@Getter
@Setter
@ToString
public class SupplierToken {
    private String refreshToken;
    private String accessToken;
    private String createTime;
    private String expireTime;
    private String supplierId;
}
