package com.shangpin.iog.optical.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="Row")
@XmlAccessorType(XmlAccessType.FIELD)
public class Row {

	private Cell cell;
}
