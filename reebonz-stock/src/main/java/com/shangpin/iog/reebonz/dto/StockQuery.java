package com.shangpin.iog.reebonz.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockQuery {
	
	private Integer pageIndex;
    private Integer pageSize;
    
    /**
     * 供应商门户编号
     */
    private String supplierId;

    
}
