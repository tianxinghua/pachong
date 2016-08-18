package com.shangpin.iog.product.special;

import java.util.List;

import com.shangpin.iog.dto.ProductDTO;

/**
 * 本类目前只用于BU导出那块，对需要特殊处理的供应商进行特殊处理<br>
 * 所有特殊处理的子类必须继承该类
 * @author lubaijiang
 *
 */
public abstract class Special {

	public abstract void screen(List<ProductDTO> productList);
}
