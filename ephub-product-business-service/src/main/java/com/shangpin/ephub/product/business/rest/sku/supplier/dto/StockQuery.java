package com.shangpin.ephub.product.business.rest.sku.supplier.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>Title: PriceQueryDto</p>
 * <p>Description: 价格查询参数 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年3月31日 下午3:12:39
 *
 */
@Getter
@Setter
public class StockQuery {
	
	private Integer pageIndex;
    private Integer pageSize;
    
    /**
     * 供应商门户编号
     */
    private String supplierId;

    
}
