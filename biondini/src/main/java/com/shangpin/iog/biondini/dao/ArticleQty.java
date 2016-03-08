package com.shangpin.iog.biondini.dao;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="Article")
@XmlAccessorType(XmlAccessType.FIELD)
public class ArticleQty {
	
	private String NumArticle;
	
	private String Grille;
	@XmlElement(name="QtTaille")
	private List<Qty> listQtTaille;
	
}
