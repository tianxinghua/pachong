package com.shangpin.iog.marylou.stock.dto;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Administrator on 2015/9/10.
 */
@Getter
@Setter
@XmlRootElement(name="items")
@XmlAccessorType(XmlAccessType.NONE)
public class Items {
    @XmlElement(name="item")
    private List<Item> items;
}