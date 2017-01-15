package com.shangpin.ephub.product.business.service.hub.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lizhongren on 2016/12/30.
 */
@Getter
@Setter
public class PlaceOrigin implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8489360827204734780L;
	private int PlaceOriginId;
    private String PlaceOriginValue;
}
