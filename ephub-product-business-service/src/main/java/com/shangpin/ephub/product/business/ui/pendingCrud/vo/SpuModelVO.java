package com.shangpin.ephub.product.business.ui.pendingCrud.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by loyalty on 16/12/23.
 * 货号列表
 */
@Getter
@Setter
public class SpuModelVO implements Serializable {

   private String spuModel;//货号

   private String brandNo;
}
