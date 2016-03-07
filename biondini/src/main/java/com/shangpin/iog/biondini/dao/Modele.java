package com.shangpin.iog.biondini.dao;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;



import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="Modele")
@XmlAccessorType(XmlAccessType.FIELD)
public class Modele {
	
	private String NumMdle;
	private String Libelle;
	private String CodFour                ;
	private String NomFour                ;
	private String Grille                 ;
	private String GrilleFour             ;
	private String CodMdle                ;
	private String RefFour                ;
	private String Rayon                  ;
	private String Famille                ;
	private String SFamille               ;
	private String Catégorie              ;
	private String Type          ;
	private String Talon        ;
	@javax.xml.bind.annotation.XmlElement(name="Matière-de-Talon")
	private String matièreDeTalon       ;
	private String Saison;
	
	@javax.xml.bind.annotation.XmlElement(name="Article")
	private List<Article> ArticleList;
	
}
