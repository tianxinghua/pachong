package com.shangpin.ep.order.conf.supplier;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@Setter
@Getter
@ToString
public class Vietti2  extends SupplierCommon implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 913972800909461549L;
    /**
     *
     */
    private String url;
    /**
     *
     */
    private String createOrderInterface;
    /**
     *
     */
    private String setStatusInterface;
    /**
     *
     */
    private String getStatusInterface;
    /**
     *
     */
    private String getItemStockInterface;
    /**
     *
     */
    private String user;
    /**
     *
     */
    private String password;
    /**
     *
     */
    private String messageType;
}
