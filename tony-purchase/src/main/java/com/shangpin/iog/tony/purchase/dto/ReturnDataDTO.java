package com.shangpin.iog.tony.purchase.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2015/10/8.
 */
@Setter
@Getter
public class ReturnDataDTO {
    private String status;
    private Object messages;
    private Data data;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getMessages() {
        return messages;
    }

    public void setMessages(Object messages) {
        this.messages = messages;
    }
}
