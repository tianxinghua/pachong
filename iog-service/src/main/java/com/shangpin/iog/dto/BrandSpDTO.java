package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by huxia on 2015/6/11.
 */
@Getter
@Setter
public class BrandSpDTO {
    private String brandId;
    private String brandName;

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
