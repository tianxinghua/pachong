package com.shangpin.iog.anatwine.dao;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name="Header")
@XmlAccessorType(XmlAccessType.FIELD)
public class Header {
	
	private String fileCreationDateTime;
	private String batchId;
	private String totalBatchCount;
	private String batchCountNumber;

}
