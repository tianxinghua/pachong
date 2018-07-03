package com.shangpin.iog.gucci.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 16:48 2018/6/28
 * @Description:
 */
@Getter
@Setter
@ToString
public class ApiResponseBody {

    private String code;

    private String msg;

    private ShangPinPageContent content;

    private String errorMsg;

}
