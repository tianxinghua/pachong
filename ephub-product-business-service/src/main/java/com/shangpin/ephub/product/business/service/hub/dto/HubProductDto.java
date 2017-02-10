package com.shangpin.ephub.product.business.service.hub.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lizhongren on 2016/12/30.
 */
@Getter
@Setter
@ToString
public class HubProductDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5023771315363250619L;
	private SpProductOrgInfoEntity ProductOrgInfo;
    private ApiProductOrgExtendDom ProductOrgInfoExtend;

    private List<ApiSkuOrgDom> SkuList;


}
