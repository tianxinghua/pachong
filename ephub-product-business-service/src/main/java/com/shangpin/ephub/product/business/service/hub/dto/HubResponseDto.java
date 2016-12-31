package com.shangpin.ephub.product.business.service.hub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lizhongren on 2016/12/31.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubResponseDto<T> implements Serializable {
    private List<T> resDatas;
    private boolean isSuccess;
    private String resMsg;

    @JsonProperty("IsSuccess")
    public boolean getIsSuccess() {
        return isSuccess;
    }
    @JsonProperty("IsSuccess")
    public void setIsSuccess(boolean success) {
        isSuccess = success;
    }
    @JsonProperty("ResDatas")
    public List<T> getResDatas() {
        return resDatas;
    }

    @JsonProperty("ResDatas")
    public void setResDatas(List<T> resDatas) {
        resDatas = resDatas;
    }
    @JsonProperty("ResMsg")
    public String getResMsg() {
        return resMsg;
    }
    @JsonProperty("ResMsg")
    public void setResMsg(String resMsg) {
        resMsg = resMsg;
    }
}
