package com.shangpin.iog.forzieri.stock.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseToken {

     private String token;
     private int errorCode;
     private  String errorMessage;
}
