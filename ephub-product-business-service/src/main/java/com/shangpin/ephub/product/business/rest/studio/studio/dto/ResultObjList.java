package com.shangpin.ephub.product.business.rest.studio.studio.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;

@ToString
public class ResultObjList {

	@JsonIgnore
    public String dT;
    @JsonIgnore
    public int isOffDay;
    
    @JsonProperty("dT")
    public String getDT() {
        return dT;
    }
    @JsonProperty("dT")
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
