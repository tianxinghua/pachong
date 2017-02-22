package com.shangpin.ephub.client.data.mongodb.product.supplier.gateway.dto;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * <p>Title: SupplierProduct</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年2月22日 下午3:30:50
 *
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SupplierProductDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2162871494223587295L;
	/**
	 * id
	 */
	private String id;
	/**
	 * 商品数据
	 */
	private Map<String,Object> product; 

}
