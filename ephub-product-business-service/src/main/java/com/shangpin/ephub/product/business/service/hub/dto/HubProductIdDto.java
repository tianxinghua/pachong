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

    Long id;

    List<HubProductIdDto> subProduct;

}
