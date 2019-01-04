package com.shangpin.api.airshop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 19:24 2018/10/18
 * @Description:
 */
@Setter
@Getter
@ToString
public class BrandDTO implements Serializable {

    private String brandNo ;
    private String brandName;

}
