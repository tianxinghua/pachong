package com.shangpin.iog.ebay.conf;

import lombok.Getter;
import lombok.Setter;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年7月15日
 */
@Setter
@Getter
public class EbayAppConf {

	private String tradeApi = null;
	private String devKey;
    private String appKey;
    private String cerKey;
    private String ruName;
    private String token;
    private String apiUrl;
    private String epsUrl;
    private String signUrl;
    private String shopingApi;
    private String findApi;
}
