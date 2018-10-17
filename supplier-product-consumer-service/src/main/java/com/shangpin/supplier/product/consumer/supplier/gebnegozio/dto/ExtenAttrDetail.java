package com.shangpin.supplier.product.consumer.supplier.gebnegozio.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/10/17.
 */
@Getter
@Setter
public class ExtenAttrDetail {
    private List<ConfigurableProductOptions> configurable_product_options;
    private List<Integer> configurable_product_links;
}
