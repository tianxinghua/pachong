package com.shangpin.ep.order.conf.supplier;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by zhaowenjun on 2018/9/12.
 */
@Setter
@Getter
@ToString
public class Gebnegozio extends SupplierCommon implements Serializable{
    private static final long serialVersionUID = -7820506502925304338L;
    private String url;
    private String tokenUrl;
    private String supplierId;
}
