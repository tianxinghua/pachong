package com.shangpin.ephub.product.business.service.hub.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lizhongren on 2016/12/30.
 */
@Getter
@Setter
public class HubProductDto implements Serializable {

    private SpProductOrgInfoEntity ProductOrgInfo;
    private ApiProductOrgExtendDom ProductOrgInfoExtend;

    private List<ApiSkuOrgDom> SkuList;


}
