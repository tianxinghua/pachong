package com.shangpin.iog.dellogliostore.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * SpuItem
 * Created by kelseo on 15/9/25.
 */
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class SpuItem {

    String name;
    @XmlElement(name = "SKU")
    String spuId;
    String price;
    String stock;
    @XmlElement(name = "product_detail")
    String description;
    @XmlElement(name = "photos")
    List<Photo> photos;
    String sex;
    String category;
    @XmlElement(name = "product_brand")
    String brand;
    @XmlElement(name = "product_material")
    String material;
    String discount;
    @XmlElement(name = "items")
    SkuItems skuItems;

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

    public SkuItems getSkuItems() {
        return skuItems;
    }

    public void setSkuItems(SkuItems skuItems) {
        this.skuItems = skuItems;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
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
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", material='" + material + '\'' +
                ", skuItems=" + skuItems +
                '}';
    }
}
