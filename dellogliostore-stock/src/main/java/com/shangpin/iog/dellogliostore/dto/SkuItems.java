package com.shangpin.iog.dellogliostore.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 *
 * Created by kelseo on 15/9/25.
 */
@XmlRootElement(name="items")
@XmlAccessorType(XmlAccessType.NONE)
public class SkuItems {

    @XmlElement(name="item")
    List<SkuItem> skuItems;

    public List<SkuItem> getSkuItems() {
        return skuItems;
    }

    public void setSkuItems(List<SkuItem> skuItems) {
        this.skuItems = skuItems;
    }

    @Override
    public String toString() {
        return "SkuItems{" +
                "skuItems=" + skuItems +
                '}';
    }
}
