package com.shangpin.iog.dior.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 16:04 2018/7/4
 * @Description:
 */
@Getter
@Setter
@ToString
public class ZhiCaiSkuHttpDTO {
    private String supplierId;
    private List<ZhiCaiSkuStock> zhiCaiSkuStockList;
}
