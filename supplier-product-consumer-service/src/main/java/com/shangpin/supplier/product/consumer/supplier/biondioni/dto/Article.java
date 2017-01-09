package com.shangpin.supplier.product.consumer.supplier.biondioni.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Article {

	private String NumArti;
	private String SaisonArticle         ;
	private String CodArti               ;
	private String RefFourA              ;
	private String Matière               ;
	
	private String CouleurPrincipale    ;
	
	private String couleurSecondaire    ;
	private String Accessoire            ;
	
	private String access       ;
	
	private String matièreSecondaire    ;
	
	private TarifMagInternet tarifMagInternet;
	
}
