package com.shangpin.api.airshop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ReturnInOutBound {
	//验证码
    private String Message;

    //时间戳
    private String MessageCode;
    private String Successed;

}
