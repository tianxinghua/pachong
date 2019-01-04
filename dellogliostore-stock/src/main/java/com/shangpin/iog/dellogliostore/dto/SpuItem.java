package com.shangpin.iog.dellogliostore.dto;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * SpuItem
 * Created by kelseo on 15/9/25.
 */
@Getter
@Setter
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class SpuItem {

    String name;
    @XmlElement(name = "SKU")
    String spuId;
    @XmlElement(name = "producer_id")
    String productCode;
    @XmlElement(name="retail_price_tax")
    String marketPrice;
    @XmlElement(name="retail_price")
    String sellPrice;
    String price;
    @XmlElement(name="season_code")
    String seasonCode;
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

    @Override
    public String toString() {
        return "SpuItem{" +
                "name='" + name + '\'' +
                ", spuId='" + spuId + '\'' +
                ", productCode='" + productCode + '\'' +
                ", marketPrice='" + marketPrice + '\'' +
                ", sellPrice='" + sellPrice + '\'' +
                ", price='" + price + '\'' +
                ", seasonCode='" + seasonCode + '\'' +
                ", stock='" + stock + '\'' +
                ", description='" + description + '\'' +
                ", photos=" + photos +
                ", sex='" + sex + '\'' +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", material='" + material + '\'' +
                ", discount='" + discount + '\'' +
                ", skuItems=" + skuItems +
                '}';
    }
}
