package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:InviqaConf.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午7:16:51
 */
@Setter
@Getter
@ToString
public class InviqaConf extends SupplierCommon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4404455785624827533L;
	/**
	 * 
	 */
	private String magentoApiKey;
	/**
	 * 
	 */
	private String magentoApiSecret;
	/**
	 * 
	 */
	private String magentoRestApiUrl;
	/**
	 * 
	 */
	private String token;
	/**
	 * 
	 */
	private String secret;

}
