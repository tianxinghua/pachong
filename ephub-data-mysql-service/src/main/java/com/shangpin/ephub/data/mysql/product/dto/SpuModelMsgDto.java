package com.shangpin.ephub.data.mysql.product.dto;

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
public class SpuModelMsgDto implements Serializable {

   private Integer total;//总数

   private List<SpuModelDto> spuModels;
}
