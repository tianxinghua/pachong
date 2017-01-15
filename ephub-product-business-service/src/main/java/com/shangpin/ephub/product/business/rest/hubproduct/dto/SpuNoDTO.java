package com.shangpin.ephub.product.business.rest.hubproduct.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by loyalty on 16/12/23.
 */
@Getter
@Setter
public class SpuNoDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7242646103263845320L;
	private String spuNo;
    /**
     * 如果多个 以"," 号分割 for example : 90001,90002
     */
    private String skuNo;
}
