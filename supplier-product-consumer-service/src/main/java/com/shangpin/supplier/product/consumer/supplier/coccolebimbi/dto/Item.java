package com.shangpin.supplier.product.consumer.supplier.coccolebimbi.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Item {

	private String uri;
	private String KEY;//sku The unique key for each product that you will pass in the future for POSTing an order to our APIs
	private String CODICE;//spu
	private String TAGLIA;//size
	
	private String TIPO_ENG;//category
	private String MODELLO_ENG;//category name
	private String DISPO;//qty
	private String GRTIPO_ENG;//gender
	private String STAGIONE_ITA;
	private String STAGIONE_ENG; //季节
	private String SESSO;
	private String SESSO_ENG;
	
	private String BRAND;//品牌
	private String ARTICOLO;  //暂定认为是货号  Manufacturer's product ID
	private String PREZZO_NETTO;//price
	private String DESCRIZIONE_LUNGA_ENG;//product_name# description
	private String DESCRIZIONE_CORTA;
	private String COLORE_ITA;//颜色
	private String COLORE_ENG;//颜色
	private String MATERIALE_ITA;//材质
	private String MATERIALE_ENG;//材质
	private String FOTO1;
	private String FOTO2;
	private String FOTO3;
	private String FOTO4;
	private String FOTO5;
	private String FOTO6;
	private String FOTO7;
	private String FOTO8;
	private String MADEIN;//产地
	private String MADEIN_ENG;//产地
}
