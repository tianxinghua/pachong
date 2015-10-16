package com.shangpin.iog.ice.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lizhongren on 2015/10/10.
 *   返回信息类
 */
@Getter
@Setter
public class ResMessage {
    public Integer ResCode;
    public Boolean IsSuccess;
    public String  MessageRes;

}
