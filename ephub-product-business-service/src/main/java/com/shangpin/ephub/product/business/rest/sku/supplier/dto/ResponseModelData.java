package com.shangpin.ephub.product.business.rest.sku.supplier.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lizhongren on 2018/4/9.
 */
@Getter
@Setter
public class ResponseModelData implements Serializable {

    private List<ZhiCaiModelResult> ZhiCaiResultList;
    private int total;
}
