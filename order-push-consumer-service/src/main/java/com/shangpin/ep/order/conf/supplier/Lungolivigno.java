package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title:Lungolivigno </p>
 * <p>Description: 供应商lungolivigno的配置文件</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月13日 下午6:17:57
 *
 */
@Getter
@Setter
public class Lungolivigno extends SupplierCommon implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6044829789883687331L;
	
	private String url_login = null;
    private String url_saveOrder = null;
    private String user_name = null;
    private String user_password = null;
    private String url_cancelOrder = null;
    private String url_getStock = null;
}
