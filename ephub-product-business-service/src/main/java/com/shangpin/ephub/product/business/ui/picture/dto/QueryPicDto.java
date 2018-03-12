package com.shangpin.ephub.product.business.ui.picture.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lizhongren on 2018/3/10.
 */
@Getter
@Setter
public class QueryPicDto implements Serializable {

    String spuNo;
    String supplierId;
    Long spuId;
}
