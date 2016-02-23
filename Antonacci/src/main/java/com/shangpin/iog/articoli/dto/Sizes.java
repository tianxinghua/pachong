package com.shangpin.iog.articoli.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="sizes")
public class Sizes {

	List<Size> size;
}
