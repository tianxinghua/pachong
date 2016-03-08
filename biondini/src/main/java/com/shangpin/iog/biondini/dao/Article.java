package com.shangpin.iog.biondini.dao;

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
public class Article {

	private String NumArti;
	private String SaisonArticle         ;
	private String CodArti               ;
	private String RefFourA              ;
	private String Matière               ;
	@XmlElement(name="Couleur-Principale")
	private String CouleurPrincipale    ;
	@XmlElement(name="Couleur-Secondaire")
	private String couleurSecondaire    ;
	private String Accessoire            ;
	@XmlElement(name="mat-coul-access")
	private String access       ;
	@XmlElement(name="Matière-secondaire")
	private String matièreSecondaire    ;
	
}
