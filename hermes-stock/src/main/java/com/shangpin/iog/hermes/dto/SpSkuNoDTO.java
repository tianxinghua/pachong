package com.shangpin.iog.hermes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 13:54 2018/6/29
 * @Description:
 */
@Setter
@Getter
@ToString
public class SpSkuNoDTO {

    private String spSkuNo;

    private String qty;

    private String supplierSkuNo;

    public SpSkuNoDTO(String spSkuNo, String supplierSkuNo) {

        this.spSkuNo = spSkuNo;
        this.supplierSkuNo = supplierSkuNo;
    }

    public SpSkuNoDTO() {
    }
}
