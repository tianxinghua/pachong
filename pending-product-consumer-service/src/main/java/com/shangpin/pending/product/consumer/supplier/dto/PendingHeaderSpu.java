package com.shangpin.pending.product.consumer.supplier.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 16/12/14.
 */
@Getter
@Setter
public class PendingHeaderSpu implements Serializable {
    private String supplierId;

    private String spuNo;

    private int status;

    private List<PendingHeaderSku> skus;
}
