package com.shangpin.iog.ice.dto;

import java.util.List;

/**
 * Created by loyalty on 15/9/10.
 */
/*@Setter
@Getter*/
public class ICEOrderDTO {
    private String id;
    private String status;
    private String expires_on;
    private List<ICEOrderDetailDTO> order_items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpires_on() {
        return expires_on;
    }

    public void setExpires_on(String expires_on) {
        this.expires_on = expires_on;
    }

    public List<ICEOrderDetailDTO> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<ICEOrderDetailDTO> order_items) {
        this.order_items = order_items;
    }
}
