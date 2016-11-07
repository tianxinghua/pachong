package com.shangpin.iog.lungolivigno.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseGetPrice {

	@JsonProperty("Result")
    private List<Result> Result;
    @JsonProperty("ErrMessage")
    private String ErrMessage;
    @JsonProperty("Status")
    private boolean Status;
    @JsonProperty("ErrCode")
    private int ErrCode;   
}
