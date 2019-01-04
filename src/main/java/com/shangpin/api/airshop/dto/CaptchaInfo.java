package com.shangpin.api.airshop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 验证码
 * Created by ZRS on 2016/1/20.
 */
@Setter
@Getter
@ToString
public class CaptchaInfo  implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8055005637434250133L;

	//验证码
    private String code;

    //时间戳
    private long timestamp;


}
