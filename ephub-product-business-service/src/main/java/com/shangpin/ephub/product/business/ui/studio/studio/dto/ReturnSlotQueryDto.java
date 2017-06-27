package com.shangpin.ephub.product.business.ui.studio.studio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/27.
 */
@Getter
@Setter
@ToString
public class ReturnSlotQueryDto implements Serializable {
    private static final long serialVersionUID = 6762064994316701808L;
    private Long id;
    private Long supplierId;
    private String supplierUser;
    private int arriveState;
}
