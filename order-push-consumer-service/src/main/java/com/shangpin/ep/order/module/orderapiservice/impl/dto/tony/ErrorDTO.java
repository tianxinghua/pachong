package com.shangpin.ep.order.module.orderapiservice.impl.dto.tony;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 15/9/22.
 */

public class ErrorDTO {
    String message;
    String sku_id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type;
}
