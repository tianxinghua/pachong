package com.shangpin.api.airshop.dto.direct;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 14:53 2018/10/10
 * @Description: 直发供应商物流段信息 DTO
 */
@Setter
@Getter
@ToString
public class SupplierSegments {
    
    /// 供应商编号
   // @JSONField(name = "SupplierNo")
    private String supplierNo;
    
    /// 是否国际段对接 0未对接，1已对接
    //@JSONField(name = "InternationalDocking")
    private byte internationalDocking;
    
    /// 是否国内段对接 0未对接，1已对接
    //@JSONField(name = "DomesticDocking")
    private byte domesticDocking;
    
    /// 供应商几段式合作 1一段式，2两段式
    //@JSONField(name = "Segments")
    private byte segments;

}
