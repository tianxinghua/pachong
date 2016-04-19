package com.shangpin.iog.tessabit.stock.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@XmlRootElement(name="Riferimenti")
@XmlAccessorType(XmlAccessType.FIELD)
public class Riferimenti {
	
	private String RF_RECORD_ID;
	private String RIFERIMENTO;
}
