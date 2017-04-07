package com.shangpin.ephub.product.business.rest.price.dto;

import java.util.List;

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
public class PriceQuery {
	
	private Integer pageIndex;
    private Integer pageSize;
    
    /**
     * 供应商门户编号
     */
    private String supplierId;
    /**
     * 尚品季节名称
     */
    private String marketSeason;
    /**
     * 尚品上市时间
     */
    private String marketYear;
    /**
     * 尚品skuid
     */
    private List<String> spSkuIds;
    
}
