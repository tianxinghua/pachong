package com.shangpin.ephub.product.business.rest.studio.studio.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;
/**
 * Created by wangchao on 2017/06/19.
 */
@ToString
public class ResultObjList {

	@JsonIgnore
    public String dT;
    @JsonIgnore
    public int isOffDay;
    
    @JsonProperty("DT")
    public String getDT() {
        return dT;
    }
    @JsonProperty("DT")
    public void setDT(String dT) {
        this.dT = dT;
    }
    @JsonProperty("IsOffDay")
    public int getIsOffDay() {
        return isOffDay;
    }
    @JsonProperty("IsOffDay") 
    public void setIsOffDay(int isOffDay) {
        this.isOffDay = isOffDay;
    }
    
}
