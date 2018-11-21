package com.shangpin.iog.gucci.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 16:47 2018/6/28
 * @Description:
 */
@Setter
@Getter
@ToString
public class ProductDTO {

    private String productUrl;

    private String supplierSpuNo;

    private String supplierSpuModel;

    private List<SkuDTO> zhiCaiSkuResultList;


}
