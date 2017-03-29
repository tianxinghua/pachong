package com.shangpin.ephub.product.business.rest.scm.sop.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lizhongren on 2017/2/14.
 */
@Setter
@Getter
public class SopSkuQueryDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7460154031367111403L;
	private String sopUserNo;
    private List<String> lstSupplierSkuNo;
}
