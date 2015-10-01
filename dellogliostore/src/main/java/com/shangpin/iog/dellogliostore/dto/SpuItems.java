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
public class SpuItems {

    @XmlElement(name="item")
    List<SpuItem> items;

    public List<SpuItem> getItems() {
        return items;
    }

    public void setItems(List<SpuItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "SpuItems{" +
                "items=" + items +
                '}';
    }
}
