package com.shangpin.ep.order.module.orderapiservice.impl.dto.baseblu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by lizhongren on 2017/2/9.
 */
public class OrderResult implements Serializable {
    @JsonIgnore
    private int  codMsg;
    @JsonIgnore
    private String msg;

    @JsonProperty("CodMsg")
    public int getCodMsg() {
        return codMsg;
    }
    @JsonProperty("CodMsg")
    public void setCodMsg(int codMsg) {
        codMsg = codMsg;
    }
    @JsonProperty("Msg")
    public String getMsg() {
        return msg;
    }
    @JsonProperty("Msg")
    public void setMsg(String msg) {
        msg = msg;
    }
}
