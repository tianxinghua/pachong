package com.shangpin.iog.tony.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 15/9/19.
 */

public class CommonParam {

    private String merchantId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    private String token;
}
