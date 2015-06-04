package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by sunny on 2015/6/3.
 */
@Getter
@Setter
public class ApennineProductDTO {
    private Long id;//代理主键
    private String cat;  // 品牌
    private String cat1;//季节
    private String cat2;//类别
    private String cdescript;//产品中文名称
    private String color;//颜色
    private String createdate;//时间
    private String descript;//产品英文名称
    private String model;//型号
    private String pricea;//吊牌价
    private String priceb;//零售价
    private String pricec;//供货价（此价格为提供给你们的价格）
    private String scode;//货号 skuId
    private String size;//尺寸
    private String style;//款号

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getCat1() {
        return cat1;
    }

    public void setCat1(String cat1) {
        this.cat1 = cat1;
    }

    public String getCat2() {
        return cat2;
    }

    public void setCat2(String cat2) {
        this.cat2 = cat2;
    }

    public String getCdescript() {
        return cdescript;
    }

    public void setCdescript(String cdescript) {
        this.cdescript = cdescript;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPricea() {
        return pricea;
    }

    public void setPricea(String pricea) {
        this.pricea = pricea;
    }

    public String getPriceb() {
        return priceb;
    }

    public void setPriceb(String priceb) {
        this.priceb = priceb;
    }

    public String getPricec() {
        return pricec;
    }

    public void setPricec(String pricec) {
        this.pricec = pricec;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
