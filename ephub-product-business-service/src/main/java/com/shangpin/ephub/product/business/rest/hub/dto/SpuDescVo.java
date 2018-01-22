package com.shangpin.ephub.product.business.rest.hub.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by lizhongren on 2017/12/21.
 */
@Getter
@Setter
@ToString
public class SpuDescVo implements Serializable {
    String spskuNo;//尚品skuno
    String descHtml;//描述
    String supplierNo;//
    String updateUser;//操作人
}
