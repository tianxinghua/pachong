package com.shangpin.iog.railSoAtelier.dto;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="images")
public class Image {

	private String large_default;
}
