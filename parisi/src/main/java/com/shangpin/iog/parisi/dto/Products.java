package com.shangpin.iog.parisi.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by loyalty on 15/6/5.
 */

@XmlRootElement(name="DocumentElement")
@XmlAccessorType(XmlAccessType.NONE)
@Getter
@Setter
@ToString
public class Products {

    @XmlElement(name="Dati")
    private List<Product> products;
}
