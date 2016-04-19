package com.shangpin.iog.hottest.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Products {

	BaseProduct BaseProduct;
	List<BaseProductImage> BaseProductImage;
	List<BaseProductsBaseSupplier> BaseProductsBaseSupplier;
	List<BaseProductLookup> BaseProductLookup;
}
