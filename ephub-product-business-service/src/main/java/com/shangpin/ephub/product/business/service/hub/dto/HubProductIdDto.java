package com.shangpin.ephub.product.business.service.hub.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lizhongren on 2016/12/30.
 * 循环套用
 */
@Setter
@Getter
public class HubProductIdDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8668507034516041167L;

	Long id;

    List<HubProductIdDto> subProduct;

}
