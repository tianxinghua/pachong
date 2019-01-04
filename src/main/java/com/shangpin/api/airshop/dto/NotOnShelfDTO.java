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
public class NotOnShelfDTO  implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8055005637434250133L;
    private String brandName;
    private String spuName;
    private String spuModel;
    private String supplierSpuNo;
    private String errorReason;
}
