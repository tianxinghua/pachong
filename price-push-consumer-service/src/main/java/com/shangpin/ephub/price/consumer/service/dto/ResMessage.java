package com.shangpin.ephub.price.consumer.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResMessage {
    public int ResCode;
    public boolean IsSuccess;
    public Object MessageRes;
}
