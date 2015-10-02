package com.shangpin.iog.dellogliostore.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * Created by kelseo on 15/9/25.
 */
@XmlRootElement(name="item")
public class SkuItem {

    String size;
    String stock;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "SkuItem{" +
                "size='" + size + '\'' +
                ", stock='" + stock + '\'' +
                '}';
    }
}
