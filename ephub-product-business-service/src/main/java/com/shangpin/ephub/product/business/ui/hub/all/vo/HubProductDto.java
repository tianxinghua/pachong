package com.shangpin.ephub.product.business.ui.hub.all.vo;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title:HubProductDto </p>
 * <p>Description: hub页面返回单个实体类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月22日 下午2:42:54
 *
 */
@Getter
@Setter
public class HubProductDto extends HubSkuDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6060605233668373417L;
	private String supplierName;
	private String supplierProductModle;//供应商货号
	private String color;
	private String material;
	private String productOrigin;
	private String spuState;//商品状态

}
