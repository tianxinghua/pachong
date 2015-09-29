package com.shangpin.iog.dellogliostore.dto;

import com.google.gson.annotations.SerializedName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * SpuItem
 * Created by kelseo on 15/9/25.
 */
@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.FIELD)
public class SpuItem {

    String name;
    String spuId;
    String price;
    String stock;
    String description;
    List<Photo> photos;
    String sex;
    String discount;
    @XmlElement(name="items")
    SizeItems sizeItems;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public SizeItems getSizeItems() {
        return sizeItems;
    }

    public void setSizeItems(SizeItems sizeItems) {
        this.sizeItems = sizeItems;
    }

    @Override
    public String toString() {
        return "SpuItem{" +
                "name='" + name + '\'' +
                ", spuId='" + spuId + '\'' +
                ", price='" + price + '\'' +
                ", stock='" + stock + '\'' +
                ", description='" + description + '\'' +
                ", photos=" + photos +
                ", sex='" + sex + '\'' +
                ", discount='" + discount + '\'' +
                ", sizeItems=" + sizeItems +
                '}';
    }
}
