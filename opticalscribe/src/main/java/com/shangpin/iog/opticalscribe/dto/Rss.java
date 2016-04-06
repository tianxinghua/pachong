package com.shangpin.iog.opticalscribe.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@XmlRootElement(name="rss")
public class Rss {
//	@XmlElement(name="channel")
	private Channel channel;
//	@XmlValue
//	private String version;
}
