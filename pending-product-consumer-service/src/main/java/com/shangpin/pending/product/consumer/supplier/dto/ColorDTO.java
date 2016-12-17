package com.shangpin.pending.product.consumer.supplier.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 16/12/16.
 */
@Setter
@Getter
public class ColorDTO {

    private Long colorItemId;//表colorItem 主键

    private Long colorDicId; //表colorDic 主键

    private String supplierColor;

    private String hubColorNo;//EPHUB 的颜色编号

    private String hubColorName;
}
