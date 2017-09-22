package com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Order {
	private String NORDINE;//Shangpin-XXXXX
	private String NWEB;//order number.
	private String DATA;//2017/07/30 13:33:14
	private String CODCLIWEB;
	private String T_CLIENTE;//Customer's name & surname
	private String T_INDIRIZZO;//Customer's address
	private String T_CITTA;//Customer's city
	private String T_CAP;//Customer's zip code
	private String T_PROV;//Customer's state
	private String T_MODPAG;//Payment method
	private String T_EMAIL;// Customer's name
	private String SPED_NOME ;
	private String SPED_COGNOME ;
	private String SPED_IND;
	private String SPED_CAP ;
	private String SPED_CITTA ;
	private String SPED_PROV;
	private String SPED_TEL;
	private String SPED_TIPO ;
	private String SPED_NAZIONE ;
	private String CORRIERE;
	private String STATO ;//an "A" character should be passed if you need to CANCEL an order, otherwise "STATO" should not be passed in the JSON.
	private String CODICE_ART;
	private String PREZZO;

}
