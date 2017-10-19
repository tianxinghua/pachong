package com.shangpin.ep.order.conf.supplier;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: Bagheera</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年9月5日 下午4:57:55
 *
 */
@Getter
@Setter
public class Srl extends SupplierCommon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3228410686458791149L;
	
	private String salesUpdate;
	private String returnsUpdate;
	private String businessApi;

}
