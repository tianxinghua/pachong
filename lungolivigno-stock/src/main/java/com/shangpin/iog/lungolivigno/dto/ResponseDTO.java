/**
  * Copyright 2016 aTool.org 
  */
package com.shangpin.iog.lungolivigno.dto;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Auto-generated: 2016-05-30 18:33:24
 *
 */
@Setter
@Getter
public class ResponseDTO {

    @JsonProperty("Result")
    private List<Result> Result;
    @JsonProperty("ErrMessage")
    private String ErrMessage;
    @JsonProperty("Status")
    private boolean Status;
    @JsonProperty("ErrCode")
    private int ErrCode;

}