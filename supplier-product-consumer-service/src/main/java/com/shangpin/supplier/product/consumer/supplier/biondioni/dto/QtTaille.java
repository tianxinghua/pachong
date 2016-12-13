package com.shangpin.supplier.product.consumer.supplier.biondioni.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QtTaille {

	private String Indice;
	private String CodeBarre;
	private String Taille;
	private String PrixAchat;
	private String PrixVente;
	private String PrixVenteSolde;
	private String PrixVentePromo;
	private String PrixVenteConseil;
	private String qty;
	
}
