package com.shangpin.iog.sandro.dto;

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

}
