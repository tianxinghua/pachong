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

 private String name;
  @XmlElement(name = "SKU")
 private String spuId;
  @XmlElement(name = "producer_id")
 private String productCode;
  @XmlElement(name="retail_price_tax")
 private String marketPrice;
  @XmlElement(name="retail_price")
 private String sellPrice;
 private String price;
  @XmlElement(name="season_code")
 private String seasonCode;
 private String stock;
  @XmlElement(name = "product_detail")
 private String description;
  @XmlElement(name = "photos")
 private List<Photo> photos;
 private String sex;
 private String category;
  @XmlElement(name = "product_brand")
 private String brand;
  @XmlElement(name = "product_material")
 private String material;
 private String discount;
  @XmlElement(name = "items")
 private SkuItems skuItems;

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
