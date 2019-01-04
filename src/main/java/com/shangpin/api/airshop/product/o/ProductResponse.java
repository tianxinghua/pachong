package com.shangpin.api.airshop.product.o;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -715232857668078757L;
	
	private Integer code;
	
	private String msg;

}
