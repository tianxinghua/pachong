package com.shangpin.iog.antonacci.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="products")
public class Products {

	List<Product> product;
}
