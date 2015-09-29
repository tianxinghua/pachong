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
public class SizeItems {

    @XmlElement(name="item")
    List<SizeItem> sizeItems;

    public List<SizeItem> getSizeItems() {
        return sizeItems;
    }

    public void setSizeItems(List<SizeItem> sizeItems) {
        this.sizeItems = sizeItems;
    }

    @Override
    public String toString() {
        return "SizeItems{" +
                "sizeItems=" + sizeItems +
                '}';
    }
}
