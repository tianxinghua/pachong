package com.shangpin.iog.hermes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 15:52 2018/7/4
 * @Description:
 */
@Getter
@Setter
@ToString
public class ZhiCaiSkuStock {

    private String spSkuNo;
    private Integer qty;

    public ZhiCaiSkuStock() {
    }

    public ZhiCaiSkuStock(String spSkuNo, Integer qty) {
        this.spSkuNo = spSkuNo;
        this.qty = qty;
    }
}
