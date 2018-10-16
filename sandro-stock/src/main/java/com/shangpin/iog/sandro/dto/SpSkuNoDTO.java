package com.shangpin.iog.sandro.dto;

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

    private String pcode;

    public SpSkuNoDTO(String spSkuNo, String pcode) {
        this.spSkuNo = spSkuNo;
        this.pcode = pcode;
    }

    public SpSkuNoDTO() {
    }
}
