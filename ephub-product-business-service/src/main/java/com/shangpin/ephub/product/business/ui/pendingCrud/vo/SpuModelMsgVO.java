package com.shangpin.ephub.product.business.ui.pendingCrud.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by loyalty on 16/12/23.
 * 返回的货号信息
 */
@Getter
@Setter
public class SpuModelMsgVO implements Serializable {

   private Integer total;//总数

   private List<SpuModelVO> spuModels;
}
