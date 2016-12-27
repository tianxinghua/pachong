package com.shangpin.ephub.product.business.ui.hub.all.vo;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title:HubProductDto </p>
 * <p>Description: hub详情页上的单个实体类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月22日 下午2:42:54
 *
 */
@Getter
@Setter
public class HubProductDetail extends HubSkuSupplierMappingDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6060605233668373417L;
	
	private Long skuId;//sku编号
	private String skuSize;//尺码
	private String supplierName;
	private String color;
	private String material;
	private String productOrigin;
	private String spuState;//商品状态

}
