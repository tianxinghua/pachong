package com.shangpin.ep.order.module.orderapiservice.impl.dto.forzieri;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OAuth {
	private String access_token;
	private int expires_in;
	private String token_type;
	private String scope;
	private String refresh_token;
	private String return_code;
	private String error_code;
}