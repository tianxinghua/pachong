package com.shangpin.ep.order.conf.supplier;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 11:00 2018/4/19
 * @Description:
 */
@Setter
@Getter
@ToString
public class TheStyleSide extends SupplierCommon implements Serializable {

    private static final long serialVersionUID = 8029990782513087878L;

    private String usr;

    private String pwd;
}
