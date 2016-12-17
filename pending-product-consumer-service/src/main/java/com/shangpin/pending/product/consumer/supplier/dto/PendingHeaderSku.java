package com.shangpin.pending.product.consumer.supplier.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by loyalty on 16/12/14.
 */
@Getter
@Setter
public class PendingHeaderSku implements Serializable {
    private String supplierId;

    private String skuNo;

    private Integer status;
}
