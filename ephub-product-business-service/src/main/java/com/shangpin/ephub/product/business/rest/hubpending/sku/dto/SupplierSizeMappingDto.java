package com.shangpin.ephub.product.business.rest.hubpending.sku.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by loyalty on 17/1/7.
 */
@Getter
@Setter
@ToString
public class SupplierSizeMappingDto implements Serializable {

    private String supplierSize;

    private String spSize;

    private String  spScreenSizeId;
}
