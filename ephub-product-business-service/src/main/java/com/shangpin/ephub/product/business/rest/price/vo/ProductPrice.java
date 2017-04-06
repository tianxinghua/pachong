package com.shangpin.ephub.product.business.rest.price.vo;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.price.unionselect.result.HubSupplierPrice;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title: ProductPrice</p>
 * <p>Description: </p>
 * <p>Company:  </p> 
 * @author lubaijiang
 * @date 2017年3月31日 下午3:18:36
 *
 */
@Getter
@Setter
public class ProductPrice {

	private Integer total;
	private List<HubSupplierPrice> productPriceList;
}
