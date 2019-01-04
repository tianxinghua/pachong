package com.shangpin.iog.prestashop.order;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name="customer")
@XmlAccessorType(XmlAccessType.FIELD)
public class Customer {

	private String id;
	private String id_default_group;
	private String id_lang;
	private String newsletter_date_add;
	private String ip_registration_newsletter;
	private String last_passwd_gen;
	private String secure_key;
	private String deleted ;
	private String passwd;
	private String lastname;
	private String firstname;
	private String email;
	private String id_gender;
	private String birthday;
	private String newsletter ;
	private String optin;
	private String website;
}
