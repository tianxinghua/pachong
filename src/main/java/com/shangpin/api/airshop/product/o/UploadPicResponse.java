package com.shangpin.api.airshop.product.o;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UploadPicResponse extends ProductResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3895055793422569516L;
	
	List<String> picUrls;
}
