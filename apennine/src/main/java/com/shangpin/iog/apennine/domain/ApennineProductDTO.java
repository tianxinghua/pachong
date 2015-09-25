package com.shangpin.iog.apennine.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by sunny on 2015/6/3.
 */
@Getter
@Setter
public class ApennineProductDTO {
    private Long id;//代理主键
    private String Cat;  // 品牌
    private String Cat1;//季节
    private String Cat2;//类别
    private String Cdescript;//产品中文名称
    private String Color;//颜色
    private String Createdate;//时间
    private String Descript;//产品英文名称
    private String Model;//型号
    private String Pricea;//吊牌价
    private String Priceb;//零售价
    private String Pricec;//供货价（此价格为提供给你们的价格）
    private String Scode;//货号 skuId
    private String Size;//尺寸
    private String Style;//款号

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCat() {
        return Cat;
    }

    public void setCat(String cat) {
        Cat = cat;
    }

    public String getCat1() {
        return Cat1;
    }

    public void setCat1(String cat1) {
        Cat1 = cat1;
    }

    public String getCat2() {
        return Cat2;
    }

    public void setCat2(String cat2) {
        Cat2 = cat2;
    }

    public String getCdescript() {
        return Cdescript;
    }

    public void setCdescript(String cdescript) {
        Cdescript = cdescript;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getCreatedate() {
        return Createdate;
    }

    public void setCreatedate(String createdate) {
        Createdate = createdate;
    }

    public String getDescript() {
        return Descript;
    }

    public void setDescript(String descript) {
        Descript = descript;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getPricea() {
        return Pricea;
    }

    public void setPricea(String pricea) {
        Pricea = pricea;
    }

    public String getPriceb() {
        return Priceb;
    }

    public void setPriceb(String priceb) {
        Priceb = priceb;
    }

    public String getPricec() {
        return Pricec;
    }

    public void setPricec(String pricec) {
        Pricec = pricec;
    }

    public String getScode() {
        return Scode;
    }

    public void setScode(String scode) {
        Scode = scode;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getStyle() {
        return Style;
    }

    public void setStyle(String style) {
        Style = style;
    }
}
