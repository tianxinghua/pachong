package com.shangpin.ephub.product.business.service.hub.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lizhongren on 2017/2/14.
 */
@Setter
@Getter
public class SopSkuQueryDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5330004526971904061L;
	private String sopUserNo;
    private List<String> lstSupplierSkuNo;
}
