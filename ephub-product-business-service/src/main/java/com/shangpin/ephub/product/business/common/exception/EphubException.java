package com.shangpin.ephub.product.business.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2017/6/15.
 */
@Getter
@Setter
public class EphubException extends Exception {


    private static final long serialVersionUID = 2344436147020999167L;

    private String errcode;

    public EphubException(){
        super();
    }
    public EphubException(String message){
        super(message);
    }

    public EphubException(String errorCode,String message){
        super(message);
        this.setErrcode(errorCode);
    }

}
