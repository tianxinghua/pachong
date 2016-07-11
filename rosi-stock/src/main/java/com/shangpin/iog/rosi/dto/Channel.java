package com.shangpin.iog.rosi.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="channel")
@XmlAccessorType(XmlAccessType.FIELD)
public class Channel {

	@javax.xml.bind.annotation.XmlElement(name="item")
	List<Item> item;
}
