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
@XmlRootElement(name="Resultat")
@XmlAccessorType(XmlAccessType.FIELD)
public class Resultat {
	
	@XmlElement(name="Modele")
	private List<Modele> Modele;
	
	@XmlElement(name="Table_Modele")
	private TableModele tableModele;
	
	@XmlElement(name="Table_Article")
	private TableModele table_Article;
	
	@XmlElement(name="Article")
	private List<ArticleQty> listArticleQty;
	
}
