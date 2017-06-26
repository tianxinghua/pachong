package com.shangpin.ephub.product.business.common.dto;

import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.GET;
import java.io.Serializable;

/**
 * Created by loyalty on 17/6/23.
 */
@Getter
@Setter
public class CommonResult implements Serializable {

    private boolean isSuccess;//是否成功
    private String errorReason; //错误原因
    private String errorCode;//错误码

    public  CommonResult(boolean isSuccess,String errorReason){
        this.isSuccess  = isSuccess;
        this.errorReason = errorReason;
    }

    public  CommonResult(boolean isSuccess,String errorReason,String errorCode){
        this.isSuccess  = isSuccess;
        this.errorReason = errorReason;
        this.errorCode = errorCode;
    }
}
