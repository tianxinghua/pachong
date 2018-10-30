package com.shangpin.iog.refreshtoken.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhaowenjun on 2018/10/25.
 */
@Getter
@Setter
public class TokenResp {
    private String code;
    private String data;
    private String message;
}
