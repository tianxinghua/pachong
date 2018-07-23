package com.shangpin.supplier.product.consumer.supplier.biondioni.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Article {

	private String NumArti;
	private String SaisonArticle         ;
	private String CodArti               ;
	private String RefFourA              ;
	private String Matière               ;

	private String imageUrl;
	private String spuModel;
	
	private String CouleurPrincipale    ;
	
	private String couleurSecondaire    ;
	private String Accessoire            ;
	
	private String access       ;
	
	private String matièreSecondaire    ;
	
	private TarifMagInternet tarifMagInternet;
	
}
