package com.shangpin.iog.gucci.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 16:50 2018/6/28
 * @Description:
 */
@Getter
@Setter
@ToString
public class ShangPinPageContent {

    //总记录数
    private Integer total;

    //直采商品数据集合
    private List<ProductDTO> zhiCaiResultList;


}
