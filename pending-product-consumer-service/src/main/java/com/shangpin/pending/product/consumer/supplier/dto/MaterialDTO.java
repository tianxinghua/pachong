package com.shangpin.pending.product.consumer.supplier.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by loyalty on 16/12/16.
 */
@Setter
@Getter
public class MaterialDTO implements Serializable {

    private Long materialItemId;

    private Long  materialDicId;

    private String supplierMaterial;

    private String hubMaterial;
}
