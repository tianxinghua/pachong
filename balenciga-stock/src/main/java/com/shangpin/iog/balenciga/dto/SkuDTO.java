package com.shangpin.iog.balenciga.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 16:53 2018/6/28
 * @Description:
 */

@Setter
@Getter
@ToString
public class SkuDTO {

    private String supplierSkuNo;
    private String spSkuNo;
    private String size;
    private String marketPrice; //市场价

    //自己添加给型号的库存信息
    private String qty;
    private String productUrl;

    //新的市场价
    private String newMarketPrice;


    public SkuDTO() {
    }

    public SkuDTO(String supplierSkuNo, String spSkuNo, String size, String marketPrice, String qty, String productUrl) {
        this.supplierSkuNo = supplierSkuNo;
        this.spSkuNo = spSkuNo;
        this.size = size;
        this.marketPrice = marketPrice;
        this.qty = qty;
        this.productUrl = productUrl;
    }
}
