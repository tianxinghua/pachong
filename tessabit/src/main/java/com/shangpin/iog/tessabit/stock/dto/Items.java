package com.shangpin.iog.tessabit.stock.dto;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Administrator on 2015/9/10.
 */
@XmlRootElement(name="items")
@XmlAccessorType(XmlAccessType.NONE)
public class Items {
    @XmlElement(name="item")
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}