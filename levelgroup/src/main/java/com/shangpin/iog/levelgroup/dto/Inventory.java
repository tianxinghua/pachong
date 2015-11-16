package com.shangpin.iog.levelgroup.dto;

/**
 * Created by Administrator on 2015/11/16.
 */
public class Inventory {
    private String _type;
    private String ats;
    private String backorderable;
    private String id;
    private String orderable;
    private String preorderable;
    private String stock_level;

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String getAts() {
        return ats;
    }

    public void setAts(String ats) {
        this.ats = ats;
    }

    public String getBackorderable() {
        return backorderable;
    }

    public void setBackorderable(String backorderable) {
        this.backorderable = backorderable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderable() {
        return orderable;
    }

    public void setOrderable(String orderable) {
        this.orderable = orderable;
    }

    public String getPreorderable() {
        return preorderable;
    }

    public void setPreorderable(String preorderable) {
        this.preorderable = preorderable;
    }

    public String getStock_level() {
        return stock_level;
    }

    public void setStock_level(String stock_level) {
        this.stock_level = stock_level;
    }
}
