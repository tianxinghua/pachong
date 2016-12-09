package com.shangpin.ep.order.conf.supplier;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lizhongren on 2016/11/25.
 */
@Setter
@Getter
public class SupplierCommon implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7052399696735495387L;
	/**
     * 调用API的key
     */
    private String openApiKey;
    /**
     * 密码
     */
    private String openApiSecret;
    /**
     * 是否采购异常
     */
    private String  isPurchaseExp;
}
