package com.shangpin.supplier.product.consumer.supplier.biondioni.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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
	private String matièreDeTalon       ;
	private String Saison;
	private List<Article> ArticleList;
	
}
