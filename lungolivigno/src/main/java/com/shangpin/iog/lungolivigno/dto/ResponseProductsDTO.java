/**
  * Copyright 2016 aTool.org 
  */
package com.shangpin.iog.lungolivigno.dto;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Auto-generated: 2016-05-30 11:14:41
 *
 */
@Getter
@Setter
public class ResponseProductsDTO {

    @JsonProperty("Result")
    private List<Result> Result;
    @JsonProperty("ErrMessage")
    private String ErrMessage;
    @JsonProperty("Status")
    private boolean Status;
    @JsonProperty("ErrCode")
    private int ErrCode;   

}