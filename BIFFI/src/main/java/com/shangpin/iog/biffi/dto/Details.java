package com.shangpin.iog.biffi.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement
public class Details {

	List<Detail> detail;
}