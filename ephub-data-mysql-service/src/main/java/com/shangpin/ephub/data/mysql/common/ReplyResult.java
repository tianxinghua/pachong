package com.shangpin.ephub.data.mysql.common;

import com.alibaba.fastjson.JSONObject;

public class ReplyResult {
    private int code;
    private String message;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void success(){
        this.code=200;
        this.message="";
    }

    public void fail(){
        this.code=404;
        this.message="";
    }

    public void fail(int code,String message){
        this.code=code;
        this.message=message;
    }

}
