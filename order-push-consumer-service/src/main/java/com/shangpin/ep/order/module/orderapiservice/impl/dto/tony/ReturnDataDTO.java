package com.shangpin.ep.order.module.orderapiservice.impl.dto.tony;

/**
 * Created by Administrator on 2015/10/8.
 */
public class ReturnDataDTO {
    private String status;
    private Object messages;

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
