package com.shangpin.ep.order.conf.supplier;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by lizhongren on 2017/2/8.
 */
@Setter
@Getter
@ToString
public class BaseBlu extends SupplierCommon implements Serializable {



    private String stockUrl;

    private String orderCreateUrl;

    private String orderQueryUrl;
    /**
     * 供货商分配给尚品的KEY
     */
    private String sKey;

}
