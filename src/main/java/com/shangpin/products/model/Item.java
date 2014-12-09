package com.shangpin.products.model;

import java.util.Date;

/**
 * Created by suny on 14-11-27.
 */
public class Item {
    private Long numIid;//商品数字id
    private Date created;//tem的发布时间

    public Date getCreated() {return created;}

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getNumIid() {
        return numIid;
    }

    public void setNumIid(Long numIid) { this.numIid = numIid; }
}