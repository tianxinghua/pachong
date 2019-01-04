package com.shangpin.iog.parisi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by lizhongren on 2017/3/1.
 */
@XmlRootElement(name="DocumentElement")
@XmlAccessorType(XmlAccessType.NONE)
@Getter
@Setter
@ToString
public class OrderOfSupplier {
    @XmlElement(name="Response")
    private OrderDetail orderDetail;
}
