package com.shangpin.iog.railSo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
@Getter
@Setter
@XmlRootElement(name="products")
@XmlAccessorType(XmlAccessType.NONE)
public class Stocks {

    @XmlElement(name="product")
    private List<Stock> stocks;
}