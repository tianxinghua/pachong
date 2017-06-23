package com.shangpin.ephub.product.business.ui.studio.studio.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/19.
 */
@Getter
@Setter
public class ErrorConent  implements Serializable{

    private static final long serialVersionUID = -4084427489725985619L;
    private String errorCode;
    private String errorMsg;
    private String spuNo;
    private Long ssid;
}
