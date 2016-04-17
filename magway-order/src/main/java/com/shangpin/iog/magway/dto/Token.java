package com.shangpin.iog.magway.dto;

import lombok.Getter;

import lombok.Setter;

@Setter
@Getter
public class Token {

	String access_token;
	String token_type;
	String expires_in;
}
