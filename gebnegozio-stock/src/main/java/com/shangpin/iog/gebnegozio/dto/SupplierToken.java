package com.shangpin.iog.gebnegozio.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhaowenjun on 2018/10/25.
 */
@Getter
@Setter
public class SupplierToken {
    private String refreshToken;
    private String accessToken;
    private String createTime;
    private String expireTime;
    private String supplierId;
}
