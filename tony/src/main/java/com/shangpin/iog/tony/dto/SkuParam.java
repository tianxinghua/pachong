package com.shangpin.iog.tony.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 15/9/19.
 */

public class SkuParam extends CommonParam {
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    String sku;
}
