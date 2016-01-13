package com.shangpin.iog.pavinGroup.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="channel")
@XmlAccessorType(XmlAccessType.NONE)
public class Channel {
	
	private String title;
	private String link;
	private String description;
	private String pubDate;
	private String docs;
	private String generator;
	private String nextPage;
	@XmlElement(name="item")
	private List<Item> listItem;

}
